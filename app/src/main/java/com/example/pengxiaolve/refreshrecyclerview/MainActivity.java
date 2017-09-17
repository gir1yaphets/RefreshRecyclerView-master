package com.example.pengxiaolve.refreshrecyclerview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mStartSwipeActivity;
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
    public void onClick(View view) {
        if (view.getId() == R.id.startSwipeActivity) {
            Intent intent = new Intent(this, SwipeRefreshActivity.class);
            startActivity(intent);
        }
    }
}
