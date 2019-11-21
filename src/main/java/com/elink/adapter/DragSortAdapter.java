package com.elink.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.elink.R;
import com.elink.bean.AgendaBean;
import com.elink.listener.RecyclerItemClickListener;
import com.elink.view.LeftListView;

import java.util.List;

public class DragSortAdapter extends BaseAdapter implements View.OnClickListener {

    private Context context;
    private List<AgendaBean> list;
    private DragSortCallback dragSortCallback;
    private LayoutInflater mInflater;
    private LeftAdapter leftAdapter;

    private RecyclerViewAdapter recyclerViewAdapter;

    public interface DragSortCallback {
        void click(View view);
    }

    @Override
    public void onClick(View view) {
        dragSortCallback.click(view);
    }

    public DragSortAdapter(Context context, List<AgendaBean> list, DragSortCallback dragSortCallback) {
        this.context = context;
        this.list = list;
        this.dragSortCallback = dragSortCallback;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // 删除指定位置的item
    public void remove(int arg0) {
        list.remove(arg0);
        orderby();
        // 不要忘记更改适配器对象的数据源
    }

    // 在指定位置插入item
    public void insert(AgendaBean item, int arg0) {
        list.add(arg0, item);
        orderby();
    }

    public void orderby() {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setOrderCode(i);
        }
        notifyData();
    }

