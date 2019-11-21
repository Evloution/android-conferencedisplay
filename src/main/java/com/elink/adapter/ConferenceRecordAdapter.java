package com.elink.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.elink.R;
import com.elink.bean.MeetingRecordBean;

import java.util.List;

/**
 * @Description：
 * @Author： Evloution_
 * @Date： 2019/10/12
 * @Email： 15227318030@163.com
 */

public class ConferenceRecordAdapter extends BaseAdapter {

    private Context context;
    private List<MeetingRecordBean> meetingRecordBeanList;
    private LayoutInflater mInflater;

    public ConferenceRecordAdapter(Context context, List<MeetingRecordBean> meetingRecordBeanList) {
        this.context = context;
        this.meetingRecordBeanList = meetingRecordBeanList;
        this.mInflater = LayoutInflater.from(context);
    }

    public void remove(int arg0) {
        meetingRecordBeanList.remove(arg0);
        // 不要忘记更改适配器对象的数据源
        notifyData();
    }

    // 刷新页面
    public void notifyData() {
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return meetingRecordBeanList.size();
    }

    @Override
    public Object getItem(int i) {
        return meetingRecordBeanList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null) {
            view = mInflater.inflate(R.layout.item_conferencerecord_listview, null, false);
            viewHolder = new ViewHolder();
            viewHolder.record_listview_item_person_txt = view.findViewById(R.id.record_listview_item_person_txt);
            viewHolder.meetingrecord_item_meeting_time_txt = view.findViewById(R.id.meetingrecord_item_meeting_time_txt);
            viewHolder.meetingrecord_item_meeting_status_img = view.findViewById(R.id.meetingrecord_item_meeting_status_img);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        String name = meetingRecordBeanList.get(i).getMeetingName();
        viewHolder.record_listview_item_person_txt.setText(name);
        String meetingTime = meetingRecordBeanList.get(i).getMeetingTime();
        viewHolder.meetingrecord_item_meeting_time_txt.setText(meetingTime);
        String meetingStatus = meetingRecordBeanList.get(i).getMeetingStatus();
        if ("0".equals(meetingStatus) || meetingStatus == "0") {
            // 会议未开始
            viewHolder.meetingrecord_item_meeting_status_img.setImageResource(R.mipmap.meeting_not_started_icon);
        } else if ("1".equals(meetingStatus) || meetingStatus == "1") {
            // 会议正在进行中
            viewHolder.meetingrecord_item_meeting_status_img.setImageResource(R.mipmap.meeting_in_progress_icon);
        } else {
            // 会议已结束
            viewHolder.meetingrecord_item_meeting_status_img.setImageResource(R.mipmap.meeting_has_ended_icon);
        }

        return view;
    }

    public class ViewHolder {
        TextView record_listview_item_person_txt;
        TextView meetingrecord_item_meeting_time_txt;
        ImageView meetingrecord_item_meeting_status_img;
    }
}
