package com.reactnativelocktable;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.didichuxing.doraemonkit.DoraemonKit;
import com.didichuxing.doraemonkit.kit.Category;
import com.didichuxing.doraemonkit.kit.IKit;
import com.facebook.react.ReactApplication;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.foucstech.xyz.function.floatwindow.DebugPop;

import java.util.ArrayList;


/**
 * 注释：多啦A梦管理类
 * 时间：2020/7/1 0001 17:22
 * 作者：郭翰林
 */
public class DoraemonManager {

    private static final String KEY_RN_DEBUG_HTTP_HOST = "debug_http_host";
    private static final String KEY_API_HOST = "API_HOST";

    private static DoraemonManager manager;
    private MainApplication application;

    private DoraemonManager(MainApplication application) {
        this.application = application;
    }

    public static DoraemonManager getInstance(MainApplication application) {
        if (manager == null) {
            manager = new DoraemonManager(application);
        }
        return manager;
    }

    public void setupDoraemon() {
        ArrayList<IKit> iKits = new ArrayList<>(getKits());
        DoraemonKit.install(application, iKits);
        setupDebugPop();
    }

    /**
     * 初始化debug悬浮球
     */
    private void setupDebugPop() {
        if (!BuildConfig.DEBUG) {
            return;
        }
        initDebugHostConfig();
    }

    private void initDebugHostConfig() {
        String debugApiConfig = getDebugPopApiConfig(KEY_API_HOST, "https://app.91xinbei.cn");
        if (!TextUtils.isEmpty(debugApiConfig)) {
            PreferenceManager.getDefaultSharedPreferences(application).edit().putString(KEY_API_HOST, debugApiConfig).apply();
        }
    }

    @NonNull
    private String getDebugPopApiConfig(String keyApiHost, String s) {
        return PreferenceManager.getDefaultSharedPreferences(application).getString(keyApiHost, s);
    }

    public ArrayList<IKit> getKits() {
        ArrayList<IKit> iKits = new ArrayList<>();
        iKits.add(new IKit() {
            @Override
            public int getCategory() {
                return Category.BIZ;
            }

            @Override
            public int getName() {
                return R.string.api_host;
            }

            @Override
            public int getIcon() {
                return R.drawable.ic_api_host;
            }

            @Override
            public void onClick(Context context) {
                DebugPop.showDialog(application.getCurrentActivity(),
                        R.array.api_debug_host_list,
                        R.string.select_node_host,
                        () -> getDebugPopApiConfig(KEY_API_HOST, "https://app.91xinbei.cn"),
                        url -> {
                            PreferenceManager.getDefaultSharedPreferences(MainApplication.getInstance()).edit().putString(KEY_API_HOST, url).apply();
                            notifyRNChangeHost(url);
                        });
            }

            @Override
            public void onAppInit(Context context) {

            }
        });
        iKits.add(new IKit() {
            @Override
            public int getCategory() {
                return Category.BIZ;
            }

            @Override
            public int getName() {
                return R.string.rn_host;
            }

            @Override
            public int getIcon() {
                return R.drawable.ic_rn_host;
            }

            @Override
            public void onClick(Context context) {
                try {
                    DebugPop.showDialog(application.getCurrentActivity(),
                            R.array.rn_code_host_list,
                            R.string.select_rn_host,
                            () -> getDebugPopApiConfig(KEY_RN_DEBUG_HTTP_HOST, "localhost:8081"),
                            url -> PreferenceManager.getDefaultSharedPreferences(MainApplication.getInstance()).edit().putString(KEY_RN_DEBUG_HTTP_HOST, url).apply());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onAppInit(Context context) {

            }
        });
        iKits.add(new IKit() {
            @Override
            public int getCategory() {
                return Category.BIZ;
            }

            @Override
            public int getName() {
                return R.string.reload_js;
            }

            @Override
            public int getIcon() {
                return R.drawable.ic_reload_js;
            }

            @Override
            public void onClick(Context context) {
                ((ReactApplication) context).getReactNativeHost().getReactInstanceManager().getDevSupportManager().handleReloadJS();
            }

            @Override
            public void onAppInit(Context context) {

            }
        });
        iKits.add(new IKit() {
            @Override
            public int getCategory() {
                return Category.BIZ;
            }

            @Override
            public int getName() {
                return R.string.rn_debug;
            }

            @Override
            public int getIcon() {
                boolean isDebug = application.getReactNativeHost().getReactInstanceManager().getDevSupportManager().getDevSettings().isRemoteJSDebugEnabled();
                if (isDebug) {
                    return R.drawable.ic_switch_on;
                } else {
                    return R.drawable.ic_switch_off;
                }
            }

            @Override
            public void onClick(Context context) {
                boolean isDebug = ((ReactApplication) context).getReactNativeHost().getReactInstanceManager().getDevSupportManager().getDevSettings().isRemoteJSDebugEnabled();
                ((ReactApplication) context).getReactNativeHost().getReactInstanceManager().getDevSupportManager().getDevSettings().setRemoteJSDebugEnabled(!isDebug);
                ((ReactApplication) context).getReactNativeHost().getReactInstanceManager().getDevSupportManager().handleReloadJS();
            }

            @Override
            public void onAppInit(Context context) {

            }
        });
        iKits.add(new IKit() {
            @Override
            public int getCategory() {
                return Category.BIZ;
            }

            @Override
            public int getName() {
                return R.string.rn_options;
            }

            @Override
            public int getIcon() {
                return R.drawable.ic_react_logo;
            }

            @Override
            public void onClick(Context context) {
                ((ReactApplication) context).getReactNativeHost().getReactInstanceManager().getDevSupportManager().showDevOptionsDialog();
            }

            @Override
            public void onAppInit(Context context) {

            }
        });
        iKits.add(new IKit() {
            @Override
            public int getCategory() {
                return Category.BIZ;
            }

            @Override
            public int getName() {
                return R.string.go_setting;
            }

            @Override
            public int getIcon() {
                return R.drawable.ic_setting;
            }

            @Override
            public void onClick(Context context) {
                Intent mIntent = new Intent();
                mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                mIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
                context.startActivity(mIntent);

            }

            @Override
            public void onAppInit(Context context) {
            }
        });
        iKits.add(new IKit() {
            @Override
            public int getCategory() {
                return Category.BIZ;
            }

            @Override
            public int getName() {
                return R.string.wifi_setting;
            }

            @Override
            public int getIcon() {
                return R.drawable.ic_wifi;
            }

            @Override
            public void onClick(Context context) {
                Intent mIntent = new Intent();
                mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mIntent.setAction(Settings.ACTION_WIFI_SETTINGS);
                context.startActivity(mIntent);
            }

            @Override
            public void onAppInit(Context context) {
            }
        });
        return iKits;
    }


    /**
     * 在运行时通知RN更改Host
     */
    private void notifyRNChangeHost(String host) {
        WritableMap params = Arguments.createMap();
        params.putString("host", host);
        ReactContext currentReactContext = MainApplication.getInstance().getReactNativeHost().getReactInstanceManager().getCurrentReactContext();
        if (currentReactContext != null) {
            currentReactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit("changeHost", params);
        }
    }


}
