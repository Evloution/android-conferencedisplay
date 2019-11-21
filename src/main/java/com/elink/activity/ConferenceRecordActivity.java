package com.elink.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.elink.R;
import com.elink.adapter.ConferenceRecordAdapter;
import com.elink.bean.MeetingBean;
import com.elink.bean.MeetingRecordBean;
import com.elink.log.L;
import com.elink.utils.FileUtil;
import com.elink.utils.JSONAnalysisUtils;
import com.elink.utils.TimeUtil;
import com.elink.utils.ToastUtil;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * @Description：
 * @Author： Evloution_
 * @Date： 2019/10/12
 * @Email： 15227318030@163.com
 */

public class ConferenceRecordActivity extends AppCompatActivity implements View.OnClickListener {

    private String idPASideBase64;
    private List<MeetingRecordBean> meetingRecordBeanList;
    private ConferenceRecordAdapter conferenceRecordAdapter;
    private ListView conferencerecord_listview;
    private ImageView cancel_iv;
    private ImageView no_meetingrecord_img;
    private TextView no_meetingrecord_txt;

    private AlertDialog alertDialog;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conferencerecord);

        initView();
        initData();
        initEvent();
    }

    private void initView() {
        conferencerecord_listview = findViewById(R.id.conferencerecord_listview);
        cancel_iv = findViewById(R.id.cancel_iv);
        no_meetingrecord_img = findViewById(R.id.no_meetingrecord_img);
        no_meetingrecord_txt = findViewById(R.id.no_meetingrecord_txt);
        meetingRecordBeanList = new ArrayList<>();
        gson = new Gson();
    }

    private void initData() {
        cancel_iv.setOnClickListener(this);
        // 读取文件夹下的所有文件名称
        List<String> listData = FileUtil.getFilesAllName("/sdcard/ConferenceRecords/");
        L.e("文件目录：" + listData);

        if (listData == null || listData.size() == 0) {
            no_meetingrecord_img.setVisibility(View.VISIBLE);
            no_meetingrecord_txt.setVisibility(View.VISIBLE);
            return;
        }

        // 循环取出文件名称和对应的值
        for (int i = 0; i < listData.size(); i++) {
            MeetingRecordBean meetingRecordBean = new MeetingRecordBean();
            meetingRecordBean.setMeetingName(listData.get(i).substring(0, listData.get(i).length() - 4));

            idPASideBase64 = FileUtil.getFileContent(new File("/sdcard/ConferenceRecords/" + meetingRecordBean.getMeetingName() + ".txt"));
            L.e(" 内容---> " + idPASideBase64);
            String meetingStatus = JSONAnalysisUtils.JSONPersonAnalysis(idPASideBase64, "meetingStatus");
            String meetingTime = JSONAnalysisUtils.JSONPersonAnalysis(idPASideBase64, "startTime");
            if ("".equals(meetingTime) || meetingTime == null) {
                meetingTime = JSONAnalysisUtils.JSONPersonAnalysis(idPASideBase64, "createTime");
            }
            L.e("会议状态：" + meetingStatus);
            L.e("会议时间：" + meetingTime);
            meetingRecordBean.setMeetingStatus(meetingStatus);
            meetingRecordBean.setMeetingTime(meetingTime);
            meetingRecordBeanList.add(meetingRecordBean);
        }

        // 将数据按照时间排列
        Collections.sort(meetingRecordBeanList, new Comparator<MeetingRecordBean>() {
            @Override
            public int compare(MeetingRecordBean meetingRecordBean, MeetingRecordBean t1) {
                Date date1 = TimeUtil.stringToDate(meetingRecordBean.getMeetingTime());
                Date date2 = TimeUtil.stringToDate(t1.getMeetingTime());
                if (date1.before(date2)) {
                    return 1;
                }
                return -1;
            }
        });

        conferenceRecordAdapter = new ConferenceRecordAdapter(this, meetingRecordBeanList);
        conferencerecord_listview.setAdapter(conferenceRecordAdapter);
    }

    private void initEvent() {
        conferencerecord_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // 文件名称也是会议名称
                String personRecordName = meetingRecordBeanList.get(position).getMeetingName();
                idPASideBase64 = FileUtil.getFileContent(new File("/sdcard/ConferenceRecords/" + personRecordName + ".txt"));
                L.e(personRecordName + " 内容---> " + idPASideBase64);
                String meetingStatus = JSONAnalysisUtils.JSONPersonAnalysis(idPASideBase64, "meetingStatus");
                L.e("会议状态：" + meetingStatus);
                MeetingBean meetingBean = gson.fromJson(idPASideBase64, MeetingBean.class);
                L.e("meetingBean-- " + meetingBean);
                Intent intent = new Intent(ConferenceRecordActivity.this, MainActivity.class);
                intent.putExtra("meetingBean", idPASideBase64);
                startActivity(intent);
                finish();
            }
        });

        conferencerecord_listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                String personRecordName = meetingRecordBeanList.get(i).getMeetingName();
                idPASideBase64 = FileUtil.getFileContent(new File("/sdcard/ConferenceRecords/" + personRecordName + ".txt"));
                MeetingBean meetingBean = gson.fromJson(idPASideBase64, MeetingBean.class);
                L.e("meetingBean状态-- " + meetingBean.getMeetingStatus());
                if (meetingBean.getMeetingStatus() == 0) {
                    // 状态为0时，可以删除该议程
                    showEndMeetingDialog("确定要删除该会议文件吗", i, "/sdcard/ConferenceRecords/", personRecordName + ".txt");
                } else if (meetingBean.getMeetingStatus() == 1) {
                    // 状态为1时，不允许删除议程
                    ToastUtil.show(ConferenceRecordActivity.this, "该会议已在进行中不允许删除");
                } else {
                    // 状态为2时，不允许删除议程
                    ToastUtil.show(ConferenceRecordActivity.this, "该会议已结束保存不允许删除");
                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel_iv:
                finish();
                break;
        }
    }

    private void showEndMeetingDialog(String text, final int position, final String filePath, final String fileName) {
        View view = LayoutInflater.from(this).inflate(R.layout.undevice_popup_layout, null, false);
        alertDialog = new AlertDialog.Builder(this).setView(view).show();
        TextView textInfo = view.findViewById(R.id.textview);
        final Button buttonOk = view.findViewById(R.id.btn_ok);
        Button buttonCancel = view.findViewById(R.id.btn_cancel);
        textInfo.setText(text);
        // 确定退出按钮
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileUtil.deleteFile(filePath, fileName);
                conferenceRecordAdapter.remove(position);
                alertDialog.dismiss();
                ToastUtil.show(ConferenceRecordActivity.this, "删除成功");
            }
        });
        // 取消按钮
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        // 点击返回键和外部都不可取消
        alertDialog.setCancelable(false);
        alertDialog.show();
    }
}
