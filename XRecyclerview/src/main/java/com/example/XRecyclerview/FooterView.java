package com.example.XRecyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import static com.example.XRecyclerview.FooterView.FooterState.LOADING;

/**
 * Created by copengxiaolue on 2017/04/25.
 */

public abstract class FooterView extends RelativeLayout{

    private FooterState mFooterState;
    private RelativeLayout mRootContainer;

    public FooterView(Context context) {
        this(context, null);
    }

    public FooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public enum FooterState {
        HIND,
        LOADING,
        FINISH
    }

    private void init() {
        View.inflate(getContext(), getFooterLayoutResId(), this);
        mRootContainer = getRootContainer();
        initView();
        setFooterState(LOADING);
    }

    public void setFooterState(FooterState state) {
        mFooterState = state;
        refreshView();
    }

    public abstract void initView();

    public abstract int getFooterLayoutResId();

    public abstract RelativeLayout getRootContainer();

    public abstract void onLoadingState();

    public abstract void onFinishState();

    private void refreshView() {
        switch (mFooterState) {
            case HIND:
                mRootContainer.setVisibility(GONE);
                break;
            case LOADING:
                onLoadingState();
                mRootContainer.setVisibility(VISIBLE);
                break;
            case FINISH:
                onFinishState();
                mRootContainer.setVisibility(GONE);
                break;
            default:
                break;
        }
    }
}
