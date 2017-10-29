package com.obelisk.koukekouke.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.obelisk.koukekouke.R;
import com.obelisk.koukekouke.activity.InfoActivity;
import com.obelisk.koukekouke.adapter.GalleryAdapter;
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
 * Created by Administrator on 2016/10/12.
 */

public class GalleryFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {


    @Bind(R.id.news_recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.news_swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.gpb)
    GoogleProgressBar progress;

    private GalleryAdapter mAdapter;
    private List<Koukekouke> data;
    private int lastVisibleItem;
    private int curPage = 0;		// 当前页的编号，从0开始


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setupBaseView();
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gallery, null);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    public void setupBaseView() {
        data = new ArrayList<Koukekouke>();
        mAdapter = new GalleryAdapter(mContext, data);
        GridLayoutManager manager = new GridLayoutManager(mContext, 2);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        mSwipeRefreshLayout.setColorSchemeColors(R.color.colorPrimary, R.color.colorPrimaryDark);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        getData(0, Constant.STATE_REFRESH);
        mAdapter.setListener(new GalleryAdapter.OnRVItemClickListener() {
            @Override
            public void onItemClisk(View view, int positon) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", data.get(positon));
                Redirct.openActivity(mContext, InfoActivity.class, bundle);
            }
        });


    }

    private void getData(int page, final int actionType) {
        BmobQuery<Koukekouke> query = new BmobQuery<Koukekouke>();
        query.addWhereEqualTo("viewType", "2");
        query.order("-createdAt");
        query.include("author,author.character");
        if(actionType == Constant.STATE_MORE){
            query.setSkip(page * Constant.limit);
        }else{
            curPage = 0;
            query.setSkip(page * Constant.limit);
        }
        query.setLimit(Constant.limit);
        query.findObjects(new FindListener<Koukekouke>() {
            @Override
            public void done(List<Koukekouke> list, BmobException e) {
                if(null == e){
                    progress.setVisibility(View.GONE);
                    if(actionType == Constant.STATE_MORE){
                        if (list.size() != 0 && list.get(list.size() - 1) != null) {
                            data.addAll(list);
                            mAdapter.notifyDataSetChanged();
                        }else{
                            Redirct.makeToast(mContext, "没有更多了~");
                        }
                        mSwipeRefreshLayout.setRefreshing(false);
                    }else{
                        if (list.size() != 0 && list.get(list.size() - 1) != null) {
                            data.clear();
                            data.addAll(list);
                            mAdapter.notifyDataSetChanged();
                        }else{
                            Redirct.makeToast(mContext, "no data");
                        }
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                    /*if (list.size() != 0 && list.get(list.size() - 1) != null){
                        data.addAll(list);
                        mAdapter.notifyDataSetChanged();
                    }else{
                        Redirct.makeToast(mContext, "no data");
                    }*/
                }else {
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
                    && lastVisibleItem + 1 == mAdapter.getItemCount()) {
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
