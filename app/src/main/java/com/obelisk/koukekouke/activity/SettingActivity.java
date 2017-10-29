package com.obelisk.koukekouke.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;
import com.obelisk.koukekouke.R;
import com.obelisk.koukekouke.base.BaseActivity;
import com.obelisk.koukekouke.common.Redirct;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jiangwz on 2016/11/8.
 */

public class SettingActivity extends BaseActivity {
    @Bind(R.id.id_iv_left)
    ImageView mIvLeft;
    @Bind(R.id.id_tv_title)
    TextView mTvTitle;
    @Bind(R.id.id_ll_advice)
    LinearLayout mLlAdvice;
    @Bind(R.id.id_ll_joinus)
    LinearLayout mLlJoinus;
    @Bind(R.id.id_ll_aboutus)
    LinearLayout mLlAboutus;
    @Bind(R.id.id_iv_msg)
    ImageView iv_msg;
    @Bind(R.id.tv_about_us)
    TextView tv_about_us;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        mTvTitle.setText("系统设置");
        LinkBuilder.on(tv_about_us)
                .addLinks(getExampleLinks())
                .build();

    }

    @Override
    protected void initData() {

    }

    private List<Link> getExampleLinks() {
        List<Link> links = new ArrayList<>();

        Link kokekoke = new Link("www.kokekoke.cn");
        kokekoke.setTextColor(Color.parseColor("#F77878"));
        kokekoke.setUnderlined(false)
                .setOnClickListener(new Link.OnClickListener() {
                    @Override
                    public void onClick(String clickedText) {
                        Bundle bundle = new Bundle();
                        bundle.putString("url", "http://www.kokekoke.cn");
                        Redirct.openActivity(mActivity, WebViewActivity.class, bundle);

                    }
                });

        links.add(kokekoke);
        return links;



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.id_iv_left, R.id.id_ll_advice, R.id.id_ll_joinus, R.id.id_iv_msg})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_iv_left:
                finish();
                break;
            //反馈
            case R.id.id_ll_advice:
                Redirct.openActivity(mActivity, FeedbackActivity.class);
                break;
            case R.id.id_ll_joinus:
                Bundle bundle = new Bundle();
                bundle.putString("url", "http://www.kokekoke.cn/join.html");
                Redirct.openActivity(mActivity, WebViewActivity.class, bundle);
                break;
            case R.id.id_iv_msg:
                if(iv_msg.getBackground().equals(R.drawable.ic_switch_on)){
                    iv_msg.setBackgroundResource(R.drawable.ic_switch_off);
                }else{
                    iv_msg.setBackgroundResource(R.drawable.ic_switch_on);
                }
                break;
        }
    }
}
