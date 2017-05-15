package com.example.XRecyclerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * Created by pengxiaolve on 17/1/7.
 */
public class SwipeLayout extends FrameLayout {
    private View mContentView;
    private View mMenuView;
    private float mPreX = 0f;
    private float mPreY = 0f;
    private Scroller mScroller = null;
    private static final String TAG = "SwipeLayout";

    private float mXLastMove = 0f;
    private float mXMove = 0f;

    public SwipeLayout(Context context) {
        super(context);
        init();
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SwipeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SwipeLayout(Context context, View contentView, View menuView) {
        super(context);
        mContentView = contentView;
        mMenuView = menuView;
        mScroller = new Scroller(context);
        init();
    }

    private void init() {
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//        this.setBackgroundColor(Color.BLUE);
        if (mContentView != null) {
//            mContentView.setLayoutParams(layoutParams);
            addView(mContentView);
//            int contentViewWidth = mContentView.getWidth();
//            int contentViewHeight = mContentView.getHeight();
//            Log.d(TAG, "init() called with: " + "contentViewWidth" + contentViewWidth);
//            Log.d(TAG, "init() called with: " + "contentViewHeight" + contentViewHeight);
        }

        if (mMenuView != null) {
//            mMenuView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
            addView(mMenuView);
//            int menuViewWidth = mMenuView.getWidth();
//            int menuViewHeight = mMenuView.getHeight();
//            Log.d(TAG, "init() called with: " + "menuViewWidth" + menuViewWidth);
//            Log.d(TAG, "init() called with: " + "menuViewHeight" + menuViewHeight);
        }
//        setWillNotDraw(false);
        this.setLayoutParams(layoutParams);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        Log.d(TAG, "onMeasure() called with: " + "count = [" + count + "]");

        /** 自动测量出来的父view的宽高 */
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
        int parentWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int parentHeightMode = MeasureSpec.getMode(heightMeasureSpec);

        for (int i = 0; i <count; i++) {
            View childView = getChildAt(i);
            final ViewGroup.LayoutParams lp = childView.getLayoutParams();
            int width = lp.width;
            int height = lp.height;
        }

        /** 测量子view的宽高 */
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        /** contentView的宽高 */
        int contentWidth = 0;
        int contentHeight = 0;

        /** menuView的宽高 */
        int menuWidth = 0;
        int menuHeight = 0;

        /** 子View的宽高，contentView，menuView的最大值 */
        int childWidth = 0;
        int childHeight = 0;

        for (int i = 0; i < count; i++) {
            if (i == 0) {
                contentWidth = getChildAt(i).getMeasuredWidth();
                contentHeight = getChildAt(i).getMeasuredHeight();
            } else {
                menuWidth = getChildAt(i).getMeasuredWidth();
                menuHeight = getChildAt(i).getMeasuredHeight();
            }
        }

        childHeight = Math.max(contentHeight, menuHeight);

        if (parentHeightMode == MeasureSpec.AT_MOST || parentHeightMode == MeasureSpec.UNSPECIFIED) {
            parentHeight = childHeight;
        } else {
            Log.i(TAG, "onMeasure: EXACTLY");
        }

        setMeasuredDimension(parentWidth, parentHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int contentViewWidth;
        int contentViewHeight;

        if (mContentView != null) {
            contentViewWidth = mContentView.getWidth();
            contentViewHeight = mContentView.getHeight();
            mContentView.layout(0, 0, contentViewWidth, contentViewHeight);
            Log.d(TAG, "onLayout() called with: " + "contentViewWidth = [" + contentViewWidth + "], contentViewHeight = [" + contentViewHeight + "]");

            if (mMenuView != null) {
                mMenuView.layout(contentViewWidth, 0, contentViewWidth + mMenuView.getMeasuredWidth(),
                        contentViewHeight);
                Log.d(TAG, "onLayout() called with: " + "mMenuView.getMeasuredWidth() = [" + mMenuView.getMeasuredWidth() + "]");
                Log.d(TAG, "onLayout() called with: " + "mMenuView.getWidth() = [" + mMenuView.getWidth() + "]");
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        Rect rect = new Rect(0, 0, mContentView.getWidth(), mContentView.getHeight());
//        Log.d(TAG, "onDraw() called with: " + "mContentView.getWidth() = [" + mContentView.getWidth() + "]");
//        Log.d(TAG, "onDraw() called with: " + "mContentView.getHeight() = [" + mContentView.getHeight() + "]");
        canvas.drawRect(rect, paint);
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.computeScrollOffset()) {
                    mXLastMove = event.getX();
                    Log.d(TAG, "ACTION_DOWN: event.getX() = " + event.getX());
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "ACTION_MOVE: getScrollX = " + getScrollX());
                Log.d(TAG, "ACTION_MOVE: mScroller.getCurrX = " + mScroller.getCurrX());
                Log.d(TAG, "ACTION_MOVE: event.getX() = " + event.getX());

//                mScroller.computeScrollOffset();
//                mXMove = event.getX();
//                int scrollX = (int) (mXLastMove - mXMove);
//                Log.d(TAG, "onTouchEvent: scrollX = " + scrollX);
//                if (scrollX == mMenuView.getWidth() && scrollX > 0
//                        || getScrollX() == 0 && scrollX < 0) {
//                    return super.onTouchEvent(event);
//                }
//
//                mXLastMove = mXMove;
//                if (getScrollX() > mMenuView.getWidth()) {
//                    scrollX = mMenuView.getWidth();
//                    scrollTo(scrollX, 0);
//                    return true;
//                }
//                scrollBy(scrollX, 0);


                if (!mScroller.computeScrollOffset()) {
                    mXMove = event.getX();
                    int scrollX = mScroller.getCurrX();
                    float distanceX = (int) (mXLastMove - mXMove);
                    Log.d(TAG, "ACTION_MOVE: distanceX = " + distanceX);

                    //1.菜单未被拖出，只能向左滑动,向右滑动不处理
                    //2.菜单被完全拖出，只能向右滑动，向左滑动不处理
                    //3.菜单已经被拖出，向左滑动，最大不能超过view的宽度，向右滑动不能超过原点位置

                    //菜单已完全拖出
                    if (scrollX >= mMenuView.getWidth()) {
                        Log.d(TAG, "ACTION_MOVE: case 1:scrollX = " + scrollX);
                        //可处理向右滑动
                        if (distanceX < 0) {
                            doScroll(distanceX);
                            return true;
                        }
                    }
                    //菜单未被拖出
                    if (scrollX <= 0) {
                        Log.d(TAG, "ACTION_MOVE: case 2:scrollX = " + scrollX);
                        //可处理向左滑动
                        if (distanceX > 0) {
                            doScroll(distanceX);
                            return true;
                        }
                    }
                    if (scrollX > 0 && scrollX < mMenuView.getWidth()) {
                        Log.d(TAG, "ACTION_MOVE: case 3:scrollX = " + scrollX);
                        //向左滑动
                        if (distanceX > 0) {
                            distanceX = distanceX + scrollX > mMenuView.getWidth() ? mMenuView.getWidth() - scrollX : distanceX;
                        }

                        //向右滑动
                        if (distanceX < 0) {
                            distanceX = distanceX + scrollX < 0 ? scrollX : distanceX;
                        }

                        doScroll(distanceX);
                        return true;
                    }

                    mXLastMove = mXMove;
                }

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                Log.d(TAG, "ACTION_UP: received");
                if (getScrollX() > mMenuView.getWidth()/2) {
                    Log.d(TAG, "ACTION_UP: OpenMenu");
                    scrollOpenMenu();
                } else {
                    Log.d(TAG, "ACTION_UP: CloseMenu");
                    scrollCloseMenu();
                }
                return true;
//            case MotionEvent.ACTION_CANCEL:
//                Log.d(TAG, "ACTION_CANCEL: received");
//                break;
        }

        return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            Log.d(TAG, "computeScroll: mScroller.getFinalX() = " + mScroller.getFinalX());
            Log.d(TAG, "computeScroll: mScroller.getCurrX() = " + mScroller.getCurrX());
            scrollTo(mScroller.getCurrX(), 0);
            postInvalidate();
        }
        super.computeScroll();
    }

    private void smoothScrollBy(int dx, int dy) {
        Log.d(TAG, "smoothScrollBy: mScroller.getFinalX() = " + mScroller.getFinalX());
        Log.d(TAG, "smoothScrollBy: mScroller.getCurrX() = " + mScroller.getCurrX());
//        mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx, dy);
        //此处与dx的计算必须用同一个量getCurrX/getFinalX，不能一个用getCurrX，一个用getFinalX，抬手的时候getCurrX！-=getFinalX
        mScroller.startScroll(mScroller.getCurrX(), mScroller.getCurrY(), dx, dy);
        invalidate();
    }

    private void smoothScrollTo(int finalX, int finalY) {
//        int dx = finalX - mScroller.getFinalX();
//        int dy = finalY - mScroller.getFinalY();

        int dx = finalX - mScroller.getCurrX();
        int dy = finalY - mScroller.getCurrY();
        smoothScrollBy(dx, dy);
    }

    private void doScroll(float distance) {
        int scrollX = getScrollX();
        Log.d(TAG, "doScroll: getScrollX() = " + scrollX);
        int finalX = scrollX + (int)distance;
        mScroller.setFinalX(finalX);
        mXLastMove = mXMove;
        invalidate();
    }

    public void scrollOpenMenu() {
        smoothScrollTo(mMenuView.getWidth(), 0);
    }

    public void scrollCloseMenu() {
        smoothScrollTo(0, 0);
    }

    public View getMenuView() {
        return mMenuView;
    }
}
