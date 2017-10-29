package com.obelisk.koukekouke.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.obelisk.koukekouke.R;
import com.obelisk.koukekouke.activity.InfoActivity;
import com.obelisk.koukekouke.adapter.NewsItemAdapter;
import com.obelisk.koukekouke.base.BaseFragment;
import com.obelisk.koukekouke.bean.Koukekouke;
import com.obelisk.koukekouke.common.Constant;
import com.obelisk.koukekouke.common.Redirct;
import com.obelisk.koukekouke.view.GoogleProgressBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Jiangwz on 2016/10/12.
 */

public class HotFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.news_recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.news_swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.gpb)
    GoogleProgressBar progress;


    private NewsItemAdapter mNewsItemAdapter ;
    List<Koukekouke> data ;
    private int lastVisibleItem;
    private int curPage = 0;		// 当前页的编号，从0开始

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setupBaseView();
        progress.setVisibility(View.VISIBLE);
    }

    private void setupBaseView() {
        data = new ArrayList<Koukekouke>();
        mNewsItemAdapter = new NewsItemAdapter(mContext, data);
        LinearLayoutManager manager = new LinearLayoutManager(mRecyclerView.getContext());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mNewsItemAdapter);
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        mSwipeRefreshLayout.setColorSchemeColors(R.color.colorPrimary, R.color.colorPrimaryDark);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mNewsItemAdapter.setOnRVItemClickListener(new NewsItemAdapter.OnRVItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", data.get(position));
                Redirct.openActivity(mContext, InfoActivity.class, bundle);
            }
        });
        getData(0, Constant.STATE_REFRESH);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news, null);
        return rootView;
    }

    public void getData(int page, final int actionType){
        BmobQuery<Koukekouke> query1 = new BmobQuery<Koukekouke>();
        BmobQuery<Koukekouke> query2 = new BmobQuery<Koukekouke>();
        query1.addWhereEqualTo("viewType", "1");
        query2.addWhereEqualTo("viewType", "2");
        BmobQuery<Koukekouke> query = new BmobQuery<Koukekouke>();
        List<BmobQuery<Koukekouke>> orQuery = new ArrayList<BmobQuery<Koukekouke>>();
        orQuery.add(query1);
        orQuery.add(query2);
        query.or(orQuery);
        if(actionType == Constant.STATE_MORE){
            query.setSkip(page * Constant.limit);
        }else{
            curPage = 0;
            query.setSkip(page * Constant.limit);
        }
        query.setLimit(Constant.limit);
        query.order("-numOfLike");
        query.include("author,author.character");
        query.findObjects(new FindListener<Koukekouke>() {
            @Override
            public void done(List<Koukekouke> list, BmobException e) {
                if(e == null){
                    progress.setVisibility(View.GONE);
                    if(actionType == Constant.STATE_MORE){
                        if (list.size() != 0 && list.get(list.size() - 1) != null) {

                            data.addAll(list);
                            mNewsItemAdapter.notifyDataSetChanged();
                        }else{
                            Redirct.makeToast(mContext, "没有更多了~");
                        }
                        mSwipeRefreshLayout.setRefreshing(false);
                    }else{
                        if (list.size() != 0 && list.get(list.size() - 1) != null) {
                            data.clear();
                            data.addAll(list);
                            mNewsItemAdapter.notifyDataSetChanged();
                        }else{
                            Redirct.makeToast(mContext, "no data");
                        }
                        mSwipeRefreshLayout.setRefreshing(false);

                    }
                }else{
                    progress.setVisibility(View.GONE);
                    Redirct.makeToast(mContext, e.toString());
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });

    }

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            lastVisibleItem = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastVisibleItemPosition();
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisibleItem + 1 == mNewsItemAdapter.getItemCount()) {
                mSwipeRefreshLayout.setRefreshing(true);
                curPage++;
                getData(curPage, Constant.STATE_MORE);
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        getData(0, Constant.STATE_REFRESH);
    }
}
