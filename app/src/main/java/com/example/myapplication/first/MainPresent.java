package com.example.myapplication.first;

import com.example.myapplication.data.LocalDataSource;

/**
 * Created by hyt on 2018/4/7.
 */

public class MainPresent implements MainContract.Presenter {

    private  MainContract.View mMainView;
    private LocalDataSource mLocalDataSource;

    public MainPresent(MainContract.View mainView,LocalDataSource localDataSource){
        mMainView = mainView;
        mainView.setPresenter(this);
        mLocalDataSource = localDataSource;
    }
    @Override
    public void start() {
    }


    @Override
    public void pathOfLicense() {
        mMainView.showSetLicense();
    }

    @Override
    public void pathOfDataset() {
        mMainView.showMap();
    }

    @Override
    public String analysisLicense(String string) {
        String result ="";
        String[] strs = string.split("/");
        for (int i = 0; i < strs.length - 1 ; i++){
            result = result + "/" + strs[i];
        }
        return result;
    }

    @Override
    public void savePath(String path) {
        mLocalDataSource.savePathOfLicense(path);
    }

    @Override
    public String getPath() {
        String path = mLocalDataSource.getPathOfLicense();
        return path;
    }

    @Override
    public void setMapListview() {
        mMainView.showMapsListview();
    }

    @Override
    public void refreshListview() {
        mMainView.showRefreshMapsListview();
    }
}
