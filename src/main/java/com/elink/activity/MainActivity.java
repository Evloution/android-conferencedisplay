package com.elink.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.elink.R;
import com.elink.adapter.DragSortAdapter;
import com.elink.adapter.RecyclerViewAdapter;
import com.elink.bean.AgendaBean;
import com.elink.bean.MeetingBean;
import com.elink.bean.PersonBean;
import com.elink.log.L;
import com.elink.utils.AESEncryptUtil;
import com.elink.utils.AESUtil;
import com.elink.utils.DialogUtil;
import com.elink.utils.IPTestingUtil;
import com.elink.utils.TimeUtil;
import com.elink.utils.ToastUtil;
import com.elink.view.DragSortListView;
import com.google.gson.Gson;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, DragSortAdapter.DragSortCallback, RecyclerViewAdapter.RecyclerVieCallback {

    // 会议名称控件
    private TextView meeting_name_text;

    private Button meeting_create_btn;
    private ImageButton meeting_end_btn;
    private ImageButton meeting_setip_imgbtn;
    private LinearLayout linar;

    private DragSortListView dragSortListView;
    private DragSortAdapter dragSortAdapter;
    private OkHttpClient okHttpClient;
    private AlertDialog alertDialog;

    private List<MeetingBean> meetingBeanList;
    private List<AgendaBean> agendaBeanList;
    private List<PersonBean> personBeanList;


    private MeetingBean meetingBean;
    private AgendaBean agendaBean;
    private int REQUEST_WRITE_EXTERNAL_STORAGE = 101;

    private String meeting_name;
    private String meetingBeanIntent;

    private SharedPreferences sp;
    private String ipName;

    // 判断是否需要补发数据
    private int requestNum = 0;
    private int requestNums = 0;

    private Timer timer;

    private Gson gson = new Gson();

    /**
     * 拖动议程时触发的事件
     */
    private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
        @Override
        public void drop(int from, int to) {// from to 分别表示 被拖动控件原位置 和目标位置
            AgendaBean item = (AgendaBean) dragSortAdapter.getItem(from);// 得到listview的适配器
            if (from != to) {
                AgendaBean itemTo = (AgendaBean) dragSortAdapter.getItem(to);// 得到listview的适配器
                if (item.getAgendaStatus() > 0 || itemTo.getAgendaStatus() > 0) {
                    ToastUtil.show(MainActivity.this, "不允许拖动");
                } else {
                    dragSortAdapter.remove(from);// 在适配器中”原位置“的数据。
                    dragSortAdapter.insert(item, to);// 在目标位置中插入被拖动的控件。
                    meetingBean.save();
                    // 发送全部数据
                    sendAllData();
                }
            } else {
                item.setDisplayPerson(false);
                dragSortAdapterNotify();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
    }

    private void initView() {
        meeting_name_text = findViewById(R.id.meeting_name_text);
        dragSortListView = findViewById(R.id.activity_create_meeting_listview);
        meeting_create_btn = findViewById(R.id.meeting_create_btn);
        meeting_end_btn = findViewById(R.id.meeting_end_btn);
        meeting_setip_imgbtn = findViewById(R.id.meeting_setip_imgbtn);
        linar = findViewById(R.id.linar);
        meetingBeanList = new ArrayList<>();
        okHttpClient = new OkHttpClient();

        sp = getSharedPreferences("ip", Context.MODE_PRIVATE);
        ipName = sp.getString("ipName", "");

        Intent intent = getIntent();
        meeting_name = intent.getStringExtra("meeting_name");
        L.e("meeting_name：" + meeting_name);
        // 走这里说明是从创建会议页面过来的
        if (meeting_name != null) {
            meetingBean = new MeetingBean(meeting_name, new ArrayList<AgendaBean>());
            meetingBean.setStartTime("");
            meetingBean.setEndTime("");
            meetingBean.setCreateTime(TimeUtil.nowStrTime());
            meetingBean.setMeetingStatus(0);
            meetingBeanList.add(meetingBean);
            L.e("会议名称：" + meetingBeanList);
            L.e("会议名称json：" + gson.toJson(meetingBean));
            meetingBean.save();
            meeting_name_text.setText(meeting_name);
            // 判断议程按钮和结束按钮
            isShowCreateAgendaOrEndMeetingBtn();
        }

        // 走这里说明是从会议记录页面过来的
        meetingBeanIntent = intent.getStringExtra("meetingBean");
        if (meetingBeanIntent != null) {
            L.e("meetingBeanIntent：" + meetingBeanIntent);
            meetingBean = gson.fromJson(meetingBeanIntent, MeetingBean.class);
            meeting_name_text.setText(meetingBean.getMeetingName());
            L.e("meetingBean：" + meetingBean);
            agendaBeanList = meetingBean.getAgendaBeanList();
            L.e("agendaBeanList：" + agendaBeanList);
            // 判断议程按钮和结束按钮
            isShowCreateAgendaOrEndMeetingBtn();
            initEvent();
        }
    }

    private void initData() {
        meeting_create_btn.setOnClickListener(this);
        meeting_end_btn.setOnClickListener(this);
        meeting_setip_imgbtn.setOnClickListener(this);
        linar.setOnClickListener(this);
        dragSortListView.setDragEnabled(true); // 设置是否可拖动。
        dragSortListView.setDropListener(onDrop);
        if (meetingBean.getMeetingStatus() == 0) {
            meeting_end_btn.setImageResource(R.mipmap.meeting_start_icon);
        } else if (meetingBean.getMeetingStatus() == 1) {
            meeting_end_btn.setImageResource(R.mipmap.meeting_end_icon);
        } else {
            meeting_end_btn.setVisibility(View.GONE);
        }
        // dragSortListView.setRemoveListener(onRemove);
    }

    private void initEvent() {
        dragSortAdapter = new DragSortAdapter(MainActivity.this, agendaBeanList, this);
        dragSortAdapter.notifyDataSetChanged();
        dragSortListView.setAdapter(dragSortAdapter);
    }

    /**
     * 对父view (main) 中的按钮的点击事件
     * 设置ip按钮，会议结束和开始按钮、添加议程按钮
     * @param v
     */
    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.meeting_setip_imgbtn: // 设置IP
                addPersonDialog("请输入ip地址", "设置ip地址", ipName, 2, 0);
                break;
            case R.id.linar: // 获取其他区域点击事件后取消人员删除按钮
                signOutHideDelete();
                break;
            case R.id.meeting_end_btn: // 会议结束按钮

                if (meetingBean.getMeetingStatus() == 0) {
                    // 开始会议
                    DialogUtil.loadingDialog(this, "发送数据中...");
                    if (meetingBean.getAgendaBeanList().size() == 0 || meetingBean.getAgendaBeanList() == null) {
                        ToastUtil.show(MainActivity.this, "请先添加议程再开始会议");
                        DialogUtil.loadingDialogEnd(this);
                        return;
                    }
                    getSendData(0, -1);
                } else {
                    // 结束会议
                    showEndMeetingDialog("要结束本次会议吗？", 1, 0);
                }
                break;
            case R.id.meeting_create_btn: // 添加议程按钮
                int position = 0;
                if ("".equals(meeting_name_text.getText().toString().trim()) || meeting_name_text.getText().toString().trim() == null) {
                    ToastUtil.show(MainActivity.this, "请先创建会议");
                    return;
                }

                if (dragSortAdapter != null) {
                    position = dragSortAdapter.getCount();
                }
                L.e("位置：" + position);
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, R.style.MyDialogStyle);
                View view = View.inflate(this, R.layout.dialog_agenda, null);
                final EditText et_alter_dialog_textview_name = view.findViewById(R.id.et_alter_dialog_textview_name);
                final EditText et_alter_dialog_department_name = view.findViewById(R.id.et_alter_dialog_department_name);
                et_alter_dialog_textview_name.setHint("请输入会议议程");
                et_alter_dialog_department_name.setHint("请输入科室名称");
                Button tv_alter_dialog_textview_ok = view.findViewById(R.id.tv_alter_dialog_textview_ok);
                Button tv_alter_dialog_textview_cancel = view.findViewById(R.id.tv_alter_dialog_textview_cancel);
                alertDialog
                        .setTitle("会议议程")
                        .setView(view)
                        .create();
                final AlertDialog show = alertDialog.show();
                final int finalPosition = position;
                tv_alter_dialog_textview_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = et_alter_dialog_textview_name.getText().toString().trim();
                        String departmentName = et_alter_dialog_department_name.getText().toString().trim();
                        if (name.length() == 0) {
                            ToastUtil.show(MainActivity.this, "会议议程不能为空");
                            return;
                        } else if (departmentName.length() == 0) {
                            ToastUtil.show(MainActivity.this, "科室名称不能为空");
                            return;
                        } else {
                            agendaBeanList = meetingBean.getAgendaBeanList();
                            agendaBean = new AgendaBean(name, new ArrayList<PersonBean>());
                            agendaBean.setDepartmentName(departmentName);
                            agendaBean.setAgendaStatus(0);
                            agendaBean.setStartTime("");
                            agendaBean.setEndTime("");
                            agendaBean.setOrderCode(finalPosition);
                            agendaBean.setDisplayPerson(false);
                            agendaBeanList.add(agendaBean);
                            L.e("agendaBeanList------------------ ：" + agendaBeanList);
                            L.e("会议议程json：" + gson.toJson(agendaBean));
                            // 保存文件
                            meetingBean.save();
                            initEvent();
                            // 刷新界面
                            //dragSortAdapterNotify();
                            show.dismiss();
                            // 发送全部数据
                            sendAllData();
                        }
                    }
                });
                tv_alter_dialog_textview_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        show.dismiss();
                    }
                });
                if (dragSortAdapter != null) {
                    dragSortAdapter.notifyData();
                }
                break;
        }
    }

    /**
     *  对议程以及议程的子数据的操作
     *  点击事件主要是删除议程按钮、议程准备按钮、人员进入按钮、人员到齐按钮、添加人员按钮
     * @param v
     */
    @Override
    public void click(final View v) {
        final int position = (Integer) v.getTag();
        switch (v.getId()) {
            case R.id.dragsort_listview_item_layout: // 删除议程按钮
                showEndMeetingDialog("确定要删除该议程吗？", 2, position);
                break;
            case R.id.dragsort_listview_item_confirm_btn: // 人员到齐按钮
                DialogUtil.loadingDialog(this, "发送数据中...");
                // 判断删除人员按钮是否显示
                isDelete(position);
                // 发送本行议程数据到服务器
                getSendData(position, 3);
                break;
            case R.id.dragsort_listview_item_call_btn: // 叫人按钮
                DialogUtil.loadingDialog(this, "发送数据中...");
                // 判断删除人员按钮是否显示
                isDelete(position);
                // 发送本行议程数据到服务器
                getSendData(position, 2);
                break;
            case R.id.dragsort_listview_item_prepare_btn: // 做准备按钮
                DialogUtil.loadingDialog(this, "发送数据中...");
                // 判断当前会议有无人员
                if (agendaBeanList.get(position).getPersonList().size() == 0 || agendaBeanList.get(position).getPersonList() == null) {
                    DialogUtil.loadingDialogEnd(MainActivity.this);
                    ToastUtil.show(MainActivity.this, "请先添加人员");
                    addPersonDialog("请输入参会人员", "参会人员", "", 1, position);
                    return;
                }

                //  点击第一条数据时判断会议有没有开始
                if (position == 0) {
                    if (meetingBean.getMeetingStatus() == 0) {
                        DialogUtil.loadingDialogEnd(MainActivity.this);
                        ToastUtil.show(MainActivity.this, "请先开始本次会议");
                        return;
                    }
                }
                // 判断删除人员按钮是否显示
                isDelete(position);
                // 发送本行议程数据到服务器
                getSendData(position, 1);
                break;
            case R.id.dragsort_listview_item_create_btn: // 添加人员按钮
                // 判断删除人员按钮是否显示
                isDelete(position);
                // 添加人员弹出框
                addPersonDialog("请输入参会人员", "参会人员", "", 1, position);
                break;
        }
    }

    /**
     * 添加人员弹出框和设置ip弹出框
     *
     * @param headText  头部text
     * @param titleText 标题text
     * @param hintText  输入框的text
     * @param code      判断是哪个按钮
     * @param position  添加人员的话传入点击的是第几个数据
     */
    private void addPersonDialog(final String headText, final String titleText, final String hintText, final int code, final int position) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, R.style.MyDialogStyle);
        View view = View.inflate(this, R.layout.activity_alter_dialog_textview, null);
        final EditText et_alter_dialog_textview_name = view.findViewById(R.id.et_alter_dialog_textview_name);
        if ("".equals(hintText) || hintText == null) {
            et_alter_dialog_textview_name.setHint(headText);
        } else {
            et_alter_dialog_textview_name.setText(hintText);
        }
        Button tv_alter_dialog_textview_ok = view.findViewById(R.id.tv_alter_dialog_textview_ok);
        Button tv_alter_dialog_textview_cancel = view.findViewById(R.id.tv_alter_dialog_textview_cancel);
        alertDialog
                .setTitle(titleText)
                .setView(view)
                .create();
        final AlertDialog show = alertDialog.show();
        L.e("修改前的ip地址：" + ipName);
        tv_alter_dialog_textview_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = et_alter_dialog_textview_name.getText().toString().trim();
                if (name.length() == 0) {
                    ToastUtil.show(MainActivity.this, code == 1 ? "人员不能为空" : "IP不能为空");
                    return;
                } else {
                    if (code == 1) {
                        // 说明是添加人员功能
                        agendaBean = agendaBeanList.get(position);
                        agendaBean.setAgendaStatus(0);
                        personBeanList = agendaBean.getPersonList();
                        PersonBean personBean = new PersonBean();
                        personBean.setPersonName(name);
                        personBeanList.add(personBean);
                        L.e("整个list数据------------------ ：" + agendaBeanList + personBeanList);
                        L.e("整个数据json：" + gson.toJson(meetingBean));
                        meetingBean.save();
                        //initEvent();
                        // 刷新界面
                        dragSortAdapterNotify();
                        show.dismiss();
                        // 发送全部数据
                        sendAllData();
                    } else if (code == 2) {
                        // 判断ip格式是否正确
                        if (IPTestingUtil.isIP(name)) {
                            // ip格式正确走这里
                            sp.edit().putString("ipName", name).commit();
                            ipName = sp.getString("ipName", "");
                            show.dismiss();
                        } else {
                            ToastUtil.show(MainActivity.this, "输入的ip不正确");
                        }
                    }
                }
            }
        });
        tv_alter_dialog_textview_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show.dismiss();
            }
        });
    }

    /**
     * 删除人员按钮的点击事件
     *
     * @param view
     * @param agendaBean 议程的bean，从这个bean中的personList中找出人员
     */
    @Override
    public void recyclerVieClick(View view, AgendaBean agendaBean) {
        int postion = (int) view.getTag();
        L.e("postion-- " + postion);
        switch (view.getId()) {
            case R.id.item_recyclerview_delete_imgbtn:
                // 发送全部数据
                sendAllData();
                agendaBean.getPersonList().remove(postion);
                if (agendaBean.getPersonList().size() == 0 || agendaBean.getPersonList() == null) {
                    agendaBean.setDisplayPerson(false);
                }
                dragSortAdapter.notifyData();
                meetingBean.save();
                break;
        }
    }

    /**
     * 根据点击的按钮生成不同的数据，并将数据发送出去
     *
     * @param position 传入点击列表的索引值
     * @param code     根据这个code值来判断点击的是什么按钮
     *                 -2 心跳数据； 心跳数据是在会议开始后对会议有任何修改的并发送数据未成功的会走这里
     *                 -1 会议开始；
     *                 0  发送全部数据； 会议开始后对会议内容做改变的会走这里
     *                 1  请人员做准备； 点击准备按钮，会走这里
     *                 2  请人员进入会议室； 点击叫人按钮，走这里
     *                 3  人员确认进入会议室； 点击确认按钮，走这里
     *                 5  会议结束按钮；
     */
    private void getSendData(int position, int code) {
        // 如果ip地址为空
        if ("".equals(ipName) || ipName == null) {
            DialogUtil.loadingDialogEnd(MainActivity.this);
            ToastUtil.show(MainActivity.this, "请先输入IP地址");
            addPersonDialog("请输入ip地址", "设置ip地址", ipName, 2, 0);
            return;
        }
        String json;
        String aesJson;
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        if (code == 0 || code == -1) {
            // 证明是开始会议时发送的数据
            map.put("data", meetingBean);
            json = gson.toJson(map);
            L.e("整体数据：" + json);
        } else if (code == 5) {
            // 会议结束
            json = gson.toJson(map);
        } else if (code == -2) {
            // 判断服务器是否开启
            json = gson.toJson(map);
        } else {
            // 走这里说明点击的按钮是准备、叫人、到齐。
            AgendaBean agendaBean = agendaBeanList.get(position);
            List<PersonBean> personBeanList = agendaBean.getPersonList();

            map.put("meetingName", meeting_name_text.getText().toString().trim());
            map.put("agendaName", agendaBean.getName());
            map.put("departmentName", agendaBean.getDepartmentName());
            map.put("personName", personBeanList);
            json = gson.toJson(map);
            L.e("map转json： " + json);
        }
        aesJson = AESUtil.encryptDataToStr(json, "52957c911899450f98a376a147aac50e");
        L.e("加密数据：" + aesJson);
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain;chaset=utf-8"), aesJson);
        // 192.168.0.118
        Request request = new Request.Builder().url("http://" + ipName + ":9007/").post(requestBody).build();
        executeRequest(request, position, code);
    }

    /**
     * 对请求结果的封装
     * @param request 请求
     * @param position 列表数据的索引值； 主要作为点击准备、叫人、到齐按钮的那一行数据做改变
     * @param code 根据这个code值来判断点击的是什么按钮，该往哪个方法走。 code 的分类请看 getSendData() 这个方法
     */
    private void executeRequest(Request request, final int position, final int code) {

        // 设置请求的超时时间
        okHttpClient.setConnectTimeout(5, TimeUnit.SECONDS);
        // 将request封装为call
        Call call = okHttpClient.newCall(request);
        // 执行call
        call.enqueue(new Callback() {
            // 发送数据失败
            @Override
            public void onFailure(Request request, IOException e) {
                DialogUtil.loadingDialogEnd(MainActivity.this);
                L.e("onFailure：" + e.getMessage());
                e.printStackTrace();

                // 发送次数 +1
                requestNum ++;
                if (e instanceof NoRouteToHostException || e instanceof UnknownHostException) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.show(MainActivity.this, "找不到主机，请检查ip地址是否正确");
                        }
                    });
                }

                if (e instanceof SocketTimeoutException || e instanceof SocketException) {
                    // 判断超时异常
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.show(MainActivity.this, "连接超时，请检查服务器是否开启");
                        }
                    });
                }

                if (e instanceof ConnectException) {
                    // 判断连接异常，
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.show(MainActivity.this, "发送失败，请检查网络是否打开");
                        }
                    });
                }

                L.e("requestNum：" + requestNum);
                // 执行定时任务
                // 定时任务是如果在会议开始的情况下改变数据了，向服务器提交数据失败后走这里，定时给服务器发送数据，直到成功，成功后会将 requestNum的值改为 1
                if (requestNum == 1) {
                    if (code == 0) {
                        timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                L.e("定时任务执行");
                                getSendData(0, -2);
                            }
                        }, 5000, 5000);//延时5s执行
                    } else {
                        requestNum = 0;
                    }
                }
            }

            // 发送数据成功
            @Override
            public void onResponse(Response response) throws IOException {
                DialogUtil.loadingDialogEnd(MainActivity.this);
                // L.e("解密数据：" + AESUtil.decryptDataToStr(aesJson, "52957c911899450f98a376a147aac50e"));
                String result = response.body().string();
                L.e("onResponse：" + result);
                if (response.body() != null) {
                    response.body().close();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.show(MainActivity.this, "发送成功");
                        if (code == -1) { // 开始会议
                            // 将会议开始时间改为现在时间
                            meetingBean.setStartTime(TimeUtil.nowStrTime());
                            // 将会议状态改为1，正在开
                            meetingBean.setMeetingStatus(1);
                            meeting_end_btn.setImageResource(R.mipmap.meeting_end_icon);
                            // 保存到本地json
                            meetingBean.save();
                            // 刷新界面
                            dragSortAdapterNotify();
                        } else if (code == 0) { // 发送全部数据
                            // 保存到本地json
                            meetingBean.save();
                            // 刷新界面
                            dragSortAdapterNotify();
                        } else if (code == 1) { // 准备按钮的返回事件
                            // 修改本行议程状态为1 准备中
                            agendaBeanList.get(position).setAgendaStatus(1);
                            // 保存到本地json
                            meetingBean.save();
                            L.e("meetingBean-- " + meetingBean);
                            // 判断议程按钮和结束按钮
                            isShowCreateAgendaOrEndMeetingBtn();
                            // 刷新界面
                            dragSortAdapterNotify();
                        } else if (code == 2) { // 叫人按钮的返回事件
                            // 将本行议程状态改为2 人进入
                            agendaBeanList.get(position).setAgendaStatus(2);
                            if (position > 0) {
                                // 如果点击的是第二行叫人按钮
                                // 将上一行的议程状态改为4 已开完
                                agendaBeanList.get(position - 1).setAgendaStatus(4);
                                // 将上一行的议程结束时间改为现在时间
                                agendaBeanList.get(position - 1).setEndTime(TimeUtil.nowStrTime());
                                // 发送上一条议程信息，让前端显示上一条议程结束
                                getSendData(position - 1, 4);
                            }
                            // 刷新界面
                            dragSortAdapterNotify();
                            // 保存到本地json
                            meetingBean.save();
                            L.e("meetingBean-- " + meetingBean);
                        } else if (code == 3) { // 走这里说明是人员到齐，议程开始
                            // 将本行议程状态改为3 正在开
                            agendaBeanList.get(position).setAgendaStatus(3);
                            // 将本议程开始时间填入
                            agendaBeanList.get(position).setStartTime(TimeUtil.nowStrTime());
                            // 刷新界面
                            dragSortAdapterNotify();
                            // 保存到本地json
                            meetingBean.save();
                            L.e("meetingBean-- " + meetingBean);
                        } else if (code == -2) { // 走这里说明是发送心跳返回
                            // 将发送次数改为0
                            requestNum = 0;
                            // 关闭定时器
                            timer.cancel();
                            // 发送全部数据
                            sendAllData();
                        } else if (code == 5) { // 走这里说明是结束会议
                            meetingBean.setEndTime(TimeUtil.nowStrTime());
                            meetingBean.setMeetingStatus(2);
                            for (int i = 0; i < meetingBean.getAgendaBeanList().size(); i++) {
                                if (agendaBeanList.get(i).getAgendaStatus() != 4) {
                                    agendaBeanList.get(i).setAgendaStatus(4);
                                }
                                if ("".equals(agendaBeanList.get(i).getEndTime()) || agendaBeanList.get(i).getEndTime() == null) {
                                    agendaBeanList.get(i).setEndTime(TimeUtil.nowStrTime());
                                }
                            }
                            meetingBean.save();
                            finish();
                        }
                    }
                });
            }
        });
    }

    /**
     * 对会议开始后，添加、删除、拖动议程和删除、新增人员时发送全部数据的封装
     */
    private void sendAllData() {
        if (meetingBean.getMeetingStatus() == 1) {
            DialogUtil.loadingDialog(MainActivity.this, "发送数据中...");
            getSendData(0, 0);
        }
    }

    /**
     * 刷新议程 调用的dragSortAdapter中的 notifyData() 方法
     */
    private void dragSortAdapterNotify() {
        dragSortAdapter.notifyData();
    }

    /**
     * 判断是否显示删除人员按钮
     * @param position 索引值
     */
    private void isDelete(int position) {
        if (agendaBeanList.get(position).getDisplayPerson()) {
            agendaBeanList.get(position).setDisplayPerson(false);
            dragSortAdapterNotify();
        }
    }

    /**
     * 程序退出时将人员删除按钮隐藏
     */
    private void signOutHideDelete() {
        if (agendaBeanList != null) {
            for (int i = 0; i < agendaBeanList.size(); i++) {
                if (agendaBeanList.get(i).getDisplayPerson()) {
                    agendaBeanList.get(i).setDisplayPerson(false);
                }
            }
            dragSortAdapterNotify();
            meetingBean.save();
        }
    }

    /**
     * 判断是否显示添加议程按钮和结束会议按钮
     */
    private void isShowCreateAgendaOrEndMeetingBtn() {
        if (meetingBean.getMeetingStatus() == 2) {
            // 会议结束了，隐藏结束会议和添加议程按钮
            meeting_end_btn.setVisibility(View.GONE);
            meeting_create_btn.setVisibility(View.GONE);
        } else if (meetingBean.getMeetingStatus() == 0) {
            // 会议未开始时，隐藏结束会议按钮，显示添加议程按钮
            // meeting_end_btn.setVisibility(View.GONE);
            meeting_create_btn.setVisibility(View.VISIBLE);
        } else {
            // 会议正在进行中，添加议程按钮和结束会议按钮都显示
            meeting_end_btn.setVisibility(View.VISIBLE);
            meeting_create_btn.setVisibility(View.VISIBLE);
        }
    }

    // 结束会议提示框
    private void showEndMeetingDialog(String text, final int opCode, final int which) {
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
                L.e("=== " + opCode);
                if (opCode == 1) { // 点击的是结束会议按钮
                    DialogUtil.loadingDialog(MainActivity.this, "发送数据中...");
                    getSendData(0, 5);
                } else if (opCode == 2) { // 点击的是删除议程按钮
                    dragSortAdapter.remove(which);
                    meetingBean.save();
                    // 发送全部数据
                    sendAllData();
                } else { // 点击的是系统返回按钮
                    // 隐藏删除人员按钮
                    signOutHideDelete();
                    finish();
                }
                alertDialog.dismiss();
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

    private void checkPermission() {
        //检查权限（NEED_PERMISSION）是否被授权 PackageManager.PERMISSION_GRANTED表示同意授权
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //用户已经拒绝过一次，再次弹出权限申请对话框需要给用户一个解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
                    .WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "请开通相关权限，否则无法正常使用本应用！", Toast.LENGTH_SHORT).show();
            }
            //申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
        } else {
            // Toast.makeText(this, "授权成功！", Toast.LENGTH_SHORT).show();
            L.e("checkPermission: 已经授权！");
        }
    }

    // 监听返回按钮键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (meetingBean.getMeetingStatus() == 1) {
                showEndMeetingDialog("会议还没有结束，确定要退出吗？", 3, 0);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 隐藏删除人员按钮
        signOutHideDelete();
    }
}
