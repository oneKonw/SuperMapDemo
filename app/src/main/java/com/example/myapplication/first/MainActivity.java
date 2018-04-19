package com.example.myapplication.first;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ZoomControls;
import com.example.myapplication.R;
import com.example.myapplication.data.LocalDataSource;
import com.example.myapplication.util.ActivityUtils;
import com.supermap.mapping.MapControl;
import com.supermap.mapping.MapView;

public class MainActivity extends AppCompatActivity {

    int i = 0;
    private MapView mapView;
    private MapControl mapControl;
    private ZoomControls zoomControls;

    private MainPresent mMainPresent;
    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.supermap.data.Environment.setLicensePath("/sdcard/SuperMap/License");
        com.supermap.data.Environment.initialization(this);
        setContentView(R.layout.activity_main);
        //设置toolbar
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setHomeAsUpIndicator(R.drawable.nav_menu);
        mActionBar.setDisplayHomeAsUpEnabled(true);


        //初始化view
        MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (mainFragment == null){
            mainFragment = MainFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),mainFragment,R.id.contentFrame
            );
        }
        //实例化Model
        LocalDataSource mLocalDataSource = new LocalDataSource();
        //实例化Presenter
        mMainPresent = new MainPresent(mainFragment,mLocalDataSource);


//
        //打开工作空间
//        final Workspace workspace =new Workspace();
////        final WorkspaceConnectionInfo info = new WorkspaceConnectionInfo();
////        info.setServer("/sdcard/SuperMap/GeometryInfo/World.smwu");
////        info.setType(WorkspaceType.SMWU);
////        workspace.open(info);
//        DatasourceConnectionInfo info = new DatasourceConnectionInfo();
//        info.setServer("/sdcard/SuperMap/GeometryInfo/World.udb");
//        info.setEngineType(EngineType.UDB);
//
//        final Datasource datasource = workspace.getDatasources().open(info);
//
//        //建立地图控件与工作空间的联系
//        mapView = (MapView) findViewById(R.id.map_view);
//        mapControl = mapView.getMapControl();
//        mapControl.getMap().setWorkspace(workspace);
//
//        //打开工作空间的地图
//        Dataset dataset = datasource.getDatasets().get(1);
//        mapControl.getMap().getLayers().add(dataset,true);
////        final String mapName = workspace.getMaps().get(0);
////        mapControl.getMap().open(mapName);
//        zoomControls = (ZoomControls) findViewById(R.id.zoomControls);
//        zoomControls.setIsZoomInEnabled(true);
//        zoomControls.setIsZoomOutEnabled(true);
//        zoomControls.setOnZoomInClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mapControl.getMap().zoom(2);
//                mapControl.getMap().refresh();
//            }
//        });
//        zoomControls.setOnZoomOutClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mapControl.getMap().zoom(0.5);
//                mapControl.getMap().refresh();
//            }
//        });
//
//        TextView tv_close = (TextView) findViewById(R.id.map_close);
//        final TextView tv_show = (TextView) findViewById(R.id.tv_showudb);
//        tv_close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //先关闭再释放对象
//                mapControl.getMap().close();
////                mapControl.dispose();
////                mapControl = null;
////                workspace.close();//先关闭再释放
////                info.dispose();
////                workspace.dispose();
//                mapControl.getMap().open(workspace.getMaps().get(1));
//            }
//        });
//        tv_show.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String datas = datasource.getDatasets().get(i).getName();
//                tv_show.setText(datas);
//                i++;
//            }
//        });
    }
}















