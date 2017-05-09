package com.wc.headrecyclerview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.wc.pagerbar.PagerNavigationBar;
import com.wc.recyclerview.HeadLayout;
import com.wc.recyclerview.HeadRecyclerView;
import com.wc.recyclerview.HeadViewPager;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {
    private HeadLayout ll_head;
    private HeadViewPager viewPager;
    private PagerNavigationBar pagerBar;
    private List<View> views;
    private int mCount = 0;//按钮点击次数测试

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ll_head = (HeadLayout) findViewById(R.id.ll_head);
        viewPager = (HeadViewPager) findViewById(R.id.viewPager);
        pagerBar = (PagerNavigationBar) findViewById(R.id.pagerBar);
        List<String> titles = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            titles.add("标签" + i);
        }
        views = new ArrayList<>();
        for (int i = 0; i < titles.size(); i++) {
            HeadRecyclerView recyclerView = new HeadRecyclerView(this);
            recyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            //recyclerView.setTag(titles.get(i));
            List<String> infos = new ArrayList<>();
            switch (i) {
                case 3:
                    for (int j = 0; j < 14; j++) {
                        infos.add("嗯哼嗯哼嗯哼，这个是item\t" + j);
                    }
                    break;
                case 4:
                    for (int j = 0; j < 23; j++) {
                        infos.add("嗯哼嗯哼嗯哼，这个是item\t" + j);
                    }
                    break;
                case 6:
                    for (int j = 0; j < 29; j++) {
                        infos.add("嗯哼嗯哼嗯哼，这个是item\t" + j);
                    }
                    break;
                default:
                    for (int j = 0; j < 200; j++) {
                        infos.add("嗯哼嗯哼嗯哼，这个是item\t" + j);
                    }
            }
            recyclerView.setAdapter(new TextAdapter(this, infos));
            recyclerView.setHeadView(ll_head);
            views.add(recyclerView);
        }
        //目前必须设置缓存为所有
        viewPager.setOffscreenPageLimit(views.size());
        viewPager.setAdapter(new ViewPagerAdapter(views, titles));
        //如果有需要滑动的HeadView需要设置这个，没有就不用
        viewPager.setHeadView(ll_head);
        // 关联
        pagerBar.setViewPager(viewPager);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Button) v).setText(MessageFormat.format("点击了按钮{0}", ++mCount));
            }
        });
    }
}
