package com.obelisk.koukekouke.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.obelisk.koukekouke.R;
import com.obelisk.koukekouke.base.BaseActivity;
import com.obelisk.koukekouke.bean.CharacterSelect;
import com.obelisk.koukekouke.bean.User;
import com.obelisk.koukekouke.common.Redirct;
import com.obelisk.koukekouke.view.CharacterConfirmDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Jiangwz on 2016/11/18.
 */

public class CharacterConfirmActivity extends BaseActivity {
    @Bind(R.id.id_iv_pic_character)
    ImageView mIvPicCharacter;

    private CharacterSelect mCharacter;
    private CharacterConfirmDialog dialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_character_confirm;
    }

    @Override
    protected void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            tintManager.setStatusBarTintEnabled(false);//启动页标题栏透明
        }
        mCharacter = (CharacterSelect) getIntent().getExtras().getSerializable("character");
        //Glide.with(mActivity).load(mCharacter.getBigPic().getFileUrl()).into(mIvPicCharacter);
        Glide.with(mActivity).load(mCharacter.getBigPic().getFileUrl()).into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                dialog.dismiss();
                mIvPicCharacter.setImageDrawable(resource);
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        dialog = new CharacterConfirmDialog(mActivity);
        dialog.show();
    }

    @OnClick({R.id.id_btn_reselect, R.id.id_btn_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_btn_reselect:
                finish();
                break;
            case R.id.id_btn_confirm:
                Bundle bundle = new Bundle();
                bundle.putSerializable("character", mCharacter);
                User mUser = BmobUser.getCurrentUser(User.class);
                if(mUser == null){
                    Redirct.openActivity(mActivity, RegisterActivity.class, bundle);
                    finish();
                }else{
                    User newUser = new User();
                    newUser.setCharacter(mCharacter);
                    newUser.update(mUser.getObjectId(),new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e == null){
                                Redirct.makeToast(mActivity, "变身成功了喵~");
                                finish();
                            }else{
                                Redirct.makeToast(mActivity, e.toString());
                            }
                        }
                    });
                }
                break;
        }
    }
}
