package com.wc.headrecyclerview;

import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * ViewPager
 * Created by RushKing on 2016/10/11.
 */

public class ViewPagerAdapter extends android.support.v4.view.PagerAdapter {
    private List<View> views;
    private List<String> titles;

    public ViewPagerAdapter(List<View> views, List<String> titles) {
        super();
        this.views = views;
        this.titles = titles;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        // TODO Auto-generated method stub
        return arg0 == arg1;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return views.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));

    }

    @Override
    public int getItemPosition(Object object) {

        return super.getItemPosition(object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (titles != null) {
            return titles.get(position);
        } else {
            return super.getPageTitle(position);
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(views.get(position));
        return views.get(position);
    }
}
