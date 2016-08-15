package cn.tomoya.android.md.app;

import android.app.Application;
import android.content.Context;

import net.danlew.android.joda.JodaTimeAndroid;

import cn.tomoya.android.md.BuildConfig;
import cn.tomoya.android.md.ui.activity.CrashLogActivity;

public class AppController extends Application implements Thread.UncaughtExceptionHandler {

    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        // 初始化JodaTimeAndroid
        JodaTimeAndroid.init(this);

        // 配置全局异常捕获
        if (!BuildConfig.DEBUG) {
            Thread.setDefaultUncaughtExceptionHandler(this);
        }
    }

    @Override
    public void uncaughtException(Thread thread, Throwable e) {
        CrashLogActivity.start(this, e);
        System.exit(1);
    }

}
