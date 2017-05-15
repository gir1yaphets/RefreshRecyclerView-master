package com.example.pengxiaolve.refreshrecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.XRecyclerview.SwipeLayout;

import java.util.List;

/**
 * Created by pengxiaolve on 17/1/7.
 */
public class SwipeAdapter extends RecyclerView.Adapter<SwipeViewHolder> {

    List<String> mData;
    Context mContext;
    LayoutInflater mInflater;
    private static final String TAG = "SwipeAdapter";

    public SwipeAdapter(Context context, List<String> data) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @Override
    public SwipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contentView = mInflater.inflate(R.layout.content_view, parent, false);
        View menuView = mInflater.inflate(R.layout.slide_view, parent, false);

        //Todo:lp.height与xml设定的值不等
        ViewGroup.LayoutParams lp = contentView.getLayoutParams();
        int width = lp.width;
        int height = lp.height;
        SwipeLayout swipeLayout = new SwipeLayout(mContext, contentView, menuView);
        SwipeViewHolder swipeViewHolder = new SwipeViewHolder(swipeLayout);
        return swipeViewHolder;
    }

    @Override
    public void onBindViewHolder(SwipeViewHolder holder, int position) {
        holder.setText(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
