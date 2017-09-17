package com.example.XRecyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by copengxiaolue on 2017/04/25.
 */

public class DefaultHeaderView extends HeaderView {

    private TextView mTextView;
    private ProgressBar mProgressBar;

    public DefaultHeaderView(Context context) {
        super(context);
    }

    public DefaultHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DefaultHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void initView() {
        mTextView = (TextView) findViewById(R.id.headerText);
        mProgressBar = (ProgressBar) findViewById(R.id.headerProgressBar);
    }

    @Override
    public RelativeLayout getRootContainer() {
        return (RelativeLayout) findViewById(R.id.rootContainer);
    }

    @Override
    public int getHeaderLayoutResId() {
        return R.layout.default_header_layout;
    }

    @Override
    public void onPullingState() {
        mTextView.setText(getContext().getResources().getString(R.string.pull_refresh));
        mProgressBar.setVisibility(INVISIBLE);
    }

    @Override
    public void onRefreshingState() {
        mTextView.setText(getContext().getResources().getString(R.string.in_refresh));
        mProgressBar.setVisibility(VISIBLE);
    }

    @Override
    public void onReadyState() {
        mTextView.setText(getContext().getResources().getString(R.string.ready_refresh));
        mProgressBar.setVisibility(INVISIBLE);
    }
}
