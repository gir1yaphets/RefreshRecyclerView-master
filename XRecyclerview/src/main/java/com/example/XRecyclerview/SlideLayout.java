package com.example.XRecyclerview;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * Created by pengxiaolve on 17/9/16.
 */

public class SlideLayout extends FrameLayout {

    private View mMainView;
    private View mSubView1;
    private View mSubView2;
    private float mStartX;
    private Scroller mScroller;

    private static final String TAG = "SlideLayout";

    public SlideLayout(@NonNull Context context) {
        this(context, null);
    }

    public SlideLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams
                .WRAP_CONTENT));
        mScroller = new Scroller(context);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure: ");
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int childCount = getChildCount();

        if (childCount < 2 || childCount > 3) {
            throw new IllegalArgumentException("Not support child view count = " + childCount);
        }

        for (int i = 0; i < childCount; i++) {
            if (i == 0) {
                mMainView = getChildAt(i);
            } else if (i == 1) {
                mSubView1 = getChildAt(i);
            } else {
                mSubView2 = getChildAt(i);
            }
        }

        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int parentWidth = widthSize;
        int parentHeight = heightSize;

        if (widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.UNSPECIFIED) {
            parentWidth = mMainView.getMeasuredWidth() + mSubView1.getMeasuredWidth() + (mSubView2 == null ? 0 :
                    mSubView2.getMeasuredWidth());
        }

        if (heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.UNSPECIFIED) {
            parentHeight = Math.max(mMainView.getMeasuredHeight(), mSubView1.getMeasuredHeight());
        }

        setMeasuredDimension(parentWidth, parentHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        mMainView.layout(0, 0, mMainView.getMeasuredWidth(), mMainView.getMeasuredHeight());
        mSubView1.layout(mMainView.getMeasuredWidth(), 0, mMainView.getMeasuredWidth() + mSubView1.getMeasuredWidth
                (), mSubView1.getMeasuredHeight());

        if (mSubView2 != null) {
            int leftOffset = mMainView.getMeasuredWidth() + mSubView1.getMeasuredWidth();
            mSubView2.layout(leftOffset, 0, leftOffset + mSubView2.getMeasuredWidth(),
                    mSubView2.getMeasuredHeight());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int subViewWidth = mSubView1.getWidth() + (mSubView2 == null ? 0 : mSubView2.getWidth());

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "onTouchEvent: ACTION_DOWN");
                mStartX = event.getX();
                return true;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "onTouchEvent: ACTION_MOVE");
                int distanceX = (int) (mStartX - event.getX());
                mStartX = event.getX();

                /** Only when slide view appear, can the view slide */
                if (getScrollX() >= 0 && getScrollX() <= subViewWidth) {
                    /** When slide to left, cannot over subViewWidth */
                    Log.d(TAG, "onTouchEvent: distanceX = " + distanceX);
                    if (distanceX >= 0) {
                        if (distanceX + getScrollX() > subViewWidth) {
                            distanceX = subViewWidth - getScrollX();
                        }
                    } else { /** When slide to right, cannot over the layout origin position(0, 0) */
                        if (distanceX + getScrollX() < 0) {
                            distanceX = -getScrollX();
                        }
                    }

                    scrollBy(distanceX, 0);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                Log.d(TAG, "onTouchEvent: ACTION_UP or ACTION_CANCEL");
                /** If parent view intercept ACTION_MOVE(up/down scroll), this child view will receive ACTION_CANCEL instead of ACTION_MOVE */
                /** If subView appear over half part, auto open subview */
                if (getScrollX() >= subViewWidth / 2) {
                    autoScrollOpenSubView(subViewWidth);
                } else { /** If subView appear less then half part, auto close subview */
                    autoScrollCloseSubView();
                }
                return true;
        }

        return super.onTouchEvent(event);
    }

    public void autoScrollCloseSubView() {
        smoothScrollTo(0, 0);
    }

    public void autoScrollOpenSubView(int subViewWidth) {
        smoothScrollTo(subViewWidth, 0);
    }

    private void smoothScrollBy(int dx, int dy) {
        mScroller.startScroll(getScrollX(), 0, dx, dy);
        invalidate();
    }

    private void smoothScrollTo(int finalX, int finalY) {
        smoothScrollBy(finalX - getScrollX(), finalY - getScrollY());
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), 0);
            postInvalidate();
        }
    }

    public View getFirstSubView() {
        return mSubView1;
    }

    public View getSecondSubView() {
        return mSubView2;
    }
}
