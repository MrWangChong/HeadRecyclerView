package com.wc.recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Head联动View
 * Created by RushKing on 2017/4/28.
 */

public class HeadRecyclerView extends RecyclerView {
    private final String TAG = "HeadRecyclerView";
    //上拉加载更多
    private OnLoadMoreListener mOnLoadMoreListener;
    private boolean isLaodMore;

    //上滑隐藏头部相关
    private int mScrollY;
    private int mTopViewHeight;

    private int mSlideViewHeight;
    private int mFixedViewHeight;
    private int mHeadViewHeight;
    private View mTopView;
    private View mFooterView;
    private View mHeadView;

    //布局管理器
    private LinearLayoutManager mLayout;

    public HeadRecyclerView(Context context) {
        super(context);
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    public HeadRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    public HeadRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    //获取TopView
    private View getTopView() {
        if (mTopView == null) {
            getThisLayoutManager();
            if (mLayout != null && mLayout.getChildCount() > 0) {
                mTopView = getChildAt(0);
                //把TopView的事件分发给mHeadView
                mTopView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (mHeadView != null) {
//                            MotionEvent ev = MotionEvent.obtain(event);
//                            ev.setLocation(event.getX(), event.getY() + getPaddingTop());
                            mHeadView.dispatchTouchEvent(event);
                            return true;
                        }
                        return false;
                    }
                });
//            if (getChildCount() > 0) {
//                mTopView = getChildAt(0);
//                //把TopView的事件分发给mHeadView
//                mTopView.setOnTouchListener(new OnTouchListener() {
//                    @Override
//                    public boolean onTouch(View v, MotionEvent event) {
//                        if (mHeadView != null) {
////                            Log.v(TAG, "e.getY()=" + event.getY() + "\tmHeadView.getBottom()=" + mHeadView.getBottom());
////                            MotionEvent ev = MotionEvent.obtain(event);
////                            ev.setLocation(event.getX(), event.getY() + getPaddingTop());
//                            mHeadView.dispatchTouchEvent(event);
//                            return true;
//                        }
//                        return false;
//                    }
//                });
//            }
            }
        }
        return mTopView;
    }

    //获取Head信息
    private void getHeadInfo() {
        if (mHeadView == null) {
            return;
        }
        if (mHeadViewHeight == 0) {
            mHeadViewHeight = mHeadView.getMeasuredHeight();
//            Log.v(TAG, "mHeadViewHeight=" + mHeadViewHeight);
        }
        if (mSlideViewHeight == 0 || mFixedViewHeight == 0) {
            if (mHeadView instanceof HeadLayout) {
                HeadLayout head = (HeadLayout) mHeadView;
                if (head.getSlideView() != null) {
                    mSlideViewHeight = head.getSlideView().getMeasuredHeight();
                }
                if (head.getFixedView() != null) {
//                    bringChildToFront(head.getFixedView());
                    mFixedViewHeight = head.getFixedView().getMeasuredHeight();
                    //强行把PaddingTop改成FixedViewHeight
                    setPadding(getPaddingLeft(), mFixedViewHeight, getPaddingRight(), getPaddingBottom());
//                    Log.v(TAG, "setPaddingTop=" + mFixedViewHeight);
                }
//                Log.v(TAG, "mSlideViewHeight=" + mSlideViewHeight + "\tmFixedViewHeight=" + mFixedViewHeight + "\tmHeadViewHeight=" + mHeadViewHeight);
            } else if (mHeadView instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) mHeadView;
                if (group.getChildCount() > 0) {
                    mSlideViewHeight = group.getChildAt(0).getMeasuredHeight();
                }
                if (group.getChildCount() > 1) {
                    mFixedViewHeight = group.getChildAt(1).getMeasuredHeight();
                    //强行把PaddingTop改成FixedViewHeight
                    setPadding(getPaddingLeft(), mFixedViewHeight, getPaddingRight(), getPaddingBottom());
//                    Log.v(TAG, "setPaddingTop=" + mFixedViewHeight);
                }
//                Log.v(TAG, "mSlideViewHeight=" + mSlideViewHeight + "\tmFixedViewHeight=" + mFixedViewHeight + "\tmHeadViewHeight=" + mHeadViewHeight);
            } else {
                mSlideViewHeight = mHeadView.getMeasuredHeight();
            }
        }
    }

    //获取布局管理器
    private void getThisLayoutManager() {
        if (mLayout == null) {
            if (getLayoutManager() instanceof LinearLayoutManager) {
                mLayout = (LinearLayoutManager) getLayoutManager();
            }
        }
    }

    //当TopView滑不见之后的事件分发
    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        //点击getPaddingTop内的区域
        if (e.getY() <= getPaddingTop()) {
            //如果滑动了的Y距离大于  mTopViewHeight - mFixedViewHeight，也就是mSlideViewHeight
//            if (mHeadView != null && mScrollY > mTopViewHeight - mFixedViewHeight) {
            if (mHeadView != null && mScrollY > mSlideViewHeight) {
                MotionEvent ev = MotionEvent.obtain(e);
                ev.setLocation(e.getX(), e.getY() + mSlideViewHeight);
//                Log.v(TAG, "ev.getY()=" + ev.getY());
                mHeadView.dispatchTouchEvent(ev);
            }
        }
        return super.dispatchTouchEvent(e);
    }

    /**
     * 动态设置满屏FooterView
     */
    public void setFullScreenFooter() {
        if (mFooterView == null) {
            mFooterView = new View(getContext());
        }
        if (mFooterView.getMeasuredHeight() != 0) {
            return;
        }
        getThisLayoutManager();
        if (mLayout != null && getAdapter() != null) {
            int spanCount = 1;
            if (mLayout instanceof GridLayoutManager) {
                spanCount = ((GridLayoutManager) mLayout).getSpanCount();
            }

            int itemCount = getAdapter().getItemCount();
            int centreHeight = 0;
            int count = mLayout.getChildCount();//这里是获取的当前显示的ChildCount
            //计算所有item的高度
            int childHeight = 0;
            for (int i = 0; i < count; i++) {
                if (i == 0) {
                    childHeight = mLayout.getChildAt(i).getMeasuredHeight();
                } else {
                    if ((i - 1) % spanCount == 0) {
                        int itemHeight = mLayout.getChildAt(i).getMeasuredHeight();
                        childHeight += itemHeight;
                    }
                }
                if (i == count / 2) {
                    centreHeight = mLayout.getChildAt(i).getMeasuredHeight();
                }
            }
            int height = getMeasuredHeight();
            // Log.v(TAG, getTag() + "\tchildHeight=" + childHeight + "\theight=" + height + "\tcentreHeight=" + centreHeight + "\tmHeadViewHeight=" + mHeadViewHeight);
            int difference = height - childHeight;
            if (difference > 0) {//不满屏幕
                int footerHeight = height + mHeadViewHeight - childHeight - mFixedViewHeight + 5;
//                Log.v(TAG, getTag() + "\tfooterHeight=" + footerHeight);
                setFooterViewHeight(footerHeight);
                //这句代码是为了防止 直接点击后面3个以上的tab的时候 scrollBy执行太快而没有绘制过来的问题
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scrollBy(0, getHeadScrollY() - mScrollY);
                    }
                }, 10);
            } else {
                int invisibleItem = itemCount - count - 1;//没有显示出来的item,再减去一个footer
                if (invisibleItem > 0) {
                    int invisibleHeight = invisibleItem * centreHeight / spanCount;
                    childHeight += invisibleHeight;
                    difference = height + mHeadViewHeight - childHeight;
                    if (difference > 0) {
                        int footerHeight = difference - mFixedViewHeight + 5;
//                        Log.v(TAG, getTag() + "\tfooterHeight=" + footerHeight);
                        setFooterViewHeight(footerHeight);
                    }
                }
            }
        }
