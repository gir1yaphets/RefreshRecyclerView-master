package com.example.XRecyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by copengxiaolue on 2017/04/26.
 */

public class DefaultFooterView extends FooterView {

    private TextView mFooterText;
    public DefaultFooterView(Context context) {
        super(context);
    }

    public DefaultFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DefaultFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void initView() {
        mFooterText = (TextView) findViewById(R.id.footerText);
    }

    @Override
    public int getFooterLayoutResId() {
        return R.layout.default_footer_layout;
    }

    @Override
    public void onLoadingState() {
        mFooterText.setText("加载更多");
    }

    @Override
    public void onFinishState() {
        mFooterText.setText("加载完成");
    }

    @Override
    public RelativeLayout getRootContainer() {
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.footerRoot);
        return layout;
    }
}
