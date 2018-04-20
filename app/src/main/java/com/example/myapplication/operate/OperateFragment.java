package com.example.myapplication.operate;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.example.myapplication.R;
import com.example.myapplication.first.MainFragment;
import com.supermap.data.CursorType;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetVector;
import com.supermap.data.Datasource;
import com.supermap.data.DatasourceConnectionInfo;
import com.supermap.data.EngineType;
import com.supermap.data.Geometry;
import com.supermap.data.Point2D;
import com.supermap.data.QueryParameter;
import com.supermap.data.Recordset;
import com.supermap.data.Workspace;
import com.supermap.data.WorkspaceConnectionInfo;
import com.supermap.data.WorkspaceType;
import com.supermap.mapping.Action;
import com.supermap.mapping.CallOut;
import com.supermap.mapping.CalloutAlignment;
import com.supermap.mapping.ColorLegendItem;
import com.supermap.mapping.Layer;
import com.supermap.mapping.Legend;
import com.supermap.mapping.LegendItem;
import com.supermap.mapping.LegendView;
import com.supermap.mapping.MapControl;
import com.supermap.mapping.MapView;
import com.supermap.mapping.view.LayerListView;

import org.w3c.dom.Text;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by hyt on 2018/4/10.
 */

public class OperateFragment extends Fragment implements OperateContract.View ,View.OnClickListener,View.OnTouchListener{
    private OperateContract.Presenter mPresenter;

    private String license,dataset,nameOfMap;
    MapView mapView;
    LayerListView layerListView;
    MapControl mapControl;
    Workspace workspace;
    WorkspaceConnectionInfo workspaceConnectionInfo;
    private TextView tv_showall;

    private String mStrCountry;
    private String mStrCapital;
    private String mStrPop;
    private String mStrContinent;

    private Button m_btnQuery;
    private Spinner m_spnSelectContinent;
    private ImageButton m_btnSelect;

    private ArrayAdapter<String> adtSelectContinent;
    private static final String[] strContinentName = {"亚洲","欧洲","非洲","南美洲","北美洲","南极洲","大洋洲"};

    private TextView m_txtCountry;
    private TextView m_txtCapital;
    private TextView m_txtPop;
    private TextView m_txtContinent;

    private View m_DetailLayout;
    private PopupWindow pwDetailInfo;

