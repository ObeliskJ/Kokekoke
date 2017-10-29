package com.obelisk.koukekouke.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.obelisk.koukekouke.R;
import com.obelisk.koukekouke.base.BaseActivity;
import com.obelisk.koukekouke.guide.GuideFragment1;
import com.obelisk.koukekouke.guide.GuideFragment2;
import com.obelisk.koukekouke.guide.GuideFragment3;
import com.obelisk.koukekouke.guide.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Jiangwz on 2016/11/22.
 */

public class GuideActivity extends BaseActivity {
    @Bind(R.id.id_guide_page)
    ViewPager mViewPager;
    private GuideFragment1 mFragment1;
    private GuideFragment2 mFragment2;
    private GuideFragment3 mFragment3;
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private PagerAdapter madapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_guide;
    }

    @Override
    protected void initView() {
        mFragment1 = new GuideFragment1();
        mFragment2 = new GuideFragment2();
        mFragment3 = new GuideFragment3();
        mFragmentList.add(mFragment1);
        mFragmentList.add(mFragment2);
        mFragmentList.add(mFragment3);

        madapter = new ViewPagerAdapter(getSupportFragmentManager(), mFragmentList);
        mViewPager.setAdapter(madapter);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }
}
