package com.example.XRecyclerview;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.InvalidClassException;

/**
 * Created by copengxiaolue on 2017/04/25.
 */

public class SwipeRefreshRecyclerView extends RecyclerView {

    private static final String TAG = "SwipeRefreshRecyclerVie";

    private SwipeRefreshAdapter<ViewHolder> mAdapter;
    private int mStartX, mCurrX;
    private int mStartY, mCurrY;
    private float mCurrentHeaderHeight;
    private int mLastPosition = -1;

    private ViewHolder mLastHolder;

    private static final float REFRESH_HEIGHT_MIN = 150f;
    private boolean mIsRefreshing;
    private boolean mIsLoading;

    private OnRefreshListener mRefreshListener;
    private static OnMenuClickListener mMenuClickListener;

    public SwipeRefreshRecyclerView(Context context) {
        super(context);
    }

    public SwipeRefreshRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SwipeRefreshRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public static abstract class ViewHolder extends RecyclerView.ViewHolder {
        private SwipeLayout swipeLayout;
        private View menuView;

        public ViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView;
            menuView = swipeLayout.getMenuView();

            if (menuView != null) {
                menuView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        swipeLayout.scrollCloseMenu();
                        if (mMenuClickListener != null) {
                            mMenuClickListener.onMenuClick(getAdapterPosition() - 1);
                        }
                    }
                });
            }
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if (adapter instanceof SwipeRefreshAdapter) {
            mAdapter = (SwipeRefreshAdapter<ViewHolder>) adapter;
        } else {
            try {
                throw new InvalidClassException("所使用Adapter并非SwipeMenuAdapter的子类");
            } catch (InvalidClassException e) {
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartX = (int) e.getX();
                mStartY = (int) e.getY();

                int currentPosition = getChildAdapterPosition(findChildViewUnder(mStartX, mStartY));
                Log.d(TAG, "onInterceptTouchEvent() called with: currentPosition = [" + currentPosition + "]");
                Log.d(TAG, "onInterceptTouchEvent() called with: mLastPosition = [" + mLastPosition + "]");

                //当前点击的不是headerview和footerview
                if (currentPosition != 0 && currentPosition != mAdapter.getItemCount() - 1) {
                    //找到本次点击的viewholder作为lastviewholder使用，如果用last position去找会崩
                    ViewHolder viewHolder = (ViewHolder) findViewHolderForAdapterPosition(currentPosition);

                    //存在之前点击的position，并且本次点击的position与前次不是同一个时，将前次position的menu关闭
                    if (mLastPosition != -1 && currentPosition != mLastPosition) {
//                        ViewHolder holder = (ViewHolder) findViewHolderForAdapterPosition(mLastPosition);
//                        SwipeLayout swipeLayout = (SwipeLayout) holder.itemView;
//                        if (swipeLayout!= null && swipeLayout.getScrollX() != 0) {
//                            swipeLayout.scrollCloseMenu();
//                        }
                        if (mLastHolder != null) {
                            SwipeLayout swipeLayout = (SwipeLayout) mLastHolder.itemView;
                            if (swipeLayout != null && swipeLayout.getScrollX() != 0) {
                                swipeLayout.scrollCloseMenu();
                            }
                        }
                    }

                    mLastPosition = currentPosition;
                    mLastHolder = viewHolder;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;

        }
        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "onTouchEvent: ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "onTouchEvent: ACTION_MOVE");
                if (mIsRefreshing) {
                    return super.onTouchEvent(e);
                }

                mCurrY = (int) e.getY();
                if (getChildAt(1) != null && computeVerticalScrollOffset() <= getChildAt(1).getHeight()) {
                    int distanceY = mCurrY - mStartY;
                    if (distanceY > 0) {
                        doPulling(distanceY);
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!mIsRefreshing) {
                    mCurrentHeaderHeight = mAdapter.getHeaderView().getHeight();
                    doPullUp();
                }
                break;
        }
        return super.onTouchEvent(e);
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);

        if (!canScrollVertically(1)) {
            if (!mIsLoading) {
                startLoadMore();
            }
        }
    }

    private void startLoadMore() {
        if (mRefreshListener != null) {
            mRefreshListener.onLoadMore();
            mIsLoading = true;
        }

        mAdapter.getFooterView().setFooterState(FooterView.FooterState.LOADING);
    }

    private void doPulling(int distanceY) {
        float moveDistance = distanceY * 0.6f;
        HeaderView headerView = mAdapter.getHeaderView();
        if (moveDistance <= REFRESH_HEIGHT_MIN) {
            headerView.setNowState(HeaderView.HeaderState.PULLING);
        } else {
            headerView.setNowState(HeaderView.HeaderState.READY);
        }

        headerView.setHeaderViewHeight(moveDistance);
    }

    private void doPullUp() {
        if (mAdapter.getHeaderView().getHeight() > REFRESH_HEIGHT_MIN) {
            //开始刷新
            startRefresh();
        } else {
            //回到原位置
            autoScrollBack();
        }
    }

    private ValueAnimator.AnimatorUpdateListener mUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            float value = (float) animation.getAnimatedValue();
            HeaderView headerView = mAdapter.getHeaderView();
            headerView.setHeaderViewHeight(mCurrentHeaderHeight * value);
        }
    };

    private Animator.AnimatorListener mAnimatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            mAdapter.getHeaderView().setNowState(HeaderView.HeaderState.HIND);
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

    private void autoScrollBack() {
        ValueAnimator animator = ObjectAnimator.ofFloat(1, 0);
        animator.setDuration(200);
        animator.addUpdateListener(mUpdateListener);
        animator.addListener(mAnimatorListener);
        animator.start();
    }

    public void startRefresh() {
        mIsRefreshing = true;
        HeaderView headerView = mAdapter.getHeaderView();
        headerView.setNowState(HeaderView.HeaderState.REFRESHING);
        headerView.setHeaderViewHeight(headerView.getLayoutParams().height);
        if (mRefreshListener != null) {
            mRefreshListener.onRefresh();
        }
    }

    public void stopRefresh() {
        mIsRefreshing = false;
        autoScrollBack();
    }

    public void stopLoadMore() {
        mIsLoading = false;
        mAdapter.getFooterView().setFooterState(FooterView.FooterState.READY);
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        this.mRefreshListener = listener;
    }

    public void setOnMenuClickListener(OnMenuClickListener listener) {
        this.mMenuClickListener = listener;
    }

    public interface OnRefreshListener {
        void onRefresh();

        void onLoadMore();
    }

    public interface OnMenuClickListener {
        void onMenuClick(int position);
    }
}
