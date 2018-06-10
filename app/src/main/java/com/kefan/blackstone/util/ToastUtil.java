package com.kefan.blackstone.util;

import android.content.Context;
import android.widget.Toast;

/**
 * @note: toast 工具类
 * @author: 柯帆
 * @date: 2018/6/10 下午12:16
 */
public class ToastUtil {


    public static void showToast(Context context,String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void showCreateRequestError(Context context) {

        showToast(context,"请求创建失败");

    }
}
