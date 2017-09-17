package com.example.pengxiaolve.refreshrecyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.XRecyclerview.SwipeRefreshAdapter;
import com.example.XRecyclerview.SwipeRefreshRecyclerView;

import java.util.List;

/**
 * Created by copengxiaolue on 2017/04/26.
 */

public class SwipeRefreshAdapterImp extends SwipeRefreshAdapter<SwipeRefreshAdapterImp.SwipeRefreshViewHolder> {

    private LayoutInflater mInflater;
    private List<String> mDatas;

    private static final String TAG = "SwipeRefreshAdapterImp";

    public SwipeRefreshAdapterImp(Context context, List<String> datas) {
        mInflater = LayoutInflater.from(context);
        mDatas = datas;
    }

    @Override
    public int getItemDataCount() {
        return mDatas.size();
    }

    @Override
    public SwipeRefreshRecyclerView.ViewHolder onCreateSwipeViewHolder(ViewGroup parent, int viewType) {
        View slideLayout = mInflater.inflate(R.layout.slide_layout, parent, false);
        SwipeRefreshRecyclerView.ViewHolder viewHolder = new SwipeRefreshViewHolder(slideLayout);
        return viewHolder;
    }

    @Override
    public void onBindSwipeViewHolder(SwipeRefreshViewHolder holder, int position) {
        holder.setText(mDatas.get(position));
    }

    class SwipeRefreshViewHolder extends SwipeRefreshRecyclerView.ViewHolder {
        TextView textView;
        View subView1;
        View subView2;

        public SwipeRefreshViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) getViewById(R.id.id_contentText);
            subView1 = getViewById(R.id.subView1);

            setSubViewClickListener(subView1);
        }

        public void setText(String content) {
            textView.setText(content);
        }
    }
}
