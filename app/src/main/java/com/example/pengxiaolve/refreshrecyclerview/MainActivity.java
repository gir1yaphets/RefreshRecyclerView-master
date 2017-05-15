package com.example.pengxiaolve.refreshrecyclerview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Scroller;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mStartSwipeActivity;
    private Scroller mScroller;
    private RecyclerView mRecyclerView;
    private List<String> mData = new ArrayList<>();
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        mStartSwipeActivity = (Button) findViewById(R.id.startSwipeActivity);
        mStartSwipeActivity.setOnClickListener(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setAdapter(new SwipeAdapter(this, mData));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    private void initData() {
        for (int i = 'A'; i < 'Z'; i++) {
            mData.add((char)i + "");
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float preX = 0;
        float preY = 0;
        float newX = 0;
        float newY = 0;

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "onTouchEvent() called with: " + "event = [" + event + "]");
                preX = event.getX();
                preY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                mScroller = new Scroller(this);
                newX = event.getX();
                newY = event.getY();

                float distance = newX - preX;
                Log.d(TAG, "onTouchEvent() called with: " + "distance = [" + distance + "]");
                float x = mScroller.getCurrX();
//                Log.d(TAG, "onTouchEvent() called with: " + "x = [" + x + "]");
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "onTouchEvent() called with: " + "event = [" + event + "]");
                break;
        }

        return super.onTouchEvent(event);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.startSwipeActivity) {
            Intent intent = new Intent(this, SwipeRefreshActivity.class);
            startActivity(intent);
        }
    }
}
