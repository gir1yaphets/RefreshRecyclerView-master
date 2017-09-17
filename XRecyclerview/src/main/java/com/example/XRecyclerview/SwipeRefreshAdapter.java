package com.example.XRecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by copengxiaolue on 2017/04/25.
 */

public abstract class SwipeRefreshAdapter<V extends SwipeRefreshRecyclerView.ViewHolder> extends RecyclerView.Adapter {
    private static final int HEADER_TYPE = 0x1100;
    private static final int FOOTER_TYPE = 0x1101;

    private HeaderFooterViewHolder mHeaderViewHolder;
    private HeaderFooterViewHolder mFooterViewHolder;

    private HeaderView mHeaderView;
    private FooterView mFooterView;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEADER_TYPE) {
            mHeaderView = mHeaderView == null ? new DefaultHeaderView(parent.getContext()) : mHeaderView;
            mHeaderViewHolder = new HeaderFooterViewHolder(mHeaderView);
            return mHeaderViewHolder;
        } else if (viewType == FOOTER_TYPE) {
            mFooterView = mFooterView == null ? new DefaultFooterView(parent.getContext()) : mFooterView;
            mFooterViewHolder = new HeaderFooterViewHolder(mFooterView);
            return mFooterViewHolder;
        } else {
            return onCreateSwipeViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position != 0 && position != getItemCount() - 1) {
            onBindSwipeViewHolder((V) holder, position - 1);
        }
    }

    @Override
    public int getItemCount() {
        return getItemDataCount() + 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEADER_TYPE;
        } else if (position == getItemCount() - 1) {
            return FOOTER_TYPE;
        } else {
            return super.getItemViewType(position);
        }
    }

    public abstract int getItemDataCount();

    public abstract SwipeRefreshRecyclerView.ViewHolder onCreateSwipeViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindSwipeViewHolder(V holder, int position);

    class HeaderFooterViewHolder extends RecyclerView.ViewHolder {
        public HeaderFooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void addHeaderView(HeaderView headerView) {
        this.mHeaderView = headerView;
    }

    public void addFooterView(FooterView footerView) {
        this.mFooterView = footerView;
    }

    public HeaderView getHeaderView() {
        return mHeaderView;
    }

    public FooterView getFooterView() {
        return mFooterView;
    }
}
