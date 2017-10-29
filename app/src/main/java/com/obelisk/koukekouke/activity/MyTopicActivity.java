package com.obelisk.koukekouke.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.obelisk.koukekouke.R;
import com.obelisk.koukekouke.adapter.NewsItemAdapter;
import com.obelisk.koukekouke.base.BaseActivity;
import com.obelisk.koukekouke.bean.Koukekouke;
import com.obelisk.koukekouke.bean.User;
import com.obelisk.koukekouke.common.Redirct;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Jiangwz on 2016/11/17.
 */

public class MyTopicActivity extends BaseActivity {
    @Bind(R.id.id_tv_title)
    TextView mTvTitle;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private List<Koukekouke> data;
    private NewsItemAdapter mItemAdapter ;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_topic;
    }

    @Override
    protected void initView() {
        mTvTitle.setText("我的话题");
        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(manager);
        data = new ArrayList<Koukekouke>();
        mItemAdapter = new NewsItemAdapter(mActivity, data);
        mRecyclerView.setAdapter(mItemAdapter);
        mItemAdapter.setOnRVItemClickListener(new NewsItemAdapter.OnRVItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", data.get(position));
                Redirct.openActivity(mActivity, InfoActivity.class, bundle);
            }
        });
    }

    @Override
    protected void initData() {
        getMyData();
    }

    private void getMyData() {
        User user = BmobUser.getCurrentUser(User.class);
        BmobQuery<Koukekouke> query = new BmobQuery<Koukekouke>();
        query.addWhereEqualTo("author", user);
        query.setLimit(30);
        query.order("-createdAt");
        query.include("author,author.character");
        query.findObjects(new FindListener<Koukekouke>() {
            @Override
            public void done(List<Koukekouke> list, BmobException e) {
                if(e == null){
                    if (list.size() != 0 && list.get(list.size() - 1) != null){
                        Redirct.makeToast(mActivity, list.size() + "");
                        data.addAll(list);
                        mItemAdapter.notifyDataSetChanged();
                    }else{
                        Redirct.makeToast(mActivity, "暂无内容");
                    }
                }else {
                    Redirct.makeToast(mActivity, e.toString());
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.id_iv_left)
    public void onClick() {
        finish();
    }
}