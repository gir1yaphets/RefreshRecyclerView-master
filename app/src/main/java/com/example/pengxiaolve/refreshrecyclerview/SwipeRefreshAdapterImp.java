package com.example.pengxiaolve.refreshrecyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.XRecyclerview.SwipeLayout;
import com.example.XRecyclerview.SwipeRefreshAdapter;
import com.example.XRecyclerview.SwipeRefreshRecyclerView;

import java.util.List;

/**
 * Created by copengxiaolue on 2017/04/26.
 */

public class SwipeRefreshAdapterImp extends SwipeRefreshAdapter<SwipeRefreshAdapterImp.SwipeRefreshViewHolder> {

    private LayoutInflater mInflater;
    private List<String> mDatas;

    public SwipeRefreshAdapterImp(Context context, List<String> datas) {
        mInflater = LayoutInflater.from(context);
        mDatas = datas;
    }

    @Override
    public int getSwipeItemCount() {
        return mDatas.size();
    }

    @Override
    public SwipeRefreshRecyclerView.ViewHolder onCreateSwipeViewHolder(ViewGroup parent, int viewType) {
        View content = mInflater.inflate(R.layout.content_view, parent, false);
        View menu = mInflater.inflate(R.layout.slide_view, parent, false);
        SwipeLayout swipeLayout = new SwipeLayout(parent.getContext(), content, menu);
        SwipeRefreshRecyclerView.ViewHolder viewHolder = new SwipeRefreshViewHolder(swipeLayout);
        return viewHolder;
    }

    @Override
    public void onBindSwipeViewHolder(SwipeRefreshViewHolder holder, int position) {
        holder.setText(mDatas.get(position));
    }

    class SwipeRefreshViewHolder extends SwipeRefreshRecyclerView.ViewHolder {
        TextView textView;

        public SwipeRefreshViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.id_contentText);
        }

        public void setText(String content) {
            textView.setText(content);
        }
    }
}
