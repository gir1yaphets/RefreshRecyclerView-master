package com.example.XRecyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by copengxiaolue on 2017/04/25.
 */

public abstract class HeaderView extends RelativeLayout {

    private HeaderState mState;
    private RelativeLayout mRootContainer;

    public enum HeaderState{
        HIND,
        PULLING,
        REFRESHING,
        READY
    }

    public HeaderView(Context context) {
        this(context, null);
    }

    public HeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setNowState(HeaderState state) {
        mState = state;
        refreshView();
    }

    private void init() {
        View.inflate(getContext(), getHeaderLayoutResId(), this);
        mRootContainer = getRootContainer();
        initView();
        setNowState(HeaderState.HIND);
    }

    public void setHeaderViewHeight(float height) {
        RelativeLayout.LayoutParams params = (LayoutParams) mRootContainer.getLayoutParams();
        params.height = (int) height;
        mRootContainer.setLayoutParams(params);
        invalidate();
    }

    public abstract void initView();

    public abstract RelativeLayout getRootContainer();

    public abstract int getHeaderLayoutResId();

    public abstract void onPullingState();

    public abstract void onRefreshingState();

    public abstract void onReadyState();

    private void refreshView() {
        switch (mState) {
            case HIND:
                mRootContainer.setVisibility(View.GONE);
                break;
            case PULLING:
                mRootContainer.setVisibility(View.VISIBLE);
                onPullingState();
                break;
            case REFRESHING:
                mRootContainer.setVisibility(View.VISIBLE);
                onRefreshingState();
                break;
            case READY:
                mRootContainer.setVisibility(View.VISIBLE);
                onReadyState();
                break;
            default:
                break;
        }
    }
}
