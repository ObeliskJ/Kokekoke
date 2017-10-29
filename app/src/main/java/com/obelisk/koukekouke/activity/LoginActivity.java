package com.obelisk.koukekouke.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.obelisk.koukekouke.R;
import com.obelisk.koukekouke.base.BaseActivity;
import com.obelisk.koukekouke.bean.CharacterSelect;
import com.obelisk.koukekouke.bean.User;
import com.obelisk.koukekouke.common.Redirct;
import com.obelisk.koukekouke.view.CharacterConfirmDialog;
import com.obelisk.koukekouke.view.DeletableEditText;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2016/10/24.
 */

public class LoginActivity extends BaseActivity {
    @Bind(R.id.id_tv_account)
    DeletableEditText mTvAccount;
    @Bind(R.id.id_tv_pwd)
    DeletableEditText mTvPwd;
    @Bind(R.id.id_ll_login)
    LinearLayout mLlLogin;
    @Bind(R.id.id_tv_login)
    TextView mTvLogin;
    @Bind(R.id.id_iv_qq)
    ImageView mIvQq;
    @Bind(R.id.id_iv_weibo)
    ImageView mIvWeibo;

    private CharacterConfirmDialog dialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    protected void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            tintManager.setStatusBarTintEnabled(false);//启动页标题栏透明
        }
    }

    @Override
    protected void initData() {

    }



    @OnClick({R.id.id_tv_register, R.id.id_ll_login, R.id.id_iv_qq, R.id.id_iv_weibo})
    public void onClick(View view) {
        switch (view.getId()) {
            //注册
            case R.id.id_tv_register:
//                Redirct.openActivity(mActivity, RegisterActivity.class);
                Redirct.openActivity(mActivity, CharacterSelectActivity.class);
                break;
            //登录
            case R.id.id_ll_login:
                doLogin();
                break;
            //QQ登录
            case R.id.id_iv_qq:
                loginByShare(SHARE_MEDIA.QQ);
                break;
            //微博登录
            case R.id.id_iv_weibo:
                loginByShare(SHARE_MEDIA.SINA);
                break;
        }
    }

    private void doLogin() {
        if(!mTvAccount.getText().toString().trim().equals("") && !mTvPwd.getText().toString().trim().equals("")){
            dialog = new CharacterConfirmDialog(mActivity, "登陆中...");
            dialog.show();
            User user = new User();
            user.setUsername(mTvAccount.getText().toString().trim());
            user.setPassword(mTvPwd.getText().toString().trim());
            user.login(new SaveListener<Object>() {
                @Override
                public void done(Object o, BmobException e) {
                    if(e == null){
                        dialog.dismiss();
                        Redirct.makeToast(mActivity, "登陆成功喵~");
                        finish();
                    }else{
                        Redirct.makeToast(mActivity, e.toString() + " 喵~");
                        dialog.dismiss();
                    }

                }
            });
        }else{
            Redirct.makeToast(mActivity, "用户名或密码不能是空的喵~");

        }
    }

    private void loginByShare(SHARE_MEDIA type){
        UMShareAPI  mShareAPI = UMShareAPI.get(mActivity);
        mShareAPI.doOauthVerify(mActivity, type, umAuthListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
           // Toast.makeText(getApplicationContext(), "Authorize succeed", Toast.LENGTH_SHORT).show();
//            StringBuilder sb = new StringBuilder();
//            Set<String> keys = data.keySet();
//            for(String key : keys){
//                sb.append(key+"="+data.get(key).toString()+"\r\n");
//            }
//            Toast.makeText(mActivity, "登陆信息: \n"+sb.toString(), Toast.LENGTH_LONG).show();
           // loginWithAuth()
//            Intent intent = new Intent();
//            intent.setClass(mActivity, )
//            startActivityForResult(intent, 01);
            String snsType = null;
            if(platform.toString().equals("SINA")){
                snsType = "weibo";
            }else{
                snsType = "qq";
            }
            String expiresIn = data.get("expires_in");
            String userId = data.get("uid");
            String accessToken = data.get("access_token");
            BmobUser.BmobThirdUserAuth authInfo = new BmobUser.BmobThirdUserAuth(snsType, accessToken, expiresIn, userId);
            loginWithAuth(authInfo);
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText( getApplicationContext(), "Authorize fail", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText( getApplicationContext(), "Authorize cancel", Toast.LENGTH_SHORT).show();
        }
    };


    public void loginWithAuth(final BmobUser.BmobThirdUserAuth authInfo){
            BmobUser.loginWithAuthData(authInfo, new LogInListener<JSONObject>() {
                @Override
                public void done(JSONObject jsonObject, BmobException e) {
                    Log.i("json", jsonObject.toString());
                    BmobQuery<CharacterSelect> query = new BmobQuery<CharacterSelect>();
                    query.setLimit(20);
                    query.findObjects(new FindListener<CharacterSelect>() {
                        @Override
                        public void done(List<CharacterSelect> list, BmobException e) {
                            int pos = (int)(1 + Math.random() * (19 - 0 + 1));
                            User user = BmobUser.getCurrentUser(User.class);
                            User newUser = new User();
                            newUser.setCharacter(list.get(pos));
                            newUser.update(user.getObjectId(), new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    Redirct.makeToast(mActivity, "初始角色随机分配，您可以在左侧抽屉中选择变身");
                                    finish();
                                }
                            });

                        }
                    });

                }
            });
    }
}
