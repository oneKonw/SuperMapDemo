package com.example.myapplication.operate;

/**
 * Created by hyt on 2018/4/10.
 */

public class OperatePresenter implements OperateContract.Presenter {

    private OperateContract.View mOperateView;

    public OperatePresenter(OperateContract.View operateView){
         mOperateView = operateView;
         operateView.setPresenter(this);
    }

    @Override
    public void start() {

    }
}
