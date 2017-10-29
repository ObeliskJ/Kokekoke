package com.obelisk.koukekouke.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.obelisk.koukekouke.R;
import com.obelisk.koukekouke.base.BaseActivity;
import com.obelisk.koukekouke.bean.Feedback;
import com.obelisk.koukekouke.bean.User;
import com.obelisk.koukekouke.common.Redirct;
import com.obelisk.koukekouke.view.CharacterConfirmDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Jiangwz on 2016/11/19.
 */

public class FeedbackActivity extends BaseActivity {
    @Bind(R.id.id_tv_title)
    TextView mTvTitle;
    @Bind(R.id.et_feedback)
    EditText etFeedback;
    @Bind(R.id.et_contact)
    EditText etContact;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.id_iv_left)
    ImageView idIvLeft;

    private String feedback = null;
    private String contact = null;
    private CharacterConfirmDialog dialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void initView() {
        mTvTitle.setText("反馈");
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.id_iv_left, R.id.fab})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_iv_left:
                finish();
                break;
            case R.id.fab:
                feedback = etFeedback.getText().toString();
                contact = etContact.getText().toString();
                if(feedback.equals("")){
                    Redirct.makeToast(mActivity, "请填写反馈内容哦 喵~");
                    return;
                }
                if(contact.equals("")){
                    Redirct.makeToast(mActivity, "请留下联系方式哦 喵~");
                    return;
                }
                dialog = new CharacterConfirmDialog(mActivity, "提交中...");
                dialog.show();
                User user = BmobUser.getCurrentUser(User.class);
                Feedback fb = new Feedback();
                fb.setContent(feedback);
                fb.setUser(user);
                fb.setContact(contact);
                fb.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if(e == null){
                            dialog.dismiss();
                            Redirct.makeToast(mActivity, "反馈成功，喵~");
                            finish();
                        }else{
                            dialog.dismiss();
                            Redirct.makeToast(mActivity, "网络有异常，喵~");
                        }
                    }
                });
                break;
        }
    }
}
