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
        super(context);
        init(context);
    }

    public FooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public enum FooterState {
        HIND,
        LOADING,
        READY
    }

    private void init(Context context) {
        View.inflate(context, getFooterLayoutRes(), this);
        mRootContainer = getRootContainer();
        initView();
        setFooterState(LOADING);
    }

    public void setFooterState(FooterState state) {
        mFooterState = state;
        refreshView();
    }

    public abstract void initView();

    public abstract int getFooterLayoutRes();

    public abstract RelativeLayout getRootContainer();

    public abstract void onLoadingState();

    public abstract void onReadyState();

    private void refreshView() {
        switch (mFooterState) {
            case HIND:
                mRootContainer.setVisibility(GONE);
                break;
            case LOADING:
                onLoadingState();
                mRootContainer.setVisibility(VISIBLE);
                break;
            case READY:
                onReadyState();
                mRootContainer.setVisibility(VISIBLE);
                break;
            default:
                break;
        }
    }
}