//        if (mLayout != null && getAdapter() != null) {
//            int itemCount = getAdapter().getItemCount();
//            int firstPosition = mLayout.findFirstVisibleItemPosition();
//            int lastPosition = mLayout.findLastVisibleItemPosition();
//            //Log.v(TAG, "firstPosition=" + firstPosition + "\tlastPosition=" + lastPosition + "\titemCount=" + itemCount);
//            return !(firstPosition == 0 && lastPosition == itemCount - 1);
//        }
    }

    @Override
    public void scrollBy(final int x, final int y) {
        super.scrollBy(x, y);
//        Log.v(TAG, getTag() + "\tscrollBy--->y=" + y);
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
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        getHeadInfo();
    }

    /**
     * 设置FooterView高度
     */
    public void setFooterViewHeight(int height) {
        if (height == 0) return;

        if (mFooterView == null) {
            mFooterView = new View(getContext());
            mFooterView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
        } else {
            ViewGroup.LayoutParams lp = mFooterView.getLayoutParams();
            if (lp == null) {
                lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
                mFooterView.setLayoutParams(lp);
            } else {
                if (lp.height != height) {
                    lp.height = height;
                    mFooterView.setLayoutParams(lp);
                }
            }
        }
    }

    /**
     * 重置刷新
     * <p>
     * 为了在一段时间内只允许触发一次onLoadMore事件，下次需要上拉加载调用该方法
     */
    public void resetLoadMore() {
        isLaodMore = false;
    }

    /**
     * 设置上拉加载更多回调监听
     * <p>
     * 由于是在onScrolled中写的，如果回调触发立马执行notifyDataSetChanged会导致IllegalStateException异常:
     * Cannot call this method in a scroll callback. Scroll callbacks mightbe run during a measure & layout pass where you cannot change theRecyclerView data.
     * Any method call that might change the structureof the RecyclerView or the adapter contents should be postponed tothe next frame.
     * <p>
     * 如果需要立马调用请延迟50ms
     *
     * @param l OnLoadMoreListener
     */
    public void setOnLoadMoreListener(OnLoadMoreListener l) {
        mOnLoadMoreListener = l;
        isLaodMore = false;
    }

    public int getHeadScrollY() {
        if (mHeadView != null) {
            return mHeadViewHeight - mHeadView.getBottom();
        }
        return 0;
    }

    public int getScrolledY() {
        return mScrollY;
    }

    public int getSlideViewHeight() {
        return mSlideViewHeight;
    }

    public boolean isTop() {
        return mHeadView != null && mHeadView.getBottom() == mFixedViewHeight;

    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
        mScrollY += dy;
        //顺便加上了一个加载更多的监听
        if (mOnLoadMoreListener != null && !isLaodMore) {
            getThisLayoutManager();
            if (mLayout != null) {
                if (dy > 0 && getAdapter() != null) {
//                    Log.d(TAG, "mLayout.findLastVisibleItemPosition()=" + mLayout.findLastVisibleItemPosition() + "getAdapter().getItemCount()=" + getAdapter().getItemCount());
                    if (getAdapter() instanceof SimpleAdapter) {
                        if (mLayout.findLastVisibleItemPosition() == getAdapter().getItemCount() - 2) {
                            Log.d(TAG, "HeadRecyclerView trigger onLoadMore");
                            mOnLoadMoreListener.onLoadMore(this);
                            isLaodMore = true;
                        }
                    } else {
                        if (mLayout.findLastVisibleItemPosition() == getAdapter().getItemCount() - 1) {
                            Log.d(TAG, "HeadRecyclerView trigger onLoadMore");
                            mOnLoadMoreListener.onLoadMore(this);
                            isLaodMore = true;
                        }
                    }
                }
            }
        }

        //设置头部View
        if (mTopView == null) {
            getTopView();
        }
        if (mTopView == null || mHeadView == null) {
            return;
        }
        if (mTopViewHeight == 0) {
            mTopViewHeight = mTopView.getMeasuredHeight();
        }
        getHeadInfo();
        int remainY = mHeadViewHeight - mScrollY;//剩余Y
        int headBottom = mHeadView.getBottom();//HeadView底部
//        Log.v(TAG, "mScrollY=" + mScrollY + "\tremainY=" + remainY + "\theadBottom=" + headBottom);
        if (remainY > mFixedViewHeight) {
            int offset = remainY - headBottom;
            ViewCompat.offsetTopAndBottom(mHeadView, offset);
            //滑动了HeadView需要通知
//            if (mOnHeadViewChangeListener != null) {
//                mOnHeadViewChangeListener.offsetTopAndBottom(this, offset);
//            }
//            Log.v(TAG, "mScrollY=" + mScrollY + "\tremainY=" + remainY + "\theadBottom=" + headBottom + "\toffset=" + offset);
        } else {
            if (remainY != mFixedViewHeight) {
                int offset = mFixedViewHeight - headBottom;
                ViewCompat.offsetTopAndBottom(mHeadView, offset);
                //滑动了HeadView需要通知
//                if (mOnHeadViewChangeListener != null) {
//                    mOnHeadViewChangeListener.offsetTopAndBottom(this, offset);
//                }
            }
        }
        //--------------------------原来的逻辑，暂时保留着参考一下---------------------------//
        //滚动View
//        int bottomScroll = scrollView.getBottom();
//        int bottom = scrollViewHeight - scrollY;
//        if (bottom >= 0) {
//            int offset = -(bottomScroll - (bottom));
//            ViewCompat.offsetTopAndBottom(scrollView, offset);
//        } else {
//            if (bottomScroll != 0) {
//                ViewCompat.offsetTopAndBottom(scrollView, -bottomScroll);
//            }
//        }

        //固定View
//        bottom = topViewHeight - scrollY;
//                Log.v("MainActivity", "bottom=" + bottom + "\theight=" + height + "\tscrollY=" + scrollY);
//        if (bottom >= fixedViewHeight) {
//            int bottomFixed = fixedView.getBottom();
//            int offset = -(bottomFixed - bottom);
//            ViewCompat.offsetTopAndBottom(fixedView, offset);
//        } else {
//            int topFixed = fixedView.getTop();
//            if (topFixed != 0) {
//                ViewCompat.offsetTopAndBottom(fixedView, -topFixed);
//            }
//        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(new SimpleAdapter(adapter));
//        super.setAdapter(adapter);
    }

    public interface OnLoadMoreListener {
        void onLoadMore(HeadRecyclerView view);
    }

    private class SimpleAdapter extends Adapter<ViewHolder> {
        private Adapter adapter;

        SimpleAdapter(Adapter adapter) {
            this.adapter = adapter;
        }

        @Override
        public void onViewDetachedFromWindow(ViewHolder holder) {
            super.onViewDetachedFromWindow(holder);
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            LayoutManager manager = recyclerView.getLayoutManager();
            if (manager instanceof GridLayoutManager) {
                final GridLayoutManager gridManager = (GridLayoutManager) manager;
                gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    public int getSpanSize(int position) {
                        return position != 0 && position <= adapter.getItemCount() ? 1 : gridManager.getSpanCount();
                    }
                });
            }

        }
        //因为需要使用LinearLayoutManager,所以暂不支持StaggeredGridLayoutManager
