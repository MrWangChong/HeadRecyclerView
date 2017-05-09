package com.wc.headrecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * 文字显示适配器
 * Created by RushKing on 2017/5/9.
 */

public class TextAdapter extends RecyclerView.Adapter<TextAdapter.RecyclerViewHolder> {
    private List<String> infos;
    private Context mContext;

    public TextAdapter(Context context, List<String> infos) {
        this.mContext = context;
        this.infos = infos;
    }

    // 获取数据的数量
    @Override
    public int getItemCount() {
        return infos.size();
    }


    // 创建新View，被LayoutManager所调用
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = new TextView(mContext);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new RecyclerViewHolder(view);
    }

    // 将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(RecyclerViewHolder viewHolder, int position) {
        viewHolder.text_view.setText(infos.get(position));
    }

    // 自定义的ViewHolder，持有每个Item的的所有界面元素
    class RecyclerViewHolder extends RecyclerView.ViewHolder {
        //商品详情
        TextView text_view;

        RecyclerViewHolder(View view) {
            super(view);
            text_view = (TextView) view;
        }
    }
}
