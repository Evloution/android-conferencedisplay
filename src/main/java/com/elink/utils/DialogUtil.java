package com.elink.utils;

import android.content.Context;

import com.elink.dialog.LoadingDialog;

/**
 * @Description：
 * @Author： Evloution_
 * @Date： 2019/10/11
 * @Email： 15227318030@163.com
 */

public class DialogUtil {

    private static LoadingDialog loadingDialog;

    /**
     * 请求加载框弹出框
     * @param context
     * @param text 加载框的数据
     */
    public static void loadingDialog(Context context, String text) {
        loadingDialog = new LoadingDialog(context, text);
        //点击返回键和外部都不可取消
        loadingDialog.setCancelable(false);
        // 仅点击外部不可取消
        // loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.show();
    }

    /**
     * 请求加载框消失
     * @param context
     */
    public static void loadingDialogEnd(Context context){
        loadingDialog.dismiss();
    }
}