//        @Override
//        public void onViewAttachedToWindow(ViewHolder holder) {
//            super.onViewAttachedToWindow(holder);
//            int position = holder.getLayoutPosition();
//            if (position == 0 || position > adapter.getItemCount()) {
//                ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
//                if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
//                    StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
//                    p.setFullSpan(true);
//                }
//            }
//        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case 0x12345://HeadView
                    getHeadInfo();
                    View headView = new View(getContext());
                    RecyclerView.LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, mHeadViewHeight);
                    lp.topMargin = -mFixedViewHeight;
                    headView.setLayoutParams(lp);
                    return new SimpleAdapter.SimpleViewHolder(headView);
                case 0x12346://FootView
                    if (mFooterView == null) {
                        mFooterView = new View(getContext());
                    }
                    return new SimpleAdapter.SimpleViewHolder(mFooterView);
                default:
                    return this.adapter.onCreateViewHolder(parent, viewType);
            }
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (this.adapter != null) {
                if (position != 0 && position <= adapter.getItemCount()) {
                    //避免使用原始类型警告
                    this.adapter.onBindViewHolder(holder, position - 1, null);
                }
            }
        }

        public int getItemViewType(int position) {
            if (position == 0) {
                return 0x12345;
            } else if (position <= adapter.getItemCount()) {
                return this.adapter.getItemViewType(position - 1);
            } else {
                return 0x12346;
            }
        }

        @Override
        public int getItemCount() {
            if (this.adapter != null) {
                return this.adapter.getItemCount() + 2;
            }
            return 0;
        }

        @Override
        public void unregisterAdapterDataObserver(AdapterDataObserver observer) {
//            super.unregisterAdapterDataObserver(observer);
            if (this.adapter != null) {
                this.adapter.unregisterAdapterDataObserver(observer);
            }
        }

        @Override
        public void registerAdapterDataObserver(AdapterDataObserver observer) {
//            super.registerAdapterDataObserver(observer);
            if (this.adapter != null) {
                this.adapter.registerAdapterDataObserver(observer);
            }
        }

        private class SimpleViewHolder extends ViewHolder {
            SimpleViewHolder(View itemView) {
                super(itemView);
            }
        }
    }
}
