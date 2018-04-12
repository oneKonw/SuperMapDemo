package com.example.myapplication.operate;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ZoomControls;

import com.example.myapplication.R;
import com.supermap.data.Dataset;
import com.supermap.data.Datasource;
import com.supermap.data.DatasourceConnectionInfo;
import com.supermap.data.EngineType;
import com.supermap.data.Workspace;
import com.supermap.data.WorkspaceConnectionInfo;
import com.supermap.data.WorkspaceType;
import com.supermap.mapping.ColorLegendItem;
import com.supermap.mapping.Legend;
import com.supermap.mapping.LegendItem;
import com.supermap.mapping.LegendView;
import com.supermap.mapping.MapControl;
import com.supermap.mapping.MapView;
import com.supermap.mapping.view.LayerListView;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by hyt on 2018/4/10.
 */

public class OperateFragment extends Fragment implements OperateContract.View {
    private OperateContract.Presenter mPresenter;

    private String license,dataset;
    MapView mapView;
    LayerListView layerListView;
    MapControl mapControl;

    //静态生成
    public static OperateFragment newInstance(String license,String dataset){
        OperateFragment operateFragment = new OperateFragment();
        Bundle bundle = new Bundle();
        bundle.putString("License",license);
        bundle.putString("Dataset",dataset);
        operateFragment.setArguments(bundle);
        return operateFragment;
    }

    public OperateFragment(){

    }
    @Override
    public void setPresenter(OperateContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.operate_frag,container,false);
        initData();
        initView(root);

        return root;
    }

    private void initView(View root){
        //region 打开udb
//        Workspace workspace = new Workspace();
//
//        DatasourceConnectionInfo datasourceConnectionInfo = new DatasourceConnectionInfo();
//        datasourceConnectionInfo.setServer(dataset);
//        datasourceConnectionInfo.setEngineType(EngineType.UDB);
//        Datasource datasource = workspace.getDatasources().open(datasourceConnectionInfo);
//
//        //建立地图空间与工作空间的联系
//        mapView = (MapView) root.findViewById(R.id.map_view);
//        MapControl mapControl = mapView.getMapControl();
//        mapControl.getMap().setWorkspace(workspace);
//        //打开第二幅地图
//        Dataset dataset = datasource.getDatasets().get(1);
//        mapControl.getMap().getLayers().add(dataset,true);
//
//        layerListView = root.findViewById(R.id.layerView);
        //endregion
        mapView = (MapView) root.findViewById(R.id.map_view);
        layerListView = (LayerListView)root.findViewById(R.id.layerView);

        WorkspaceConnectionInfo workspaceConnectionInfo = new WorkspaceConnectionInfo();
        workspaceConnectionInfo.setServer(dataset);
        workspaceConnectionInfo.setType(WorkspaceType.SMWU);
        Workspace workspace = new Workspace();
        if (workspace.open(workspaceConnectionInfo)){
             workspaceConnectionInfo.dispose();
             mapControl = mapView.getMapControl();
             mapControl.getMap().setWorkspace(workspace);
             mapControl.getMap().open("世界地图");
             layerListView.loadMap(mapControl.getMap());
              //region 图例列表
//            legendView.setRowHeight(36);
//            legendView.setTextSize(10);
//            legendView.setTextColor(R.color.colorAccent);
//            Legend legend = new Legend(mapControl.getMap());
//            ColorLegendItem itemRoad = new ColorLegendItem();
//            itemRoad.setCaption("道路");
//            itemRoad.setColor(android.graphics.Color.rgb(255,222,173));
//            ColorLegendItem itemBuild=new ColorLegendItem();
//            itemBuild.setCaption("建筑");
//            itemBuild.setColor(android.graphics.Color.rgb(210,105,30));
//            ColorLegendItem itemLand=new ColorLegendItem();
//            itemLand.setCaption("绿地");
//            itemLand.setColor(android.graphics.Color.rgb(60,179,113));
//            ColorLegendItem itemRiver=new ColorLegendItem();
//            itemRiver.setCaption("河流");
//            itemRiver.setColor(android.graphics.Color.rgb(30,144,255));
//            LegendItem itemSchool=new LegendItem();
//            itemSchool.setCaption("学校");
//            Bitmap bitmap= BitmapFactory.decodeResource(getResources(), R.drawable.school);
//            itemSchool.setBitmap(bitmap);
//            legend.addUserDefinedLegendItem(itemSchool);
//            legend.addColorLegendItem(1, itemRoad);
//            legend.addColorLegendItem(2, itemBuild);
//            legend.addColorLegendItem(2, itemLand);
//            legend.addColorLegendItem(2, itemRiver);
//            legend.connectLegendView(legendView);
            //endregion
            mapControl.getMap().refresh();
        }
    }

    private void initData(){
        //如果Arguments不为null
        if (getArguments()!= null){
            license = getArguments().getString("License");
            dataset = getArguments().getString("Dataset");
        }
    }
}
