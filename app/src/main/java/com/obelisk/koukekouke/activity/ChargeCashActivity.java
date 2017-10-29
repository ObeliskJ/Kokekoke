package com.obelisk.koukekouke.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.obelisk.koukekouke.R;
import com.obelisk.koukekouke.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jiangwz on 2016/11/17.
 */

public class ChargeCashActivity extends BaseActivity {
    @Bind(R.id.id_tv_title)
    TextView mTvTitle;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_charge;
    }

    @Override
    protected void initView() {
        mTvTitle.setText("充值提现");
    }

    @Override
    protected void initData() {

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
