package com.yf.remotecontrolserver.common;

import android.app.Application;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;


public class App extends Application {
    //    public static final String TAG = App.class.getSimpleName();
    private static App instance;

    public static App getAppContext() {
        return instance;
    }

    public static App getInstance() {
        return instance;
    }
    @Override public void onCreate() {
        super.onCreate();
        instance = this;
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  //（可选）是否显示线程信息。 默认值为true
                .methodCount(1)         // （可选）要显示的方法行数。 默认2
                .tag("remotecontrolserver")   //（可选）每个日志的全局标记。 默认PRETTY_LOGGER
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
    }
}