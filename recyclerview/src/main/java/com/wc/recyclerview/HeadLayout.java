package com.wc.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * 头部View布局
 * Created by RushKing on 2017/5/2.
 */

public class HeadLayout extends ViewGroup {
    private final String TAG = "HeadLayout";
//    private View mBottomView;

    public HeadLayout(Context context) {
        super(context);
    }

    public HeadLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeadLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void addView(View child) {
        if (getChildCount() > 1) {
            throw new IllegalStateException("HeadLayout can host only two direct child");
        }

        super.addView(child);
    }

    @Override
    public void addView(View child, int index) {
        if (getChildCount() > 1) {
            throw new IllegalStateException("HeadLayout can host only two direct child");
        }
        super.addView(child, index);
    }

    @Override
    public void addView(View child, LayoutParams params) {
        if (getChildCount() > 1) {
            throw new IllegalStateException("HeadLayout can host only two direct child");
        }
        super.addView(child, params);
    }

    @Override
    public void addView(View child, int index, LayoutParams params) {
        if (getChildCount() > 1) {
            throw new IllegalStateException("HeadLayout can host only two direct child");
        }
        super.addView(child, index, params);
    }

    //获取跟着滑动的View
    public View getSlideView() {
        if (getChildCount() > 0) {
            return getChildAt(0);
        }
        return null;
    }

    //获取固定的View
    public View getFixedView() {
        if (getChildCount() > 1) {
            return getChildAt(1);
        }
        return null;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childHeight = 0;
        int count = getChildCount();
        if (count <= 2) {
            for (int i = 0; i < count; i++) {
                final View child = getChildAt(i);
                //setChildTouchListener(child);
                int height = child.getLayoutParams().height;
                int childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, 0, height);
                child.measure(widthMeasureSpec, childHeightMeasureSpec);
                if (height == LayoutParams.WRAP_CONTENT) {
                    height = child.getMeasuredHeight();
                }
                childHeight += height;
            }
        }
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode != MeasureSpec.EXACTLY) {
            if (getLayoutParams().height == LayoutParams.WRAP_CONTENT) {
                setMeasuredDimension(getMeasuredWidth(), childHeight);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        if (count <= 2) {
            int top = 0;
            for (int i = 0; i < count; i++) {
                final View child = getChildAt(i);
                int childHeight = child.getMeasuredHeight();
                child.layout(0, top, child.getMeasuredWidth(), top + childHeight);
                top += childHeight;
            }
        }
    }

    //    private int downY;
//
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                downY = (int) ev.getY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                int dy = downY - (int) ev.getY();
//                if (Math.abs(dy) > 0) {
//                    mBottomView.dispatchTouchEvent(ev);
//                }
//                break;
//            case MotionEvent.ACTION_CANCEL:
//            case MotionEvent.ACTION_UP:
//                downY = 0;
//                break;
//        }
//        return super.dispatchTouchEvent(ev);
//    }

    //    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                downY = (int) ev.getY();
//                isIntercept = false;
//                break;
//            case MotionEvent.ACTION_MOVE:
//                int dy = downY - (int) ev.getY();
//                if (Math.abs(dy) > 0) {
//                    isIntercept = mBottomView.dispatchTouchEvent(ev);
//                }
//                break;
//            case MotionEvent.ACTION_CANCEL:
//            case MotionEvent.ACTION_UP:
//                downY = 0;
//                isIntercept = false;
//                break;
//        }
//        return isIntercept;
//    }

//    public void setBottomView(View v) {
//        mBottomView = v;
//    }

//    private void setChildTouchListener(final View child) {
//        if (child instanceof ViewGroup) {
//            ViewGroup group = (ViewGroup) child;
//            for (int i = 0; i < group.getChildCount(); i++) {
//                setChildTouchListener(group.getChildAt(i));
//            }
//        } else {
//            child.setOnTouchListener(new OnTouchListener() {
//                private float downY = 0;
//
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    switch (event.getAction()) {
//                        case MotionEvent.ACTION_DOWN:
//                            downY = getY();
//                            break;
//                        case MotionEvent.ACTION_MOVE:
//                            float y = getY();
//                            float dy = downY - y;
//                            if (Math.abs(dy) > 0) {
//                                child.dispatchTouchEvent(event);
//                            }
//                            downY = y;
//                            break;
//                        case MotionEvent.ACTION_UP:
//                        case MotionEvent.ACTION_CANCEL:
//                            downY = 0;
//                            break;
//                    }
//                    return false;
//                }
//            });
//    }
//    }

}
