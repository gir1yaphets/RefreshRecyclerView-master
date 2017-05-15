package com.example.pengxiaolve.refreshrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by pengxiaolve on 17/1/7.
 */
public class SwipeViewHolder extends RecyclerView.ViewHolder {
    TextView mTextView;

    public SwipeViewHolder(View itemView) {
        super(itemView);
        mTextView = (TextView) itemView.findViewById(R.id.id_contentText);
    }

    public void setText(String text) {
        mTextView.setText(text);
    }
}
