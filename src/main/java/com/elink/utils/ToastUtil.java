/**
 * 
 */
package com.elink.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.elink.R;


public class ToastUtil {

	public static void show(Context context, String info) {
		Toast.makeText(context.getApplicationContext(), info, Toast.LENGTH_SHORT).show();
	}

	public static void show(Context context, int info) {
		Toast.makeText(context.getApplicationContext(), info, Toast.LENGTH_LONG).show();
	}
}
