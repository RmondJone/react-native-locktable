package com.reactnativelocktable;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.facebook.react.PackageList;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.soloader.SoLoader;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class MainApplication extends Application implements ReactApplication {
    /**
     * 注释：Application
     * 时间：2019/9/4 0004 10:20
     * 作者：郭翰林
     */
    private static MainApplication instance;

    /**
     * 注释：当前Activity
     * 时间：2019/9/4 0004 10:09
     * 作者：郭翰林
     */
    private Activity currentActivity;


    /**
     * 注释：获取当前Application
     * 时间：2019/9/4 0004 10:21
     * 作者：郭翰林
     *
     * @return
     */
    public static MainApplication getInstance() {
        return instance;
    }

    /**
     * 注释：获取当前Activity
     * 时间：2020/7/1 0001 15:32
     * 作者：郭翰林
     *
     * @return
     */
    public Activity getCurrentActivity() {
        if (currentActivity != null) {
            return currentActivity;
        } else {
            Log.e("MainApplication", "getCurrenActivity获得的Activity为空");
            return null;
        }
    }

    private final ReactNativeHost mReactNativeHost =
            new ReactNativeHost(this) {
                @Override
                public boolean getUseDeveloperSupport() {
                    return BuildConfig.DEBUG;
                }

                @Override
                protected List<ReactPackage> getPackages() {
                    @SuppressWarnings("UnnecessaryLocalVariable")
                    List<ReactPackage> packages = new PackageList(this).getPackages();
                    // Packages that cannot be autolinked yet can be added manually here, for example:
                    // packages.add(new MyReactNativePackage());
                    return packages;
                }

                @Override
                protected String getJSMainModuleName() {
                    return "index";
                }
            };

    @Override
    public ReactNativeHost getReactNativeHost() {
        return mReactNativeHost;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        SoLoader.init(this, false);
        //关闭热更新
        if (BuildConfig.DEBUG) {
            PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("hot_module_replacement", false).apply();
        }
        initializeFlipper(this);
        registerActivityLifecycleCallbacks();
        DoraemonManager.getInstance(this).setupDoraemon();
    }

    /**
     * 注释：注册Activity生命周期监听
     * 时间：2020/3/25 0025 14:20
     * 作者：郭翰林
     */
    private void registerActivityLifecycleCallbacks() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                currentActivity = activity;
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    /**
     * Loads Flipper in React Native templates.
     *
     * @param context
     */
    private static void initializeFlipper(Context context) {
        if (BuildConfig.DEBUG) {
            try {
        /*
         We use reflection here to pick up the class that initializes Flipper,
        since Flipper library is not available in release mode
        */
                Class<?> aClass = Class.forName("com.facebook.flipper.ReactNativeFlipper");
                aClass.getMethod("initializeFlipper", Context.class).invoke(null, context);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
