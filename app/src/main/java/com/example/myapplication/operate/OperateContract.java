package com.example.myapplication.operate;

import com.example.myapplication.BasePresenter;
import com.example.myapplication.BaseView;
import com.example.myapplication.first.MainContract;

/**
 * Created by hyt on 2018/4/10.
 */

public interface OperateContract {
    interface View extends BaseView<Presenter>{

    }

    interface Presenter extends BasePresenter{

    }
}
