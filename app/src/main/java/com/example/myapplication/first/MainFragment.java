package com.example.myapplication.first;

import android.Manifest;
import android.app.Fragment;
import com.example.myapplication.R;
import com.example.myapplication.operate.OperateActivity;

import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URI;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by hyt on 2018/4/7.
 */

public class MainFragment extends android.support.v4.app.Fragment implements MainContract.View ,View.OnClickListener {

    private String LICENSE = "";
    private String PATH_DATA;
    private MainContract.Presenter mPresenter;

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
                Toast.makeText(getContext(), uri1.getPath().toString(), Toast.LENGTH_LONG).show();
                //路径需要解析为父路径
                LICENSE = mPresenter.analysisLicense(uri1.getPath().toString());
                //将路径写入储存
                mPresenter.savePath(LICENSE);
                break;
            case 2:
                Uri uri2 = data.getData();
                Toast.makeText(getContext(), uri2.getPath().toString(), Toast.LENGTH_LONG).show();

                PATH_DATA = uri2.getPath().toString();
                //将license和dataset的路劲发送到operate
                Intent intent = new Intent(getContext(), OperateActivity.class);
                intent.putExtra("License",LICENSE);
                intent.putExtra("Dataset",PATH_DATA);
                startActivity(intent);
                break;
        }
    }

    //打开文件夹并返回路径
    public void openFile(int i){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("lic/*");
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

    private void initData(){
        LICENSE = mPresenter.getPath();
    }

    private void iniView(View root){
        TextView setLicense = (TextView)root.findViewById(R.id.tv_select_license);
        //设置监听接口
        setLicense.setOnClickListener(this);
        TextView setDataset = (TextView)root.findViewById(R.id.tv_select_dataset);
        setDataset.setOnClickListener(this);
    }

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
}
