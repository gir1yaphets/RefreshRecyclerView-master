package com.example.pengxiaolve.refreshrecyclerview;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.XRecyclerview.SwipeRefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by copengxiaolue on 2017/04/25.
 */

public class SwipeRefreshActivity extends Activity implements SwipeRefreshRecyclerView.OnRefreshListener {

    SwipeRefreshRecyclerView mRecyclerView;
    SwipeRefreshAdapterImp mAdapter;
    private List<String> mData = new ArrayList<>();

    private static final int REFRESH_DATA = 0;
    private static final int LOAD_MORE = 1;

    private static final String TAG = "SwipeRefreshActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swipe_refresh_activity_view);
        initData();
        initView();
    }

    private void initView() {
        mRecyclerView = (SwipeRefreshRecyclerView) findViewById(R.id.swipeRecyclerView);
        mAdapter = new SwipeRefreshAdapterImp(this, mData);
        mRecyclerView.setAdapter(mAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setOnRefreshListener(this);
        mRecyclerView.setOnMenuClickListener(new SwipeRefreshRecyclerView.OnMenuClickListener() {
            @Override
            public void onMenuClick(int position) {
                Log.d("SwipeRefreshRecyclerVie", "onMenuClick: ");
                mData.remove(position);
                mAdapter.notifyDataSetChanged();
//                mAdapter.notifyItemRemoved(position + 1);
            }
        });
    }

    private void initData() {
        for (int i = 'A'; i < 'Z'; i++) {
            mData.add((char)i + "");
        }
    }

    @Override
    public void onRefresh() {
        new RefreshLoadTask().execute(REFRESH_DATA);
    }

    @Override
    public void onLoadMore() {
        new RefreshLoadTask().execute(LOAD_MORE);
    }

    class RefreshLoadTask extends AsyncTask<Integer, Void, Integer> {
        @Override
        protected Integer doInBackground(Integer... integers) {
            int result = integers[0];
            if (integers[0] == REFRESH_DATA) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else if (integers[0] == LOAD_MORE){
                try {
                    Thread.sleep(3000);
                    for (int i = 'a'; i < 'z'; i++) {
                        mData.add((char)i + "");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (result == REFRESH_DATA) {
                mRecyclerView.stopRefresh();
            } else if (result == LOAD_MORE) {
                mRecyclerView.stopLoadMore();
            }
        }
    }
}
