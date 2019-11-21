package com.elink.utils;

import android.os.Bundle;
import android.util.Log;

import com.elink.R;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description：获取时间
 * @Author： Evloution_
 * @Date： 2019/10/13
 * @Email： 15227318030@163.com
 */
public class TimeUtil {

    public static String nowStrTime() {
        Date date = new Date();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String sim = dateFormat.format(date);

        Log.i("md", "时间sim为： " + sim);
        return sim;
    }

    public static Date stringToDate(String dateString) {
        ParsePosition position = new ParsePosition(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateValue = simpleDateFormat.parse(dateString, position);
        return dateValue;
    }
}
