package com.elink.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.elink.R;
import com.elink.adapter.DragSortAdapter;
import com.elink.bean.AgendaBean;
import com.elink.log.L;
import com.elink.view.DragSortListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description：
 * @Author： Evloution_
 * @Date： 2019/9/25
 * @Email： 15227318030@163.com
 */

public class DragSortListViewActivity extends AppCompatActivity implements DragSortAdapter.DragSortCallback {

    private String TAG = "DragSortListViewActivity";

    private DragSortListView dragSortListView;
    private DragSortAdapter dragSortAdapter;
    private List<AgendaBean> list;

    // 监听器在手机拖动停下的时候触发
    private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
        @Override
        public void drop(int from, int to) {// from to 分别表示 被拖动控件原位置 和目标位置
            if (from != to) {
                AgendaBean item = (AgendaBean) dragSortAdapter.getItem(from);// 得到listview的适配器
                dragSortAdapter.remove(from);// 在适配器中”原位置“的数据。
                dragSortAdapter.insert(item, to);// 在目标位置中插入被拖动的控件。
                L.e("------- 移动位置" + item.getName());
            }
        }
    };
    // 删除监听器，点击左边差号就触发。删除item操作。
    private DragSortListView.RemoveListener onRemove = new DragSortListView.RemoveListener() {
        @Override
        public void remove(int which) {
            L.e("------- 删除");
            dragSortAdapter.remove(which);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dragsortlistview);
        L.TAG = TAG;

        initView();
        initData();
        initEvent();
    }

    private void initView() {
        dragSortListView = findViewById(R.id.activity_dragsortlistview_listview);
        dragSortListView.setDropListener(onDrop);
        dragSortListView.setRemoveListener(onRemove);
        list = new ArrayList<AgendaBean>();
    }

    private void initData() {
        /*AgendaBean myJaveBean1 = new AgendaBean();
        myJaveBean1.setName("张三");

        AgendaBean myJaveBean2 = new AgendaBean() ;
        myJaveBean2.setName("李四");

        AgendaBean myJaveBean3 = new AgendaBean();
        myJaveBean3.setName("王五");

        AgendaBean myJaveBean4 = new AgendaBean();
        myJaveBean4.setName("韦德");

        AgendaBean myJaveBean5 = new AgendaBean();
        myJaveBean5.setName("詹姆斯");

        AgendaBean myJaveBean6 = new AgendaBean();
        myJaveBean6.setName("蒿俊闵");

        for (int i = 0; i < 10; i++) {
            list.add(myJaveBean1);
            list.add(myJaveBean2);
            list.add(myJaveBean3);
            list.add(myJaveBean4);
            list.add(myJaveBean5);
            list.add(myJaveBean6);
        }*/
    }

    private void initEvent() {
        dragSortAdapter = new DragSortAdapter(DragSortListViewActivity.this, list,this);
        dragSortListView.setAdapter(dragSortAdapter);
        dragSortListView.setDragEnabled(true); // 设置是否可拖动。
    }

    @Override
    public void click(View view) {

    }
}