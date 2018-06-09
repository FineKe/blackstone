package com.kefan.blackstone.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @note:
 * @author: 柯帆
 * @date: 2018/6/9 下午11:39
 */
public class NetWorkUtil {

    /**
     * 根据类型判断网络是否连接
     * @param context
     * @param type
     * @return
     */
    private static boolean isConnected(Context context,Integer type) {

        //获取网络管理器
        ConnectivityManager connectivityManager=((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE));

        NetworkInfo networkInfo=null;

        //获取不通类型的网络信息
        if (type == null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        } else {
            networkInfo = connectivityManager.getNetworkInfo(type);
        }

        //返回网络连接状态
        return networkInfo.isAvailable();
    }


    /**
     * 判断网络是否连接
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {
        return isConnected(context,null);
    }

    /**
     * 判断是否为wifi
     * @param context
     * @return
     */
    public static boolean isWifi(Context context) {
        return isConnected(context,ConnectivityManager.TYPE_WIFI);
    }

    /**
     * 判断是否为移动数据
     * @param context
     * @return
     */
    public static boolean isMobile(Context context) {
        return isConnected(context,ConnectivityManager.TYPE_MOBILE);
    }
}