    //静态生成
    public static OperateFragment newInstance(String license,String dataset,String NameOfMap){
        OperateFragment operateFragment = new OperateFragment();
        Bundle bundle = new Bundle();
        bundle.putString("License",license);
        bundle.putString("Dataset",dataset);
        bundle.putString("NameOfMap",NameOfMap);
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
        tv_showall = (TextView)root.findViewById(R.id.tv_showall);
        tv_showall.setOnClickListener(this);

        final ZoomControls zoomControls = (ZoomControls)root.findViewById(R.id.zoomControls);
        //按钮可见
        zoomControls.setIsZoomOutEnabled(true);
        zoomControls.setIsZoomInEnabled(true);

        workspaceConnectionInfo = new WorkspaceConnectionInfo();
        workspaceConnectionInfo.setServer(dataset);
        workspaceConnectionInfo.setType(WorkspaceType.SMWU);
        workspace = new Workspace();
        if (workspace.open(workspaceConnectionInfo)){
             workspaceConnectionInfo.dispose();
             mapControl = mapView.getMapControl();
             //手势监听
             mapControl.setGestureDetector(new GestureDetector(new MapGestureListener()));
             mapControl.getMap().setWorkspace(workspace);
             //类应该继承OnTouchListener接口
             mapControl.setOnTouchListener(this);

             //打开工作空间中的“世界地图”
             mapControl.getMap().open(nameOfMap);
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
        //缩小两倍按钮
        zoomControls.setOnZoomOutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapControl.getMap().zoom(0.5);
                mapControl.getMap().refresh();
            }
        });
        //放大两倍按钮
        zoomControls.setOnZoomInClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapControl.getMap().zoom(2);
                mapControl.getMap().refresh();
            }
        });
        //选择按钮
        m_btnSelect = (ImageButton)root.findViewById(R.id.btn_selectGeo);
        m_btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //将图层状态设置为可点击选择对象
                mapControl.setAction(Action.SELECT);
            }
        });
        m_spnSelectContinent = (Spinner) root.findViewById(R.id.spn_select_continent);
        //设置ArrayAdapter参数，绑定数据源
        adtSelectContinent = new ArrayAdapter<String>(getContext(),R.layout.support_simple_spinner_dropdown_item,strContinentName);
        adtSelectContinent.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        //绑定adapter
        m_spnSelectContinent.setAdapter(adtSelectContinent);

        //查询按钮
        m_btnQuery = (Button) root.findViewById(R.id.btn_search);
        m_btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //查询
                Query();
            }
        });
        //详情界面
        LayoutInflater lfCallOut = getLayoutInflater();
        m_DetailLayout = lfCallOut.inflate(R.layout.detailinfo,null);
        pwDetailInfo = new PopupWindow(m_DetailLayout,380, ViewGroup.LayoutParams.WRAP_CONTENT);

        m_txtCountry = (TextView)m_DetailLayout.findViewById(R.id.txt_country);
        m_txtCapital = (TextView)m_DetailLayout.findViewById(R.id.txt_capital);
        m_txtPop = (TextView)m_DetailLayout.findViewById(R.id.txt_pop);
        m_txtContinent = (TextView)m_DetailLayout.findViewById(R.id.txt_Continent);
    }

    private void initData(){
        //如果Arguments不为null
        if (getArguments()!= null){
            license = getArguments().getString("License");
            dataset = getArguments().getString("Dataset");
            nameOfMap = getArguments().getString("NameOfMap");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_showall:
                //全副显示
                mapControl.getMap().viewEntire();
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        //关闭并释放工作空间和地图控件
        //关闭顺序必须是map，mapcontrol，workspace
        mapControl.getMap().close();
        mapControl.dispose();
        mapControl = null;
        workspace.close();
        workspaceConnectionInfo.dispose();
        workspace.dispose();
    }

    //属性查询并展示
    private void Query(){
        String strContinent = m_spnSelectContinent.getSelectedItem().toString();
        //拼接查询语句
        String strFilter = "CONTINENT ='"+strContinent+"'";
        //获取第10个图层
        Layer layer = mapControl.getMap().getLayers().get(9);
        //获取图层的数据集，强制向下转换为矢量数据集
        DatasetVector datasetVector = (DatasetVector)layer.getDataset();

        //设置查询参数
        QueryParameter parameter = new QueryParameter();
        //设置查询语句
        parameter.setAttributeFilter(strFilter);
        parameter.setCursorType(CursorType.STATIC);

        // 查询，返回查询结果记录集
        Recordset recordset = datasetVector.query(parameter);

        if (recordset.getRecordCount()<1) {
            Toast.makeText(getContext(), "未搜索到对象", Toast.LENGTH_SHORT).show();
            mapControl.getMap().refresh();
            return;
        }
        //新建点
        Point2D ptInner;
        recordset.moveFirst();
        Geometry geometry = recordset.getGeometry();

        mapView.removeAllCallOut(); // 移除所有Callout
        //判断当前记录集是为最后一条，即遍历记录集
        while (!recordset.isEOF()) {
            geometry = recordset.getGeometry();
            //获取几何对象的内点坐标
            ptInner = geometry.getInnerPoint();

            LayoutInflater lfCallOut = getLayoutInflater();
            View calloutLayout = lfCallOut.inflate(R.layout.callout2, null);

            Button btnSelected = (Button)calloutLayout.findViewById(R.id.btnSelected);
            btnSelected.setText(geometry.getID() + "");
            btnSelected.setTag(geometry.getID());
            btnSelected.setOnClickListener(new detailClickListener());

            CallOut callout = new CallOut(getContext());
            callout.setContentView(calloutLayout);				// 设置显示内容
            callout.setCustomize(true);							// 设置自定义背景图片
            callout.setLocation(ptInner.getX(), ptInner.getY());// 设置显示位置
            mapView.addCallout(callout);

            recordset.moveNext();
        }

        mapView.showCallOut();								// 显示标注
        mapControl.getMap().setCenter(geometry.getInnerPoint());
        mapControl.getMap().refresh();

        // 释放资源
        recordset.dispose();
        geometry.dispose();
    }

    // ID查询
    private void QuerybyID(String id){
        String strFilter = "SMID = '" + id + "'";

        // 获得第10个图层
        Layer layer = mapControl.getMap().getLayers().get(9);
        DatasetVector datasetvector = (DatasetVector)layer.getDataset();

        // 设置查询参数
        QueryParameter parameter = new QueryParameter();
        parameter.setAttributeFilter(strFilter);
        parameter.setCursorType(CursorType.STATIC);

        // 查询，返回查询结果记录集
        Recordset recordset = datasetvector.query(parameter);

        if (recordset.getRecordCount()<1) {
            return;
        }

        recordset.moveFirst();

        mStrCountry = recordset.getFieldValue("COUNTRY").toString();
        mStrCapital = recordset.getFieldValue("CAPITAL").toString();
        mStrContinent = recordset.getFieldValue("CONTINENT").toString();
        mStrPop = recordset.getFieldValue("POP_1994").toString();

        // 释放资源
        recordset.dispose();
    }

    private ImageButton btn_Close;
    class detailClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (btn_Close != null)
                btn_Close.performClick();

            String strID = v.getTag().toString();
            System.out.println(strID);
            //按MID查询属性
            QuerybyID(strID);

            m_txtCountry.setText(mStrCountry);
            m_txtCapital.setText(mStrCapital);
            m_txtPop.setText(mStrPop);
            m_txtContinent.setText(mStrContinent);

//			pwDetailInfo.showAtLocation(m_mapControl, Gravity.NO_GRAVITY, 8, 86);
            pwDetailInfo.showAsDropDown(v, 60, -60);
            btn_Close = (ImageButton)m_DetailLayout.findViewById(R.id.btn_close);
            //点击关闭
            btn_Close.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    pwDetailInfo.dismiss();
                }
            });
        }
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        mapControl.onMultiTouch(motionEvent);
        return true;
    }

    //重写手势监听器
    class MapGestureListener extends GestureDetector.SimpleOnGestureListener{
        //重写长按方法
        @Override
        public void onLongPress(MotionEvent e) {
            //新建记录集，通过记录集，可以对矢量数据集中的数据进行参考
            Recordset rt = null;
            //获取世界行政区图层，也即是第十个图层
            Layer ly = mapControl.getMap().getLayers().get(9);
            //获取图层中的选择集对象，并将选择集对象转换为记录集
            rt = ly.getSelection().toRecordset();
            //防止出错
            if (rt != null){
                if (rt.getRecordCount()<1){
                    return;
                }
                //根据字段序号指定字段，返回数据集的属性数据表中当前记录相应字段的值
                mStrCountry = rt.getFieldValue("COUNTRY").toString();
                mStrCapital = rt.getFieldValue("CAPITAL").toString();
                mStrContinent = rt.getFieldValue("CONTINENT").toString();
                mStrPop = rt.getFieldValue("POP_1994").toString();

                //几何类型的基类
                Geometry geometry = rt.getGeometry();
                Point2D ptInner = geometry.getInnerPoint();
                LayoutInflater IfCallOut = getLayoutInflater();
                View calloutLayout = IfCallOut.inflate(R.layout.callout,null);
                TextView txtBubbleTitle = (TextView)calloutLayout.findViewById(R.id.edtBubbleTitle);
                TextView txtBubbleText = (TextView)calloutLayout.findViewById(R.id.edtBubbleText);
                txtBubbleTitle.setText(mStrCountry);
                txtBubbleText.setText(mStrCapital);
                CallOut callOut = new CallOut(getContext());
                callOut.setContentView(calloutLayout);//显示内容
                callOut.setStyle(CalloutAlignment.BOTTOM);//设置对齐方式
                callOut.setLocation(ptInner.getX(),ptInner.getY());//设置显示位置
                mapView.removeAllCallOut();//清除原有标注
                mapView.addCallout(callOut);//添加当前标注
                mapView.showCallOut();//显示当前标注
            }
            super.onLongPress(e);
        }
    }
}
