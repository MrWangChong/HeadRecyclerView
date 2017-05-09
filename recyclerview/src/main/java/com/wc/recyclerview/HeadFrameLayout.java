package com.wc.recyclerview;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 头部联动FrameLayout，使用一般布局ViewCompat.offsetTopAndBottom之后锁屏开屏后View位置会被还原
 * Created by RushKing on 2017/5/4.
 */

public class HeadFrameLayout extends FrameLayout {

    public HeadFrameLayout(@NonNull Context context) {
        super(context);
    }

    public HeadFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HeadFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void requestLayout() {
        super.requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                final int width = child.getMeasuredWidth();
                final int height = child.getMeasuredHeight();
                int childTop = getPaddingTop() + lp.topMargin;
                //加上这句话就能解决ViewCompat.offsetTopAndBottom之后锁屏开屏后View位置被还原的问题
                if (child.getTop() != childTop) {
                    childTop = child.getTop();
                }
                int childLeft = getPaddingLeft() + lp.leftMargin;
                child.layout(childLeft, childTop, childLeft + width, childTop + height);
            }
        }
    }
}
