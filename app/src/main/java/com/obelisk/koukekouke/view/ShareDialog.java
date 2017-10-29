package com.obelisk.koukekouke.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.obelisk.koukekouke.R;
import com.obelisk.koukekouke.common.Constant;

/**
 * Created by Administrator on 2016/5/31.
 */
public class ShareDialog extends Dialog {
    LinearLayout ll_friend, ll_qq, ll_qzone, ll_weibo, ll_wechat, ll_save;
    private OnButtonClickListener listener;
    public interface OnButtonClickListener {
        public void setPlatform(int what);
    }

    public ShareDialog(Context context, OnButtonClickListener listener) {
        super(context, R.style.CustomStyle);
        setContentView(R.layout.dialog_share);
        this.listener = listener;
        initView();

    }

    private void initView() {
        ll_friend = (LinearLayout)findViewById(R.id.id_ll_friend);
        ll_qq = (LinearLayout)findViewById(R.id.id_ll_qq);
        ll_qzone = (LinearLayout)findViewById(R.id.id_ll_qzone);
        ll_weibo = (LinearLayout)findViewById(R.id.id_ll_weibo);
        ll_wechat = (LinearLayout)findViewById(R.id.id_ll_wechat);
        ll_save = (LinearLayout)findViewById(R.id.id_ll_save);
        ll_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.setPlatform(Constant.FRIEND);
            }
        });
        ll_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.setPlatform(Constant.QQ);
            }
        });
        ll_qzone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.setPlatform(Constant.QZONE);
            }
        });
        ll_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.setPlatform(Constant.SAVE);
            }
        });
        ll_weibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.setPlatform(Constant.WEIBO);
                dismiss();
            }
        });
        ll_wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.setPlatform(Constant.WECHAT);
            }
        });
    }

    @Override
    public void show() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;  //DensityUtil.srceenWidth(getContext()); //设置宽度//
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity= Gravity.BOTTOM;
        getWindow().setAttributes(params);
        super.show();


    }
}
