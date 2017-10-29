package com.obelisk.koukekouke.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.obelisk.koukekouke.R;
import com.obelisk.koukekouke.adapter.CharacterSelectAdapter;
import com.obelisk.koukekouke.base.BaseActivity;
import com.obelisk.koukekouke.bean.CharacterSelect;
import com.obelisk.koukekouke.common.Redirct;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Jiangwz on 2016/10/24.
 */

public class CharacterSelectActivity extends BaseActivity {
    @Bind(R.id.rv_character_select)
    RecyclerView mRecyclerView;
    @Bind(R.id.id_tv_title)
    TextView mTvTitle;
    @Bind(R.id.tv_1)
    TextView tv1;
    @Bind(R.id.tv_2)
    TextView tv2;
    @Bind(R.id.tv_3)
    TextView tv3;
    @Bind(R.id.tv_4)
    TextView tv4;
    @Bind(R.id.tv_5)
    TextView tv5;


    private CharacterSelectAdapter mAdapter;
    private List<CharacterSelect> data;
    //    private CharacterSelect bean;
    ///1、Servant   2、 場所  3、星座   4、何歳  5、仕事
    private String type = "1";

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_character_select;
    }


    @Override
    protected void initView() {
        mTvTitle.setText("选择喜爱的角色");
        data = new ArrayList<CharacterSelect>();
        mAdapter = new CharacterSelectAdapter(mActivity, data);
        GridLayoutManager layout = new GridLayoutManager(this, 4);
        mRecyclerView.setLayoutManager(layout);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new CharacterSelectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("character", data.get(position));
                Redirct.openActivity(mActivity, CharacterConfirmActivity.class, bundle);
                finish();
            }
        });
    }


    private void clearFoot() {
        tv1.setTextColor(getResources().getColor(R.color.text_unselect));
        tv2.setTextColor(getResources().getColor(R.color.text_unselect));
        tv3.setTextColor(getResources().getColor(R.color.text_unselect));
        tv4.setTextColor(getResources().getColor(R.color.text_unselect));
        tv5.setTextColor(getResources().getColor(R.color.text_unselect));
    }

    @Override
    protected void initData() {
        BmobQuery<CharacterSelect> query = new BmobQuery<CharacterSelect>();
        query.addWhereEqualTo("type", type);
        query.order("createdAt,viewType").setLimit(100);
        query.findObjects(new FindListener<CharacterSelect>() {
            @Override
            public void done(List<CharacterSelect> list, BmobException e) {
                if (e == null) {
                    if (list.size() != 0 && list.get(list.size() - 1) != null) {
                        data.clear();
                        data.addAll(list);
                        mAdapter.notifyDataSetChanged();
                    } else {
                    }

                } else {
                    Redirct.makeToast(mActivity, e.toString());
                }

            }
        });

    }

    @OnClick(R.id.id_iv_left)
    public void onClick() {
        finish();
    }

    @OnClick({R.id.tv_1, R.id.tv_2, R.id.tv_3, R.id.tv_4, R.id.tv_5})
    public void onClick(View view) {
        clearFoot();
        switch (view.getId()) {
            case R.id.tv_1:
                tv1.setTextColor(getResources().getColor(R.color.white));
                type = "1";
                break;
            case R.id.tv_2:
                tv2.setTextColor(getResources().getColor(R.color.white));
                type = "2";
                break;
            case R.id.tv_3:
                tv3.setTextColor(getResources().getColor(R.color.white));
                type = "3";
                break;
            case R.id.tv_4:
                tv4.setTextColor(getResources().getColor(R.color.white));
                type = "4";
                break;
            case R.id.tv_5:
                tv5.setTextColor(getResources().getColor(R.color.white));
                type = "5";
                break;
        }
        initData();
    }

    @OnClick(R.id.id_tv_add)
    public void onAddClick() {
        Redirct.openActivity(mActivity, FeedbackActivity.class);
    }
}
