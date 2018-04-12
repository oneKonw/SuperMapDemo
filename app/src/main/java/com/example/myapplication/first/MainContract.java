package com.example.myapplication.first;

import com.example.myapplication.BasePresenter;
import com.example.myapplication.BaseView;

/**
 * Created by hyt on 2018/4/7.
 */

public interface MainContract {
    interface View extends BaseView<Presenter>{
        void showMap();
        void showSetLicense();
    }
    interface Presenter extends BasePresenter{
        void pathOfLicense();
        void pathOfDataset();
        //解析路径
        String analysisLicense(String string);
        //保存License路径
        void savePath(String path);
        //读取License路径
        String getPath();
    }
}