    // 刷新页面
    public void notifyData() {
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.item_dragsort_listview, null, false);
            viewHolder = new ViewHolder();
            viewHolder.titletextview = convertView.findViewById(R.id.dragsort_listview_item_textview);
            viewHolder.dragsort_listview_item_create_btn = convertView.findViewById(R.id.dragsort_listview_item_create_btn);
            viewHolder.dragsort_listview_item_prepare_btn = convertView.findViewById(R.id.dragsort_listview_item_prepare_btn);
            viewHolder.dragsort_listview_item_call_btn = convertView.findViewById(R.id.dragsort_listview_item_call_btn);
            viewHolder.dragsort_listview_item_confirm_btn = convertView.findViewById(R.id.dragsort_listview_item_confirm_btn);
            // viewHolder.dragsort_item_listview = convertView.findViewById(R.id.dragsort_item_listview);
            viewHolder.dragsort_item_recyclerview = convertView.findViewById(R.id.dragsort_item_recyclerview);
            viewHolder.dragsort_listview_item_layout = convertView.findViewById(R.id.dragsort_listview_item_layout);
            viewHolder.item_deagsort_line_linearlayout = convertView.findViewById(R.id.item_deagsort_line_linearlayout);
            viewHolder.view_line_software = convertView.findViewById(R.id.view_line_software);
            viewHolder.item_dragsort_person_txt = convertView.findViewById(R.id.item_dragsort_person_txt);
            viewHolder.dragsort_listview_agenda_status_progress_img = convertView.findViewById(R.id.dragsort_listview_agenda_status_progress_img);
            viewHolder.dragsort_listview_agenda_status_closed_img = convertView.findViewById(R.id.dragsort_listview_agenda_status_closed_img);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // 判断
        // list.get(position).getAgendaStatus() >= 0 && list.get(position).getAgendaStatus() < 4
        if (position == 0 && (list.get(position).getAgendaStatus() >= 0 && list.get(position).getAgendaStatus() < 4)) {// 第一个item
            viewHolder.dragsort_listview_item_prepare_btn.setVisibility(View.VISIBLE);
            viewHolder.dragsort_listview_item_call_btn.setVisibility(View.VISIBLE);
            viewHolder.dragsort_listview_item_confirm_btn.setVisibility(View.VISIBLE);
            if (list.get(position).getAgendaStatus() == 0) {
                viewHolder.dragsort_listview_item_prepare_btn.setVisibility(View.VISIBLE);
                viewHolder.dragsort_listview_item_call_btn.setVisibility(View.GONE);
                viewHolder.dragsort_listview_item_confirm_btn.setVisibility(View.GONE);
            }
            if (list.get(position).getAgendaStatus() == 1) {
                viewHolder.dragsort_listview_item_prepare_btn.setVisibility(View.GONE);
                viewHolder.dragsort_listview_item_call_btn.setVisibility(View.VISIBLE);
                viewHolder.dragsort_listview_item_confirm_btn.setVisibility(View.GONE);
            }
            if (list.get(position).getAgendaStatus() == 2) {
                viewHolder.dragsort_listview_item_prepare_btn.setVisibility(View.GONE);
                viewHolder.dragsort_listview_item_call_btn.setVisibility(View.GONE);
                viewHolder.dragsort_listview_item_confirm_btn.setVisibility(View.VISIBLE);
            }
            if (list.get(position).getAgendaStatus() == 3) {
                viewHolder.dragsort_listview_item_prepare_btn.setVisibility(View.GONE);
                viewHolder.dragsort_listview_item_call_btn.setVisibility(View.GONE);
                viewHolder.dragsort_listview_item_confirm_btn.setVisibility(View.GONE);

                viewHolder.dragsort_listview_agenda_status_progress_img.setVisibility(View.VISIBLE);
            }
            if (list.get(position).getAgendaStatus() == 4) {

            }
        } else if (position > 0 && position < getCount() - 1) {//中间的item
            if (list.get(position - 1).getAgendaStatus() >= 3 && (list.get(position).getAgendaStatus() >= 0 && list.get(position).getAgendaStatus() < 4) && 0 == list.get(position + 1).getAgendaStatus()) {
                viewHolder.dragsort_listview_item_prepare_btn.setVisibility(View.VISIBLE);
                viewHolder.dragsort_listview_item_call_btn.setVisibility(View.VISIBLE);
                viewHolder.dragsort_listview_item_confirm_btn.setVisibility(View.VISIBLE);
                // 解决点击到齐按钮后还显示到齐按钮的判断
                /*if (list.get(position).getAgendaStatus() == 3) {
                    viewHolder.dragsort_listview_item_prepare_btn.setVisibility(View.GONE);
                    viewHolder.dragsort_listview_item_call_btn.setVisibility(View.GONE);
                    viewHolder.dragsort_listview_item_confirm_btn.setVisibility(View.GONE);
                }*/
                if (list.get(position).getAgendaStatus() == 0) {
                    viewHolder.dragsort_listview_item_prepare_btn.setVisibility(View.VISIBLE);
                    viewHolder.dragsort_listview_item_call_btn.setVisibility(View.GONE);
                    viewHolder.dragsort_listview_item_confirm_btn.setVisibility(View.GONE);
                }
                if (list.get(position).getAgendaStatus() == 1) {
                    viewHolder.dragsort_listview_item_prepare_btn.setVisibility(View.GONE);
                    viewHolder.dragsort_listview_item_call_btn.setVisibility(View.VISIBLE);
                    viewHolder.dragsort_listview_item_confirm_btn.setVisibility(View.GONE);
                }
                if (list.get(position).getAgendaStatus() == 2) {
                    viewHolder.dragsort_listview_item_prepare_btn.setVisibility(View.GONE);
                    viewHolder.dragsort_listview_item_call_btn.setVisibility(View.GONE);
                    viewHolder.dragsort_listview_item_confirm_btn.setVisibility(View.VISIBLE);
                }
                if (list.get(position).getAgendaStatus() == 3) {
                    viewHolder.dragsort_listview_item_prepare_btn.setVisibility(View.GONE);
                    viewHolder.dragsort_listview_item_call_btn.setVisibility(View.GONE);
                    viewHolder.dragsort_listview_item_confirm_btn.setVisibility(View.GONE);
                }
            } else {
                viewHolder.dragsort_listview_item_prepare_btn.setVisibility(View.GONE);
                viewHolder.dragsort_listview_item_call_btn.setVisibility(View.GONE);
                viewHolder.dragsort_listview_item_confirm_btn.setVisibility(View.GONE);
            }
        } else if (position == getCount() - 1 && (list.get(position).getAgendaStatus() >= 0 && list.get(position).getAgendaStatus() < 4) && (3 == list.get(position - 1).getAgendaStatus() || 4 == list.get(position - 1).getAgendaStatus())) {
            viewHolder.dragsort_listview_item_prepare_btn.setVisibility(View.VISIBLE);
            viewHolder.dragsort_listview_item_call_btn.setVisibility(View.VISIBLE);
            viewHolder.dragsort_listview_item_confirm_btn.setVisibility(View.VISIBLE);
            // 解决点击到齐按钮后还显示到齐按钮的判断
            /*if (list.get(position).getAgendaStatus() == 3) {
                viewHolder.dragsort_listview_item_prepare_btn.setVisibility(View.GONE);
                viewHolder.dragsort_listview_item_call_btn.setVisibility(View.GONE);
                viewHolder.dragsort_listview_item_confirm_btn.setVisibility(View.GONE);
            }*/
            if (list.get(position).getAgendaStatus() == 0) {
                viewHolder.dragsort_listview_item_prepare_btn.setVisibility(View.VISIBLE);
                viewHolder.dragsort_listview_item_call_btn.setVisibility(View.GONE);
                viewHolder.dragsort_listview_item_confirm_btn.setVisibility(View.GONE);
            }
            if (list.get(position).getAgendaStatus() == 1) {
                viewHolder.dragsort_listview_item_prepare_btn.setVisibility(View.GONE);
                viewHolder.dragsort_listview_item_call_btn.setVisibility(View.VISIBLE);
                viewHolder.dragsort_listview_item_confirm_btn.setVisibility(View.GONE);
            }
            if (list.get(position).getAgendaStatus() == 2) {
                viewHolder.dragsort_listview_item_prepare_btn.setVisibility(View.GONE);
                viewHolder.dragsort_listview_item_call_btn.setVisibility(View.GONE);
                viewHolder.dragsort_listview_item_confirm_btn.setVisibility(View.VISIBLE);
            }
            if (list.get(position).getAgendaStatus() == 3) {
                viewHolder.dragsort_listview_item_prepare_btn.setVisibility(View.GONE);
                viewHolder.dragsort_listview_item_call_btn.setVisibility(View.GONE);
                viewHolder.dragsort_listview_item_confirm_btn.setVisibility(View.GONE);

            }
        } else {
            viewHolder.dragsort_listview_item_prepare_btn.setVisibility(View.GONE);
            viewHolder.dragsort_listview_item_call_btn.setVisibility(View.GONE);
            viewHolder.dragsort_listview_item_confirm_btn.setVisibility(View.GONE);
        }

