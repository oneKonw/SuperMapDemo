package com.example.myapplication.first;

import android.Manifest;
import android.app.Fragment;
import com.example.myapplication.R;
import com.example.myapplication.operate.OperateActivity;
import com.supermap.data.Maps;
import com.supermap.data.Workspace;
import com.supermap.data.WorkspaceConnectionInfo;
import com.supermap.data.WorkspaceType;
import com.supermap.mapping.Map;

import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by hyt on 2018/4/7.
 */

public class MainFragment extends android.support.v4.app.Fragment implements MainContract.View ,View.OnClickListener {

    private String LICENSE = "";
    private String PATH_DATA;
    private MainContract.Presenter mPresenter;
    private ListView listView;
    private List<String> List_maps = new ArrayList<>();
    private MapAdapter mapAdapter;
    private Workspace workspace = new Workspace();
    private Maps maps;
    private WorkspaceConnectionInfo workspaceConnectionInfo;


    public static MainFragment newInstance(){
        return  new MainFragment();
    }

    public MainFragment(){

    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    //初始化view
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_main_frag,container,false);
        iniView(root);
        initData();
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this.getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},11);
        }
        return root;
    }

    @Override
    public void showMap() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this.getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }else {
            openFile(2);
        }

    }

    //设置许可证路径
    @Override
    public void showSetLicense() {

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this.getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }else {
            openFile(1);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openFile(1);
                }
                break;
            case 11:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getContext(), "获取权限失败，无法进行初始化", Toast.LENGTH_SHORT).show();
                }
            default:
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                Uri uri1 = data.getData();
                //路径需要解析为父路径
                LICENSE = mPresenter.analysisLicense(uri1.getPath().toString());
                //将路径写入储存
                mPresenter.savePath(LICENSE);
                break;
            case 2:
                Uri uri2 = data.getData();

                PATH_DATA = uri2.getPath().toString();
                mPresenter.setMapListview();
                break;
        }
    }

    //打开文件夹并返回路径
    public void openFile(int i){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("lic/*;smwu/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        switch (i){
            case 1:
                try{
                    startActivityForResult(intent,1);
                }catch (Exception ex){
                    Toast.makeText(getContext(), "未找到文件管理器", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                try{
                    startActivityForResult(intent,2);
                }catch (Exception ex){
                    Toast.makeText(getContext(), "未找到文件管理器", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    //接口实现
    MapItemListener mapItemListener = new MapItemListener() {
        @Override
        public void onItemClick(String id) {
            Intent intent = new Intent(getContext(), OperateActivity.class);
            intent.putExtra("License",LICENSE);
            intent.putExtra("Dataset",PATH_DATA);
            //要打开的地图名
            intent.putExtra("NameOfMap",id);
            startActivity(intent);
        }
    };

    //item接口
    public interface MapItemListener{
        void onItemClick(String id);
    }

    //adapter
    private static class MapAdapter extends BaseAdapter{
        MapItemListener itemListener;
        List<String> nameOfMaps;
        public MapAdapter (List<String> nameOfMaps,MapItemListener itemListener){
            this.itemListener = itemListener;
            this.nameOfMaps = nameOfMaps;
        }

        @Override
        public int getCount() {
            return nameOfMaps.size();
        }

        @Override
        public Object getItem(int i) {
            return nameOfMaps.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View rowView =view;
            if (rowView == null){
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                rowView = inflater.inflate(R.layout.activity_main_item,viewGroup,false);
            }
            final String nameOfMap = nameOfMaps.get(i);
            TextView textView = (TextView)rowView.findViewById(R.id.textview);
            textView.setText(nameOfMap);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   itemListener.onItemClick(nameOfMap);
                }
            });
            return rowView;
        }
    }

    @Override
    public void showMapsListview() {
        mPresenter.refreshListview();

        workspaceConnectionInfo = new WorkspaceConnectionInfo();
        workspaceConnectionInfo.setServer(PATH_DATA);
        workspaceConnectionInfo.setType(WorkspaceType.SMWU);
        workspace.open(workspaceConnectionInfo);
        //获取地图集合对象
         maps = workspace.getMaps();
        for (int i = 0; i < maps.getCount(); i++){
            List_maps.add(maps.get(i));
        }
        mapAdapter = new MapAdapter(List_maps,mapItemListener);
        listView.setAdapter(mapAdapter);


    }

    @Override
    public void showRefreshMapsListview() {
        List_maps.clear();
    }

    //初始化数据
    private void initData(){
        LICENSE = mPresenter.getPath();
    }
    //初始化界面
    private void iniView(View root){
        TextView setLicense = (TextView)root.findViewById(R.id.tv_select_license);
        //设置监听接口
        setLicense.setOnClickListener(this);
        TextView setDataset = (TextView)root.findViewById(R.id.tv_select_dataset);
        setDataset.setOnClickListener(this);

        listView = (ListView)root.findViewById(R.id.listview1);

    }
    //按钮事件
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_select_license:
                mPresenter.pathOfLicense();
                break;
            case R.id.tv_select_dataset:
                mPresenter.pathOfDataset();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        workspace.close();
        workspace.dispose();
    }
}
