package com.wc.headrecyclerview;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.wc.recyclerview.HeadLayout;
import com.wc.recyclerview.HeadRecyclerView;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView mTextView;
    private HeadRecyclerView mRecyclerView;
    private HeadLayout mHeadLayout;
    private int mCount = 0;//按钮点击次数测试

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.tv_fixed);
        mHeadLayout = (HeadLayout) findViewById(R.id.head_layout);
        mRecyclerView = (HeadRecyclerView) findViewById(R.id.recycler_view);

        final List<String> infos = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            infos.add("啦啦啦啦啦，这个是item\t" + i);
        }
        final TextAdapter adapter = new TextAdapter(this, infos);
        //设置布局管理器和适配器
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //支持GridLayoutManager
        //mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.setAdapter(adapter);
        //设置HeadView
        mRecyclerView.setHeadView(mHeadLayout);
        //设置上拉加载更多监听
        mRecyclerView.setOnLoadMoreListener(new HeadRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore(HeadRecyclerView view) {
                for (int i = 500; i < 588; i++) {
                    infos.add("加载更多，这个是item\t" + i);
                }
                //如果是本地加载不能直接刷新，网络加载本身就有一点延迟
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        mRecyclerView.resetLoadMore();
                    }
                }, 50);
            }
        });

        findViewById(R.id.botton1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextView.setText(MessageFormat.format("点击了按钮{0}", ++mCount));
            }
        });
        findViewById(R.id.botton2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Main2Activity.class));
            }
        });
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextView.setText(MessageFormat.format("点击了定点View{0}", ++mCount));
            }
        });

    }
}
