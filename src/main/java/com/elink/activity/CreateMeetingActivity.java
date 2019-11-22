package com.elink.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.elink.R;
import com.elink.log.L;
import com.elink.permission.PermissionHelper;
import com.elink.permission.PermissionInterface;
import com.elink.permission.PermissionUtil;
import com.elink.utils.FileUtil;
import com.elink.utils.ToastUtil;

/**
 * @Description： 首页
 * @Author： Evloution_
 * @Date： 2019/9/26
 * @Email： 15227318030@163.com
 */
public class CreateMeetingActivity extends AppCompatActivity implements View.OnClickListener, PermissionInterface {

    private Button createmetting_meeting_name_btn;
    private Button createmetting_metting_record_btn;

    private PermissionHelper permissionHelper;
    private String[] mPermissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createmeeting);

        initView();
        initData();
        initEvent();
    }

    private void initView() {
        createmetting_meeting_name_btn = findViewById(R.id.createmetting_meeting_name_btn);
        createmetting_metting_record_btn = findViewById(R.id.createmetting_metting_record_btn);

        permissionHelper = new PermissionHelper(CreateMeetingActivity.this, (PermissionInterface) CreateMeetingActivity.this);
        permissionHelper.requestPermissions();
    }

    private void initData() {
        createmetting_meeting_name_btn.setOnClickListener(this);
        createmetting_metting_record_btn.setOnClickListener(this);
    }

    private void initEvent() {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.createmetting_meeting_name_btn:
                final AlertDialog.Builder createAlertDialog = new AlertDialog.Builder(this, R.style.MyDialogStyle);
                View createView = View.inflate(this, R.layout.activity_alter_dialog_textview, null);
                final EditText et_alter_dialog_create_textview_name = createView.findViewById(R.id.et_alter_dialog_textview_name);
                et_alter_dialog_create_textview_name.setHint("请输入本次会议名称");
                Button tv_alter_dialog_create_textview_ok = createView.findViewById(R.id.tv_alter_dialog_textview_ok);
                Button tv_alter_dialog_create_textview_cancel = createView.findViewById(R.id.tv_alter_dialog_textview_cancel);
                createAlertDialog
                        .setTitle("会议名称")
                        .setView(createView)
                        .create();
                final AlertDialog createshow = createAlertDialog.show();
                tv_alter_dialog_create_textview_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = et_alter_dialog_create_textview_name.getText().toString().trim();
                        L.e("name===>" + name.length());
                        if (name.length() != 0) {
                            if (FileUtil.fileIsExists("/sdcard/ConferenceRecords/" + name + ".txt")) {
                                ToastUtil.show(CreateMeetingActivity.this, "该会议已存在");
                                return;
                            }
                            Intent mainActivityIntent = new Intent(CreateMeetingActivity.this, MainActivity.class);
                            mainActivityIntent.putExtra("meeting_name", name);
                            startActivity(mainActivityIntent);
                            createshow.dismiss();
                        } else {
                            ToastUtil.show(CreateMeetingActivity.this, "会议不能为空");
                            return;
                        }
                    }
                });
                tv_alter_dialog_create_textview_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        createshow.dismiss();
                    }
                });
                break;
            case R.id.createmetting_metting_record_btn:
                Intent intent_activity_main_text = new Intent(CreateMeetingActivity.this, ConferenceRecordActivity.class);
                startActivity(intent_activity_main_text);
                break;
        }
    }

    @Override
    public int getPermissionsRequestCode() {
        return 0;
    }

    @Override
    public String[] getPermissions() {
        return mPermissions;
    }

    @Override
    public void requestPermissionsSuccess() {
        Log.i("1", "权限获取成功");

    }

    @Override
    public void requestPermissionsFail() {
        ToastUtil.show(this, "权限获取失败");
        mPermissions = PermissionUtil.getDeniedPermissions(this, mPermissions);
        PermissionUtil.PermissionDialog(this, PermissionUtil.permissionText(mPermissions) + "请在应用权限管理进行设置！");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissionHelper.requestPermissionsResult(requestCode, mPermissions, grantResults)) {
            //权限请求结果，并已经处理了该回调
            return;
        }
    }
}