        // 议程已进行完毕
        if (list.get(position).getAgendaStatus() > 1) {
            // 添加按钮和删除按钮隐藏
            viewHolder.dragsort_listview_item_create_btn.setVisibility(View.INVISIBLE);
            viewHolder.dragsort_listview_item_layout.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.dragsort_listview_item_create_btn.setVisibility(View.VISIBLE);
            viewHolder.dragsort_listview_item_layout.setVisibility(View.VISIBLE);
        }

        if (list.get(position).getPersonList() == null || list.get(position).getPersonList().size() == 0) {
            viewHolder.item_deagsort_line_linearlayout.setVisibility(View.GONE);
            viewHolder.view_line_software.setVisibility(View.GONE);
            viewHolder.item_dragsort_person_txt.setVisibility(View.GONE);
        } else {
            viewHolder.item_deagsort_line_linearlayout.setVisibility(View.VISIBLE);
            viewHolder.view_line_software.setVisibility(View.VISIBLE);
            viewHolder.item_dragsort_person_txt.setVisibility(View.VISIBLE);
        }

        // 判断议程的状态
        if (list.get(position).getAgendaStatus() == 3) {
            viewHolder.dragsort_listview_agenda_status_progress_img.setVisibility(View.VISIBLE);
            viewHolder.dragsort_listview_agenda_status_closed_img.setVisibility(View.GONE);
        } else if (list.get(position).getAgendaStatus() == 4) {
            viewHolder.dragsort_listview_agenda_status_progress_img.setVisibility(View.GONE);
            viewHolder.dragsort_listview_agenda_status_closed_img.setVisibility(View.VISIBLE);
        } else {
            viewHolder.dragsort_listview_agenda_status_progress_img.setVisibility(View.GONE);
            viewHolder.dragsort_listview_agenda_status_closed_img.setVisibility(View.GONE);
        }

