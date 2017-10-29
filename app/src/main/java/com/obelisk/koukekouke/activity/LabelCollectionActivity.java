package com.obelisk.koukekouke.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.obelisk.koukekouke.R;
import com.obelisk.koukekouke.adapter.NewsItemAdapter;
import com.obelisk.koukekouke.base.BaseActivity;
import com.obelisk.koukekouke.bean.HotLabel;
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
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Jiangwz on 2016/11/19.
 */

public class LabelCollectionActivity extends BaseActivity {
    @Bind(R.id.id_tv_title)
    TextView mTvTitle;
    @Bind(R.id.id_iv_share)
    ImageView mIvInfoCenter;
    @Bind(R.id.id_iv_info_center)
    ImageView mIvShare;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.iv_pic)
    ImageView ivPic;
    @Bind(R.id.tv_des)
    TextView tvDes;

    private HotLabel label;
    private NewsItemAdapter mAdapter;
    private List<Koukekouke> data;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_label_collection;
    }

    @Override
    protected void initView() {
        label = (HotLabel) getIntent().getSerializableExtra("label");
        mTvTitle.setText(label.getLabelText());
        mIvInfoCenter.setBackgroundResource(R.drawable.ic_label_unlike);
        mIvShare.setBackgroundResource(R.drawable.ic_share);
        if(label.getDescribe() != null){
            tvDes.setText(label.getDescribe());
            tvDes.setVisibility(View.VISIBLE);
        }else{
            tvDes.setVisibility(View.GONE);
        }
        if(label.getHeaderPic() != null){
            Glide.with(mActivity).load(label.getHeaderPic().getFileUrl()).dontAnimate().placeholder(R.drawable.pic_loading).into(ivPic);
        }else{
            ivPic.setVisibility(View.GONE);
        }

        data = new ArrayList<Koukekouke>();
        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        recyclerView.setLayoutManager(manager);
        mAdapter = new NewsItemAdapter(mActivity, data);
        recyclerView.setAdapter(mAdapter);

        mIvShare.setVisibility(View.INVISIBLE);

        if (isLogin()) {
            User user = BmobUser.getCurrentUser(User.class);
            BmobQuery<HotLabel> query = new BmobQuery<HotLabel>();
            query.addWhereRelatedTo("myLabel", new BmobPointer(user));
            query.addWhereEqualTo("LabelText", label.getLabelText());
            query.findObjects(new FindListener<HotLabel>() {
                @Override
                public void done(List<HotLabel> list, BmobException e) {
                    if (e == null && list.size() != 0) {
                        mIvInfoCenter.setBackgroundResource(R.drawable.ic_label_like);
                    } else {
                        mIvInfoCenter.setBackgroundResource(R.drawable.ic_label_unlike);
                    }
                }
            });
        } else {
            mIvInfoCenter.setBackgroundResource(R.drawable.ic_label_unlike);
        }


    }

    @Override
    protected void initData() {
        BmobQuery<Koukekouke> query = new BmobQuery<Koukekouke>();
        query.addWhereEqualTo("label", "#" + label.getLabelText());
        query.setLimit(30);
        query.order("-createdAt");
        query.include("author,author.character");
        query.findObjects(new FindListener<Koukekouke>() {
            @Override
            public void done(List<Koukekouke> list, BmobException e) {
                if (e == null) {
                    if (list.size() != 0 && list.get(list.size() - 1) != null) {
                        data.addAll(list);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        Redirct.makeToast(mActivity, "暂无数据 喵~");
                    }
                } else {
                    Redirct.makeToast(mActivity, e.toString());
                }

            }
        });
        mAdapter.setOnRVItemClickListener(new NewsItemAdapter.OnRVItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", data.get(position));
                Redirct.openActivity(mActivity, InfoActivity.class, bundle);
            }
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.id_iv_left, R.id.id_iv_share, R.id.id_iv_info_center})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_iv_left:
                finish();
                break;
//            case R.id.id_iv_share:
//
//                break;
            //收藏标签
            case R.id.id_iv_share:
                if (!isLogin()) {
                    Redirct.makeToast(mActivity, "收藏前请先登录 喵~");
                    Redirct.openActivity(mActivity, LoginActivity.class);
                } else {
                    User user = BmobUser.getCurrentUser(User.class);
                    BmobRelation relation = new BmobRelation();
                    relation.add(label);
                    user.setMyLabel(relation);
                    user.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Redirct.makeToast(mActivity, "收藏成功 喵~");
                                mIvInfoCenter.setBackgroundResource(R.drawable.ic_label_like);
                            } else {
                                Redirct.makeToast(mActivity, "收藏失败" + e.toString());
                            }
                        }
                    });
                }
                break;
        }
    }
}
