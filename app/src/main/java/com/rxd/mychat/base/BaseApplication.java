package com.rxd.mychat.base;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.rxd.mychat.db.BaseManager;

import org.jivesoftware.smack.tcp.XMPPTCPConnection;

/**
 * Created by Administrator on 2017/7/6.
 */

public class BaseApplication extends Application{

    private static BaseApplication baseApplication;
    public static XMPPTCPConnection xmpptcpConnection;


    @Override
    public void onCreate() {
        super.onCreate();
        baseApplication = this;
        //初始化数据库
        BaseManager.initOpenHelper(this);
        //初始化logger
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    public static Context getAppContext() {
        return baseApplication;
    }
    public static Resources getAppResources() {
        return baseApplication.getResources();
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
    }


}
