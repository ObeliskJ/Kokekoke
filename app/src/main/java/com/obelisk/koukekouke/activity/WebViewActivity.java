package com.obelisk.koukekouke.activity;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;

import com.obelisk.koukekouke.R;
import com.obelisk.koukekouke.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jiangwz on 2016/11/28.
 */

public class WebViewActivity extends BaseActivity {
    @Bind(R.id.id_iv_left)
    ImageView idIvLeft;
    @Bind(R.id.webview)
    WebView webview;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_webview;
    }

    @Override
    protected void initView() {

        String url = getIntent().getStringExtra("url");
        webview.loadUrl(url);
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
