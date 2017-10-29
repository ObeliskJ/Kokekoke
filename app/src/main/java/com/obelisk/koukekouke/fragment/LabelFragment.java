package com.obelisk.koukekouke.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.obelisk.koukekouke.R;
import com.obelisk.koukekouke.activity.LabelCollectionActivity;
import com.obelisk.koukekouke.base.BaseFragment;
import com.obelisk.koukekouke.bean.HotLabel;
import com.obelisk.koukekouke.common.Redirct;
import com.obelisk.koukekouke.view.FlowLayout.FlowLayout;
import com.obelisk.koukekouke.view.FlowLayout.TagAdapter;
import com.obelisk.koukekouke.view.FlowLayout.TagFlowLayout;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2016/10/12.
 */

public class LabelFragment extends BaseFragment {
    @Bind(R.id.id_fl_1)
    FrameLayout mFl1;
    @Bind(R.id.id_fl_2)
    FrameLayout mFl2;
    @Bind(R.id.id_fl_3)
    FrameLayout mFl3;
    @Bind(R.id.id_fl_4)
    FrameLayout mFl4;
    @Bind(R.id.id_flowlayout)
    TagFlowLayout mFlowlayout;
    @Bind(R.id.id_iv_1)
    ImageView mIv1;
    @Bind(R.id.id_tv_1)
    TextView mTv1;
    @Bind(R.id.id_iv_2)
    ImageView mIv2;
    @Bind(R.id.id_tv_2)
    TextView mTv2;
    @Bind(R.id.id_iv_3)
    ImageView mIv3;
    @Bind(R.id.id_tv_3)
    TextView mTv3;
    @Bind(R.id.id_iv_4)
    ImageView mIv4;
    @Bind(R.id.id_tv_4)
    TextView mTv4;

//    private HotLabelAdapter mAdapter;
    private List<HotLabel> headerList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_label, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        getHeaderLabel();
        getNormalLabel();
    }

    private void getNormalLabel() {
        final LayoutInflater mInflater = LayoutInflater.from(mContext);
        BmobQuery<HotLabel> query = new BmobQuery<HotLabel>();
        query.addWhereEqualTo("isActivation", true).addWhereEqualTo("isPic", false).setLimit(50);
        query.findObjects(new FindListener<HotLabel>() {
            @Override
            public void done(final List<HotLabel> list, BmobException e) {
                if (e == null) {
                    if (list.size() != 0 && list.get(list.size() - 1) != null) {
                        mFlowlayout.setAdapter(new TagAdapter(mContext, list) {
                            @Override
                            public View getView(FlowLayout parent, int position, Object o) {
                                TextView tv = (TextView) mInflater.inflate(R.layout.item_hot_tv, mFlowlayout, false);
                                final HotLabel mHotLabel = list.get(position);
                                tv.setText(mHotLabel.getLabelText());
                                if (mHotLabel.getIsRedLabel()) {
                                    tv.setBackgroundResource(R.drawable.hot_label_red);
                                    tv.setTextColor(mContext.getResources().getColor(R.color.white));
                                } else {
                                    tv.setBackgroundResource(R.drawable.hot_label_normal);
                                    tv.setTextColor(mContext.getResources().getColor(R.color.hot_label_text));
                                }
                                return tv;
                            }
                        });
                    } else {
                        Redirct.makeToast(mContext, "error!");
                    }
                } else {
                    Redirct.makeToast(mContext, e.toString());
                }

                mFlowlayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                    @Override
                    public boolean onTagClick(View view, int position, FlowLayout parent) {
                        HotLabel mHotLabel = list.get(position);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("label", mHotLabel);
                        Redirct.openActivity(mContext, LabelCollectionActivity.class, bundle);
                        return false;
                    }
                });
            }
        });

    }

    private void getHeaderLabel() {
        BmobQuery<HotLabel> query = new BmobQuery<HotLabel>();
        query.addWhereEqualTo("isActivation", true).addWhereEqualTo("isPic", true).setLimit(4);
        query.findObjects(new FindListener<HotLabel>() {
            @Override
            public void done(List<HotLabel> list, BmobException e) {
                if (e == null) {
                    if (list.size() != 0 && list.get(list.size() - 1) != null) {
                        setHeaderLabel(list);
                    } else {
                        Redirct.makeToast(mContext, "error!");
                    }

                } else {
                    Redirct.makeToast(mContext, e.toString());
                }

            }
        });
    }

    public void setHeaderLabel(List<HotLabel> list) {
        headerList = list;
        mTv1.setText(headerList.get(0).getLabelText());
        mTv2.setText(headerList.get(1).getLabelText());
        mTv3.setText(headerList.get(2).getLabelText());
        mTv4.setText(headerList.get(3).getLabelText());
        Glide.with(mContext).load(headerList.get(0).getLabelPic().getFileUrl()).crossFade().into(mIv1);
        Glide.with(mContext).load(headerList.get(1).getLabelPic().getFileUrl()).crossFade().into(mIv2);
        Glide.with(mContext).load(headerList.get(2).getLabelPic().getFileUrl()).crossFade().into(mIv3);
        Glide.with(mContext).load(headerList.get(3).getLabelPic().getFileUrl()).crossFade().into(mIv4);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.id_fl_1, R.id.id_fl_2, R.id.id_fl_3, R.id.id_fl_4})
    public void onClick(View view) {
        HotLabel mHotLabel = new HotLabel();
        switch (view.getId()) {
            case R.id.id_fl_1:
                mHotLabel = headerList.get(0);
                break;
            case R.id.id_fl_2:
                mHotLabel = headerList.get(1);
                break;
            case R.id.id_fl_3:
                mHotLabel = headerList.get(2);
                break;
            case R.id.id_fl_4:
                mHotLabel = headerList.get(3);
                break;
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable("label", mHotLabel);
        Redirct.openActivity(mContext, LabelCollectionActivity.class, bundle);
    }
}
