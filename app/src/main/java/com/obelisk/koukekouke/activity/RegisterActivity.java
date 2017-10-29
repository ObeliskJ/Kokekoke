package com.obelisk.koukekouke.activity;

import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

import com.obelisk.koukekouke.R;
import com.obelisk.koukekouke.base.BaseActivity;
import com.obelisk.koukekouke.bean.CharacterSelect;
import com.obelisk.koukekouke.bean.User;
import com.obelisk.koukekouke.common.Redirct;
import com.obelisk.koukekouke.view.CharacterConfirmDialog;
import com.obelisk.koukekouke.view.DeletableEditText;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Jiangwz on 2016/10/24.
 */

public class RegisterActivity extends BaseActivity {
    @Bind(R.id.id_tv_account)
    DeletableEditText mTvAccount;
    @Bind(R.id.id_tv_pwd)
    DeletableEditText mTvPwd;
    @Bind(R.id.id_tv_mail)
    DeletableEditText mTvMail;
    @Bind(R.id.id_btn_register)
    Button mBtnRegister;
    private CharacterConfirmDialog dialog;

    private String account;
    private String pwd;
    private String email;
    private CharacterSelect character;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        character = (CharacterSelect) getIntent().getSerializableExtra("character");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            tintManager.setStatusBarTintEnabled(false);//启动页标题栏透明
        }

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick(R.id.id_btn_register)
    public void onClick() {
        account = mTvAccount.getText().toString().trim();
        pwd = mTvPwd.getText().toString().trim();
        email = mTvMail.getText().toString().trim();
        if(account == null){
            mTvAccount.setShakeAnimation();
            return;
        }else if(pwd == null){
            mTvPwd.setShakeAnimation();
            return;
        }else if(email == null){
            mTvMail.setShakeAnimation();
            return;
        }
        dialog = new CharacterConfirmDialog(mActivity, "注册中...");
        dialog.dismiss();
        final User mUser = new User();
        mUser.setCharacter(character);
        mUser.setUsername(account);
        mUser.setPassword(pwd);
        mUser.setEmail(email);
        mUser.signUp(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if(e == null){
                   // Redirct.makeToast(mActivity, "注册成功 喵~");
                    mUser.login(new SaveListener<User>() {
                        @Override
                        public void done(User user, BmobException e) {
                            if(e == null){
                                dialog.dismiss();
                                Redirct.makeToast(mActivity, "登陆成功喵~");
                                finish();
                            }else{
                                dialog.dismiss();
                                Redirct.makeToast(mActivity, e.toString() + " 喵~");
                            }

                        }
                    });
                    finish();
                }else{
                    if(e.getErrorCode() == 301)
                    Redirct.makeToast(mActivity, "邮箱格式不正确 喵~");
                    else Redirct.makeToast(mActivity, e.getMessage());
                }
            }
        });


    }
}
