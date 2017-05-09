package com.wc.recyclerview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

/**
 * 头部联动ViewPager
 * Created by RushKing on 2017/5/4.
 */

public class HeadViewPager extends ViewPager {
    private final String TAG = "HeadViewPager";
    private View mHeadView;
    private int mFixedViewHeight = 0;

    private float scrollY, scrollX;
    private boolean isDispatchToHeadView;//事件需要分发给HeadView
    private boolean isFixedViewRegion;//在FixedView区域内
    private boolean isDispatched;//已经分发
    private int mTouchSlop;

    public HeadViewPager(Context context) {
        super(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public HeadViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    protected void onPageScrolled(int position, float offset, int offsetPixels) {
        super.onPageScrolled(position, offset, offsetPixels);
        setHeadRecyclerView(getHeadRecyclerView(getChildAt(position)));
        if (position + 1 < getChildCount()) {
            setHeadRecyclerView(getHeadRecyclerView(getChildAt(position + 1)));
        }
    }

    private void setHeadRecyclerView(HeadRecyclerView headRecyclerView) {
        if (headRecyclerView == null) {
            return;
        }
        headRecyclerView.setFullScreenFooter();
        int headScrollY = headRecyclerView.getHeadScrollY();
        int scrolledY = headRecyclerView.getScrolledY();
        if (scrolledY < headScrollY) {
//            Log.v(TAG, headRecyclerView.getTag() + "\theadScrollY=" + headScrollY + "\tscrolledY=" + scrolledY);
            headRecyclerView.scrollBy(0, headScrollY - scrolledY);
        } else if (scrolledY > headScrollY) {
            int slideViewHeight = headRecyclerView.getSlideViewHeight();
            if (scrolledY > slideViewHeight) {
                if (!headRecyclerView.isTop()) {
                    headRecyclerView.scrollBy(0, slideViewHeight - scrolledY);
                }
//                Log.v(TAG, "headScrollY=" + headScrollY + "\tscrolledY=" + scrolledY + "\tslideViewHeight" + slideViewHeight);
            } else {
                headRecyclerView.scrollBy(0, headScrollY - scrolledY);
//                Log.v(TAG, "headScrollY=" + headScrollY + "\tscrolledY=" + scrolledY + "\tslideViewHeight" + slideViewHeight);
            }
        }
    }

    private HeadRecyclerView getHeadRecyclerView(View v) {
        if (v instanceof HeadRecyclerView) {
//            Log.v(TAG, "v instanceof HeadRecyclerView");
            return (HeadRecyclerView) v;
        } else if (v instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) v;
            for (int i = 0; i < group.getChildCount(); i++) {
                HeadRecyclerView headRecyclerView = getHeadRecyclerView(group.getChildAt(i));
                if (headRecyclerView != null)
                    return headRecyclerView;
            }
        }
        return null;
    }

    /**
     * 设置真正的HeadView
     */
    public void setHeadView(View v) {
        mHeadView = v;
        //把HeadView重置到最上层布局
        //mHeadView.bringToFront();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isDispatchToHeadView = false;
                isFixedViewRegion = false;
                isDispatched = false;
                if (mHeadView != null) {
                    scrollY = ev.getY();
                    if (mFixedViewHeight == 0) {
                        if (mHeadView instanceof HeadLayout) {
                            HeadLayout head = (HeadLayout) mHeadView;
                            if (head.getFixedView() != null) {
                                bringChildToFront(head.getFixedView());
                                mFixedViewHeight = head.getFixedView().getMeasuredHeight();
                            }
                        } else if (mHeadView instanceof ViewGroup) {
                            ViewGroup group = (ViewGroup) mHeadView;
                            if (group.getChildCount() > 1) {
                                mFixedViewHeight = group.getChildAt(1).getMeasuredHeight();
                            }
                        }
                    }
                    int bottom = mHeadView.getBottom();
                    if (scrollY <= bottom && scrollY > bottom - mFixedViewHeight) {
                        isFixedViewRegion = true;
                        scrollX = ev.getX();
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isFixedViewRegion && !isDispatched && !isDispatchToHeadView) {
                    float y = ev.getY();
                    if (Math.abs(scrollY - y) > mTouchSlop) {
                        isDispatchToHeadView = false;
                        isDispatched = true;
                        break;
                    }
                    float x = ev.getX();
                    if (Math.abs(scrollX - x) > mTouchSlop) {
                        isDispatchToHeadView = true;
                        isDispatched = true;
                    }
                }
//                if (!isDispatchToHeadView) {
//                    int x = (int) ev.getX();
//                    Log.v(TAG, "x=" + x + "\tscrollX=" + scrollX);
//                    if (Math.abs(scrollX - x) < 0) {
//                        isDispatchToHeadView = true;
//                    }
                break;
        }
        if (isDispatchToHeadView) {
            return mHeadView.dispatchTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }
}
