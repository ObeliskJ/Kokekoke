package com.obelisk.koukekouke.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.obelisk.koukekouke.R;
import com.obelisk.koukekouke.base.BaseActivity;
import com.obelisk.koukekouke.common.Redirct;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.push.BmobPush;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.listener.BmobUpdateListener;
import cn.bmob.v3.update.BmobUpdateAgent;
import cn.bmob.v3.update.UpdateResponse;

/**
 * Created by Jiangwz on 2016/11/8.
 */

public class SplashActivity extends BaseActivity {
    @Bind(R.id.id_iv_bg)
    ImageView mIvBg;

    private boolean isFirst;
    ScaleAnimation sa;
    AlphaAnimation aa;
    Bitmap bitmap;
    private Context mContext;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
        mContext = this;
//        Glide.with(mActivity).load(R.drawable.splash_bg).into(mIvBg);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            tintManager.setStatusBarTintEnabled(false);//启动页标题栏透明
        }
        aa = new AlphaAnimation(0.5f, 1.0f);
        aa.setDuration(2000);
        mIvBg.startAnimation(aa);

        sa =new ScaleAnimation(1.0f, 1.3f, 1.0f, 1.3f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        //如果值为true，控件则保持动画结束的状态
        sa.setFillAfter(true);
        //动画效果推迟1秒钟后启动
        sa.setStartOffset(1000);
        sa.setDuration(1500);
        mIvBg.startAnimation(sa);
        sa.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                goMainActivity();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
//        BmobUpdateAgent.update(this);//判断是否需要更新
//        BmobUpdateAgent.setUpdateListener(new BmobUpdateListener() {
//
//            @Override
//            protected Object clone() throws CloneNotSupportedException {
//                return super.clone();
//            }
//
//            @Override
//            public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
//                // TODO Auto-generated method stub
//                //根据updateStatus来判断更新是否成功
//                Log.i("update", updateStatus+ "");
//                Log.i("update", updateInfo+ "");
//            }
//        });

//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        double ratio = Math.max(options.outWidth *1.0d/1024f,options.outHeight *1.0d/1024);
//        options.inSampleSize =(int) Math.ceil(ratio);
////        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.splash_bg);
        mIvBg.setImageBitmap(bitmap);

        // 使用推送服务时的初始化操作
        BmobInstallation.getCurrentInstallation().save();
        // 启动推送服务
        BmobPush.startWork(mContext);

    }
    private void goMainActivity(){
        SharedPreferences spf = getSharedPreferences("isFirst", MODE_PRIVATE);
        isFirst = spf.getBoolean("isFirst", true);
        if(isFirst){
            Redirct.openActivity(mContext, GuideActivity.class);
            finish();
        }else{
            Redirct.openActivity(mContext, MainActivity.class);
            finish();
        }
        SharedPreferences.Editor editor = spf.edit();
        //存入数据
        editor.putBoolean("isFirst", false);
        //提交修改
        editor.commit();
       // editor.clear();
    }


    @Override
    protected void initData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @OnClick(R.id.id_fl_footer)
    public void onClick() {
        Redirct.openActivity(mContext, MainActivity.class);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        sa.cancel();
        aa.cancel();
        bitmap.recycle();
        System.gc(); // 提醒系统及时回收
    }

}
