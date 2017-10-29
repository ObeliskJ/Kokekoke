package com.obelisk.koukekouke.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.obelisk.koukekouke.R;
import com.obelisk.koukekouke.base.BaseActivity;
import com.obelisk.koukekouke.bean.HotLabel;
import com.obelisk.koukekouke.bean.User;
import com.obelisk.koukekouke.common.Redirct;
import com.obelisk.koukekouke.view.FlowLayout.FlowLayout;
import com.obelisk.koukekouke.view.FlowLayout.TagAdapter;
import com.obelisk.koukekouke.view.FlowLayout.TagFlowLayout;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Jiangwz on 2016/11/19.
 */

public class MyLabelCollectionActivity extends BaseActivity {
    @Bind(R.id.id_tv_title)
    TextView mTvTitle;
    @Bind(R.id.id_flowlayout)
    TagFlowLayout mFlowlayout;
    private User user;
    private LayoutInflater mInflater;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_label_collection;
    }

    @Override
    protected void initView() {
        mInflater = LayoutInflater.from(mActivity);
        mTvTitle.setText("收藏标签");
        user = BmobUser.getCurrentUser(User.class);


    }

    @Override
    protected void initData() {
        BmobQuery<HotLabel> query = new BmobQuery<HotLabel>();
        query.addWhereRelatedTo("myLabel", new BmobPointer(user));
        query.setLimit(30);
        query.findObjects(new FindListener<HotLabel>() {
            @Override
            public void done(final List<HotLabel> list, BmobException e) {
                if(e == null){
                    if(list.size() != 0 && list.get(list.size() - 1) != null){
                        mFlowlayout.setAdapter(new TagAdapter(mActivity, list) {
                            @Override
                            public View getView(FlowLayout parent, int position, Object o) {
                                TextView tv = (TextView) mInflater.inflate(R.layout.item_hot_tv, mFlowlayout, false);
                                final HotLabel mHotLabel = list.get(position);
                                tv.setText(mHotLabel.getLabelText());
                                if(mHotLabel.getIsRedLabel()){
                                    tv.setBackgroundResource(R.drawable.hot_label_red);
                                    tv.setTextColor(mActivity.getResources().getColor(R.color.white));
                                }else{
                                    tv.setBackgroundResource(R.drawable.hot_label_normal);
                                    tv.setTextColor(mActivity.getResources().getColor(R.color.hot_label_text));
                                }
                                return tv;
                            }
                        });

                    }else {
                        Redirct.makeToast(mActivity, "暂无内容");
                    }


                }else{
                    Redirct.makeToast(mActivity, e.toString());
                }

                mFlowlayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                    @Override
                    public boolean onTagClick(View view, int position, FlowLayout parent) {
                        HotLabel mHotLabel = list.get(position);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("label", mHotLabel);
                        Redirct.openActivity(mActivity, LabelCollectionActivity.class, bundle);
                        return false;
                    }
                });

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
