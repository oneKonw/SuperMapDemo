package com.example.myapplication.data;

/**
 * Created by hyt on 2018/4/7.
 */

public interface DataSource {
    //保存License路径
    void savePathOfLicense(String path);
    //读取License路径
    String getPathOfLicense();
}
