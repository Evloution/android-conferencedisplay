package com.elink.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.elink.R;
import com.elink.adapter.LeftAdapter;
import com.elink.bean.PersonBean;
import com.elink.bean.PersonBean;
import com.elink.myinterface.onRightItemClickListener;
import com.elink.view.LeftListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description：
 * @Author： Evloution_
 * @Date： 2019/9/25
 * @Email： 15227318030@163.com
 */

public class LeftListViewActivity extends AppCompatActivity {

    private LeftListView leftListView;
    private LeftAdapter leftAdapter;
    private List<PersonBean> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leftlistview);

        initView();
        initData();
        initEvent();
    }

    private void initView() {
        leftListView = findViewById(R.id.activity_leftlistview_listview);
        list = new ArrayList<PersonBean>();
        leftAdapter = new LeftAdapter(LeftListViewActivity.this, list, leftListView.getRightViewWidth());
        leftListView.setAdapter(leftAdapter);
    }

    private void initData() {
        PersonBean PersonBean1 = new PersonBean();
        PersonBean1.setPersonName("张三");

        PersonBean PersonBean2 = new PersonBean();
        PersonBean2.setPersonName("李四");

        PersonBean PersonBean3 = new PersonBean();
        PersonBean3.setPersonName("王五");

        PersonBean PersonBean4 = new PersonBean();
        PersonBean4.setPersonName("韦德");

        PersonBean PersonBean5 = new PersonBean();
        PersonBean5.setPersonName("詹姆斯");

        PersonBean PersonBean6 = new PersonBean();
        PersonBean6.setPersonName("蒿俊闵");

        for (int i = 0; i < 10; i++) {
            list.add(PersonBean1);
            list.add(PersonBean2);
            list.add(PersonBean3);
            list.add(PersonBean4);
            list.add(PersonBean5);
            list.add(PersonBean6);
        }
    }

    private void initEvent() {
        //左划删除
        leftAdapter.setOnRightItemClickListener(new onRightItemClickListener() {

            @Override
            public void onRightItemClick(View v, int position) {
                String result = list.get(position).getPersonName() + "\n" + position;
                Toast.makeText(LeftListViewActivity.this, result, Toast.LENGTH_LONG).show();
            }
        });
    }
}
