package com.example.myapplication.operate;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.myapplication.R;
import com.example.myapplication.util.ActivityUtils;

public class OperateActivity extends AppCompatActivity {

    OperatePresenter mOperatePresneter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String license = getIntent().getStringExtra("License");
//        String license ="/sdcard/SuperMap/License";
        String pathOfDataset = getIntent().getStringExtra("Dataset");
        String nameOfMap = getIntent().getStringExtra("NameOfMap");
        com.supermap.data.Environment.setLicensePath(license);
        com.supermap.data.Environment.initialization(this);

        setContentView(R.layout.operate_act);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);


        OperateFragment operateFragment =
                (OperateFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        //获取传入的intent中的参数


        if (operateFragment == null){
            operateFragment = OperateFragment.newInstance(license,pathOfDataset,nameOfMap);
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),operateFragment,R.id.contentFrame);
        }
        mOperatePresneter = new OperatePresenter(operateFragment);

    }

    //返回按钮
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
