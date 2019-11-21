package com.elink.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.elink.R;
import com.elink.bean.AgendaBean;
import com.elink.bean.PersonBean;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements View.OnClickListener {
    private List<PersonBean> list;
    private AgendaBean agendaBean;
    private Context context;
    private RecyclerVieCallback recyclerVieCallback;

    @Override
    public void onClick(View view) {
        recyclerVieCallback.recyclerVieClick(view, this.agendaBean);
    }

    public interface RecyclerVieCallback {
        void recyclerVieClick(View view, AgendaBean agendaBean);
    }

    public RecyclerViewAdapter(Context context, List<PersonBean> list, RecyclerVieCallback recyclerVieCallback, AgendaBean agendaBean) {
        this.context = context;
        this.list = list;
        this.agendaBean = agendaBean;
        this.recyclerVieCallback = recyclerVieCallback;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView item_recyclerview_name_txt;
        private ImageButton item_recyclerview_delete_imgbtn;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycleview, null);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.item_recyclerview_name_txt = (TextView) view.findViewById(R.id.item_recyclerview_name_txt);
        viewHolder.item_recyclerview_delete_imgbtn = (ImageButton) view.findViewById(R.id.item_recyclerview_delete_imgbtn);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.item_recyclerview_name_txt.setText(list.get(position).getPersonName());
        if (agendaBean.getAgendaStatus() < 2) {
            holder.item_recyclerview_delete_imgbtn.setVisibility(agendaBean.getDisplayPerson() ? View.VISIBLE : View.GONE);
        } else {
            agendaBean.setDisplayPerson(false);
            holder.item_recyclerview_delete_imgbtn.setVisibility(View.GONE);
        }
        holder.item_recyclerview_delete_imgbtn.setOnClickListener(this);
        holder.item_recyclerview_delete_imgbtn.setTag(position);
        /*holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("item", view.toString());
                list.remove(position);
                notifyDataSetChanged();
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
