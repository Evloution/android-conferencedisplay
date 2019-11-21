package com.elink.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elink.R;


/**
 * @author Evloution_
 * @date 2018/9/20
 * @explain 验证登陆加载框
 */
public class LoadingDialog extends Dialog {

    private TextView tv;
    private String text;
    private static String TAG = "LoadingDialog";

    public LoadingDialog(Context context, String text) {
        super(context);
        this.text = text;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉默认的title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉白色边角 小米手机在xml里设置 android:background="@android:color/transparent"居然不生效
        //所以在代码里设置，不知道是不是小米手机的原因
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.dialog_load_wait);
        Log.i(TAG, "LoadingDialog onCreate");
        tv = (TextView) findViewById(R.id.tv);
        tv.setText(text);
        LinearLayout linearLayout = (LinearLayout) this.findViewById(R.id.LinearLayout);
        linearLayout.getBackground().setAlpha(210);
    }
}
