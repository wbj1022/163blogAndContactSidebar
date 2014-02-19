package com.franco.hanzi2pinyin;


import android.app.Application;

public class XYZApplication extends Application {
    private static XYZApplication mInstance;
    
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        mInstance = this;
    }
    
    @Override
    public void onTerminate() {
        // TODO Auto-generated method stub
        ChineseToPinyinResource.recyleChineseToPinyin();
        super.onTerminate();
    }
    
    public static XYZApplication getXYZApplication() {
        return mInstance;
    }
}
