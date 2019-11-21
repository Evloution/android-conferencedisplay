package com.elink.listener;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.elink.log.L;


public  class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener{
    private GestureDetector mGestureDetector;
    private OnItemClickListener mListener;

    //内部接口，定义点击方法以及长按方法
    public interface OnItemClickListener {
        void onItemLongClick(View view, int position);
    }

    public RecyclerItemClickListener(Context context, final RecyclerView recyclerView,OnItemClickListener listener){
        mListener = listener;
        mGestureDetector = new GestureDetector(context,
                new GestureDetector.SimpleOnGestureListener(){
                    //长按事件
                    @Override
                    public void onLongPress(MotionEvent e) {
                        L.e("长按走这里");
                        View childView = recyclerView.findChildViewUnder(e.getX(),e.getY());
                        if(childView != null && mListener != null){
                            mListener.onItemLongClick(childView,recyclerView.getChildLayoutPosition(childView));
                        }
                    }
                });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        //把事件交给GestureDetector处理
        if(mGestureDetector.onTouchEvent(e)){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }

}