        String name = list.get(position).getName();
        String departmentName = list.get(position).getDepartmentName();
        viewHolder.titletextview.setText(name + "  --- 科室：" + departmentName);
        // 添加按钮
        viewHolder.dragsort_listview_item_create_btn.setOnClickListener(this);
        viewHolder.dragsort_listview_item_prepare_btn.setOnClickListener(this);
        viewHolder.dragsort_listview_item_call_btn.setOnClickListener(this);
        viewHolder.dragsort_listview_item_confirm_btn.setOnClickListener(this);
        viewHolder.dragsort_listview_item_layout.setOnClickListener(this);
        viewHolder.dragsort_listview_item_layout.setTag(position);
        viewHolder.dragsort_listview_item_create_btn.setTag(position);
        viewHolder.dragsort_listview_item_prepare_btn.setTag(position);
        viewHolder.dragsort_listview_item_call_btn.setTag(position);
        viewHolder.dragsort_listview_item_confirm_btn.setTag(position);
        viewHolder.dragsort_item_recyclerview.setTag(position);

        LinearLayoutManager manager = new LinearLayoutManager(context);
        viewHolder.dragsort_item_recyclerview.setLayoutManager(manager);
        //横向
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewAdapter = new RecyclerViewAdapter(context, list.get(position).getPersonList(), (RecyclerViewAdapter.RecyclerVieCallback) context, list.get(position));
        viewHolder.dragsort_item_recyclerview.setAdapter(recyclerViewAdapter);
        //长按点击显示
        final int[] a = {1};
        viewHolder.dragsort_item_recyclerview.addOnItemTouchListener(new RecyclerItemClickListener(context, viewHolder.dragsort_item_recyclerview, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                // Constant.isDelete = true;
                if (view.getParent() == null) {
                    return;
                }
                if (view.getParent() instanceof RecyclerView) {
                    RecyclerView recyclerView = (RecyclerView) view.getParent();
                    int recyclerTag = (int) recyclerView.getTag();
                    list.get(recyclerTag).setDisplayPerson(true);
                }
                notifyDataSetChanged();
                //recyclerViewAdapter.notifyData();
            }
        }));

        /*leftAdapter = new LeftAdapter(context, list.get(position).getPersonList(), viewHolder.dragsort_item_listview.getRightViewWidth());
        viewHolder.dragsort_item_listview.setAdapter(leftAdapter);

        final int positions = position;
        leftAdapter.setOnRightItemClickListener(new onRightItemClickListener() {
            @Override
            public void onRightItemClick(View v, int position) {
                list.get(positions).getPersonList().remove(position);
                notifyDataSetChanged();
            }
        });*/

        return convertView;
    }

    public static class ViewHolder {
        private RelativeLayout dragsort_listview_item_layout;
        private TextView titletextview;
        private ImageView dragsort_listview_item_create_btn;
        private Button dragsort_listview_item_prepare_btn;
        private Button dragsort_listview_item_call_btn;
        private Button dragsort_listview_item_confirm_btn;
        private LeftListView dragsort_item_listview;
        private RecyclerView dragsort_item_recyclerview;
        private LinearLayout item_deagsort_line_linearlayout;
        private View view_line_software;
        private TextView item_dragsort_person_txt;
        private LinearLayout dragsort_listview_agenda_status_progress_img;
        private LinearLayout dragsort_listview_agenda_status_closed_img;
    }
}
