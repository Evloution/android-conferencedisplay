package com.elink.view;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

public class DragSortItemView extends ViewGroup {

    private int mGravity = Gravity.TOP;

    public DragSortItemView(Context context) {
        super(context);

        // always init with standard ListView layout params
        setLayoutParams(new AbsListView.LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));

        //setClipChildren(true);
    }

    public void setGravity(int gravity) {
        mGravity = gravity;
    }

    public int getGravity() {
        return mGravity;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final View child = getChildAt(0);

        if (child == null) {
            return;
        }

        if (mGravity == Gravity.TOP) {
            child.layout(0, 0, getMeasuredWidth(), child.getMeasuredHeight());
        } else {
            child.layout(0, getMeasuredHeight() - child.getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight());
        }
    }

    /**
     *
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        final View child = getChildAt(0);
        if (child == null) {
            setMeasuredDimension(0, width);
            return;
        }

        if (child.isLayoutRequested()) {
            // Always let child be as tall as it wants.
            measureChild(child, widthMeasureSpec,
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        }

        if (heightMode == MeasureSpec.UNSPECIFIED) {
            LayoutParams lp = getLayoutParams();

            if (lp.height > 0) {
                height = lp.height;
            } else {
                height = child.getMeasuredHeight();
            }
        }

        setMeasuredDimension(width, height);
    }

}
