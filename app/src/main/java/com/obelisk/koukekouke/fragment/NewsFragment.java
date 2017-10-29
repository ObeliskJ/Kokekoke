package com.obelisk.koukekouke.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.obelisk.koukekouke.R;
import com.obelisk.koukekouke.activity.InfoActivity;
import com.obelisk.koukekouke.activity.WebViewActivity;
import com.obelisk.koukekouke.adapter.NewsItemAdapter;
import com.obelisk.koukekouke.base.BaseFragment;
import com.obelisk.koukekouke.bean.Comment;
import com.obelisk.koukekouke.bean.Koukekouke;
import com.obelisk.koukekouke.bean.User;
import com.obelisk.koukekouke.common.Constant;
import com.obelisk.koukekouke.common.Redirct;
import com.obelisk.koukekouke.view.GoogleProgressBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2016/10/12.
 */

public class NewsFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

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

    private Koukekouke headerData;

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
        mSwipeRefreshLayout.setDistanceToTriggerSync(48);
       // mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.splash_footer));
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.splash_footer));

        mNewsItemAdapter.setOnRVItemClickListener(new NewsItemAdapter.OnRVItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(position == 0){
//                    Bundle bundle = new Bundle();
//                    bundle.putString("url", data.get(position).getContent());
//                    Redirct.openActivity(mContext, WebViewActivity.class, bundle);
                    Uri uri = Uri.parse(data.get(position).getContent());
                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                    startActivity(intent);
                }else{
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", data.get(position));
                    Redirct.openActivity(mContext, InfoActivity.class, bundle);
                }

            }
        });
        getHeader();
      //  haha();  //必杀方法
    }

    private void getHeader() {
        BmobQuery<Koukekouke> query = new BmobQuery<Koukekouke>();
        query.setLimit(1);
        query.addWhereEqualTo("viewType", "0");
        query.findObjects(new FindListener<Koukekouke>() {
            @Override
            public void done(List<Koukekouke> list, BmobException e) {
                if(e == null){
                    if (list.size() != 0 && list.get(list.size() - 1) != null) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        headerData = list.get(0);
                        data.addAll(list);
                        mNewsItemAdapter.notifyDataSetChanged();
                    }else{
                       // Redirct.makeToast(mContext, "");
                    }
                    //先加载头部
                    getData(0, Constant.STATE_REFRESH);

                }else{
                    Redirct.makeToast(mContext, e.toString());
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news, null);
        return rootView;
    }

    public void getData(int page, final int actionType){
        BmobQuery<Koukekouke> query = new BmobQuery<Koukekouke>();
        query.addWhereNotEqualTo("viewType", "0");
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
                if(e == null){
                    progress.setVisibility(View.GONE);
                    if(actionType == Constant.STATE_MORE){
                        if (list.size() != 0 && list.get(list.size() - 1) != null) {
                            data.addAll(list);
                            mNewsItemAdapter.notifyDataSetChanged();
                        }else{
                            Redirct.makeToast(mContext, "没有更多了");
                        }
                        mSwipeRefreshLayout.setRefreshing(false);
                    }else{
                        if (list.size() != 0 && list.get(list.size() - 1) != null) {
                            data.clear();
                            data.add(headerData);
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

    public void haha(){
        final User user = BmobUser.getCurrentUser(User.class);
        BmobQuery<Koukekouke> query = new BmobQuery<Koukekouke>();
        query.setLimit(500);
        query.include("author,author.character");
        query.addWhereNotEqualTo("viewType", "0");
        query.findObjects(new FindListener<Koukekouke>() {
            @Override
            public void done(List<Koukekouke> list, BmobException e) {
                if(e == null){
                    for(int i = 0 ; i <= list.size(); i++){
                        final Koukekouke data = list.get(i);
                        if(data.getAuthor().getCharacter() == null){
                           data.setAuthor(user);
                            data.update(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if(e == null){
                                        Redirct.makeToast(getActivity(), "ok");
                                    }
                                }
                            });
                        }
//                        if(data.getViewType() == null){
//                            data.setViewType("1");
//                        }
//                            data.update(new UpdateListener() {
//                                @Override
//                                public void done(BmobException e) {
//
//                                }
//                            });
//                        BmobQuery<Comment> cQuery  = new BmobQuery<Comment>();
//                        cQuery.addWhereRelatedTo("comment", new BmobPointer(data));
//                        cQuery.findObjects(new FindListener<Comment>() {
//                            @Override
//                            public void done(List<Comment> list, BmobException e) {
//                                Integer num =  list.size();
//                                data.setNumOfComment(num);
//                                data.update(new UpdateListener() {
//                                    @Override
//                                    public void done(BmobException e) {
//                                        if(e == null){
//                                            Redirct.makeToast(getActivity(), "OK");
//                                        }
//
//                                    }
//                                });
//                            }
//                        });
                    }

                }else{
                    Redirct.makeToast(mContext, e.toString());
                }

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
      //  onRefresh();
    }
}
