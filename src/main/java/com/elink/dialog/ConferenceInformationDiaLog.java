package com.elink.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.elink.R;
import com.elink.log.L;

import java.util.List;
import java.util.Map;

/**
 * @Description：
 * @Author： Evloution_
 * @Date： 2019/10/12
 * @Email： 15227318030@163.com
 */
public class ConferenceInformationDiaLog extends Dialog {

    private ListView dialog_conferencerecord_listview;
    private SimpleAdapter infomationadapter;
    private List<Map<String, Object>> list;
    private Context context;

    public ConferenceInformationDiaLog(Context context, List<Map<String, Object>> list) {
        super(context);
        this.context = context;
        this.list = list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉默认的title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉白色边角 我的小米手机在xml里设置 android:background="@android:color/transparent"居然不生效
        //所以在代码里设置，不知道是不是小米手机的原因
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.dialog_conferencerecord);
        /*LinearLayout linearLayout = (LinearLayout) this.findViewById(R.id.linearLayout);
        linearLayout.getBackground().setAlpha(210);*/
        L.e("LoginFailDialog onCreate");

        dialog_conferencerecord_listview = findViewById(R.id.dialog_conferencerecord_listview);
        infomationadapter = new SimpleAdapter(context, list, R.layout.item_dialog_conferencerecord_listview, new String[]{"agendaName", "personName"}, new int[]{R.id.item_dialog_agenda_txt, R.id.item_dialog_person_txt});
        dialog_conferencerecord_listview.setAdapter(infomationadapter);
    }
}
