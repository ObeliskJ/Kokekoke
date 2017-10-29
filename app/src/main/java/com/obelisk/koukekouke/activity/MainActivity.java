package com.obelisk.koukekouke.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.obelisk.koukekouke.MyApplication;
import com.obelisk.koukekouke.R;
import com.obelisk.koukekouke.adapter.MainFragmentPagerAdapter;
import com.obelisk.koukekouke.base.BaseActivity;
import com.obelisk.koukekouke.bean.User;
import com.obelisk.koukekouke.common.Constant;
import com.obelisk.koukekouke.common.Redirct;
import com.obelisk.koukekouke.fragment.GalleryFragment;
import com.obelisk.koukekouke.fragment.HotFragment;
import com.obelisk.koukekouke.fragment.LabelFragment;
import com.obelisk.koukekouke.fragment.NewsFragment;
import com.obelisk.koukekouke.utils.ScreenshotUtil;
import com.obelisk.koukekouke.view.LogoutDialog;
import com.obelisk.koukekouke.view.ShareDialog;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.BmobUpdateListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.update.BmobUpdateAgent;
import cn.bmob.v3.update.UpdateResponse;


public class MainActivity extends BaseActivity implements View.OnClickListener {


    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.tabs)
    TabLayout mTabs;
    @Bind(R.id.app_bar)
    AppBarLayout mAppBar;
    @Bind(R.id.viewpager)
    ViewPager mViewpager;
    @Bind(R.id.fab)
    FloatingActionButton mFab;
    @Bind(R.id.main_content)
    CoordinatorLayout mMainContent;
    @Bind(R.id.id_drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.id_navi)
    NavigationView mNavigationView;

    TextView tv_name, tv_money;
    ImageView iv_avatar;

    private MainFragmentPagerAdapter mPagerAdapter;
    private GalleryFragment mGalleryFragment;
    private HotFragment mHotFragment;
    private LabelFragment mLabelFragment;
    private NewsFragment mNewsFragment;
    public Context mContext;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initView() {
        mContext = this;
        initNaviView();
        setSupportActionBar(mToolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setTitle("首页");
        ab.setHomeAsUpIndicator(R.mipmap.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);


        if (mNavigationView != null) {
            setupDrawerContent(mNavigationView);
        }

        if (mViewpager != null) {
            setupViewPager();
        }
        mTabs.setupWithViewPager(mViewpager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User mUser = BmobUser.getCurrentUser(User.class);
                if(mUser == null){
                    Redirct.makeToast(mContext, "评论前请先登录~");
                    Redirct.openActivity(mContext, LoginActivity.class);
                }else{
                    Redirct.openActivity(mContext, EditContentActivity.class);
                }

            }
        });
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    protected void initData() {

    }



    private void initNaviView() {
        View naviView = mNavigationView.inflateHeaderView(R.layout.navi_layout);
        iv_avatar = (ImageView) naviView.findViewById(R.id.id_iv_avatar);
        ImageView iv_trans = (ImageView) naviView.findViewById(R.id.id_iv_trans);
        LinearLayout ll_index = (LinearLayout) naviView.findViewById(R.id.id_ll_index);
        LinearLayout ll_topic = (LinearLayout) naviView.findViewById(R.id.id_ll_topic);
        LinearLayout ll_label = (LinearLayout) naviView.findViewById(R.id.id_ll_label);
        LinearLayout ll_charge = (LinearLayout) naviView.findViewById(R.id.id_ll_charge);
        LinearLayout ll_setting = (LinearLayout) naviView.findViewById(R.id.id_ll_setting);
        LinearLayout ll_logout  = (LinearLayout) naviView.findViewById(R.id.id_ll_logout);
        tv_name = (TextView) naviView.findViewById(R.id.id_tv_name);
        tv_money = (TextView) naviView.findViewById(R.id.id_tv_money);

        iv_avatar.setOnClickListener(this);
        iv_trans.setOnClickListener(this);
        ll_index.setOnClickListener(this);
        ll_topic.setOnClickListener(this);
        ll_label.setOnClickListener(this);
        ll_charge.setOnClickListener(this);
        ll_setting.setOnClickListener(this);
        ll_logout.setOnClickListener(this);

        setViewData();
    }

    private void setViewData() {
        if(isLogin()){
            User user = BmobUser.getCurrentUser(User.class);
            BmobQuery<User> query = new BmobQuery<User>();
            query.include("character");
            query.getObject(user.getObjectId(), new QueryListener<User>() {
                @Override
                public void done(User user, BmobException e) {
                    if(e == null){
                        if(user != null){
                            tv_name.setText(user.getCharacter().getName());
                            Glide.with(mContext).load(user.getCharacter().getAvater().getFileUrl()).into(iv_avatar);
                        }else{
                            tv_name.setText("口可口可");
                            Glide.with(mContext).load(R.drawable.avater_loading).into(iv_avatar);
                        }
                    }
                }
            });
        }else{
            tv_name.setText("点击头像登录");
            Glide.with(this).load(R.drawable.ic_navi_unlogin).into(iv_avatar);
//            iv_avatar.setBackgroundResource(R.drawable.ic_navi_unlogin);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    private void setupViewPager() {
        mPagerAdapter = new MainFragmentPagerAdapter(getSupportFragmentManager());
        mGalleryFragment = new GalleryFragment();
        mHotFragment = new HotFragment();
        mLabelFragment = new LabelFragment();
        mNewsFragment = new NewsFragment();

        mPagerAdapter.addFragment(mHotFragment, "热门");
        mPagerAdapter.addFragment(mNewsFragment, "最新");
        mPagerAdapter.addFragment(mGalleryFragment, "画廊");
        mPagerAdapter.addFragment(mLabelFragment, "标签");
        mViewpager.setAdapter(mPagerAdapter);
        mViewpager.setCurrentItem(1);   //默认显示“最新”标签
        mViewpager.setOffscreenPageLimit(3);  //防止频繁的销毁视图

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.menu_invite_friend:
                share();
                break;
            case R.id.menu_about_us:
                Redirct.openActivity(mContext, SettingActivity.class);
                break;
            case R.id.menu_logout:
                loginOut();
                break;
            //消息中心
            case R.id.action_msg_center:
                if(isLogin()){
                    Redirct.openActivity(mContext, MsgCenterActivity.class);
                }else{
                    Redirct.makeToast(mContext, "请先登录 喵~");
                    Redirct.openActivity(mContext, LoginActivity.class);
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setViewData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //点击头像
            case R.id.id_iv_avatar:
                if(!isLogin()){
                    Redirct.openActivity(mContext, LoginActivity.class);
                }
                break;
            //变身
            case R.id.id_iv_trans:
                Redirct.openActivity(mContext, CharacterSelectActivity.class);
                break;
            //首页
            case R.id.id_ll_index:
                mDrawerLayout.closeDrawers();
                break;
            //我的话题
            case R.id.id_ll_topic:
                if(isLogin()){
                    Redirct.openActivity(mContext, MyTopicActivity.class);
                }else{
                    Redirct.makeToast(mContext, "请先登录 喵~");
                    Redirct.openActivity(mContext, LoginActivity.class);
                }
                break;
            //标签收藏
            case R.id.id_ll_label:
                if(isLogin()){
                    Redirct.openActivity(mContext, MyLabelCollectionActivity.class);
                }else{
                    Redirct.makeToast(mContext, "请先登录 喵~");
                    Redirct.openActivity(mContext, LoginActivity.class);
                }
                break;
            //充值提现
            case R.id.id_ll_charge:
                Redirct.openActivity(mContext, ChargeCashActivity.class);
                break;
            //系统设置
            case R.id.id_ll_setting:
                if(isLogin()){
                    Redirct.openActivity(mContext, SettingActivity.class);
                }else{
                    Redirct.makeToast(mContext, "请先登录 喵~");
                    Redirct.openActivity(mContext, LoginActivity.class);
                }
                break;
            //登录登出
            case R.id.id_ll_logout:
                loginOut();
                break;
        }
    }
    public void loginOut(){
        if(isLogin()){
            final LogoutDialog dialog = new LogoutDialog(mContext);
            dialog.setLeftBtn(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BmobUser.logOut();
                    Redirct.makeToast(mContext, "退出登陆成功 喵~");
                    dialog.dismiss();
                    onResume();
                    Redirct.openActivity(mContext, LoginActivity.class);
                }
            });
            dialog.setRightBtn(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Redirct.openActivity(mContext, CharacterSelectActivity.class);
                }
            });
            dialog.show();

        }else{
            Redirct.makeToast(mContext, "您处于未登录状态 喵~");
            Redirct.openActivity(mContext, LoginActivity.class);
        }
    }

    private static long firstTime;
    /**
     * 连续按两次返回键就退出
     */
    @Override
    public void onBackPressed() {
        if (firstTime + 2000 > System.currentTimeMillis()) {
            MyApplication.getInstance().exit();
            super.onBackPressed();
        } else {
            Redirct.makeToast(mContext, "再按一次退出程序");
        }
        firstTime = System.currentTimeMillis();
    }

    public void share(){
        ShareDialog dialog = new ShareDialog(mContext, new ShareDialog.OnButtonClickListener() {
            @Override
            public void setPlatform(int what) {
               // Bitmap bitmap = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.ic_invite_friend);
                UMImage image = new UMImage(mContext, R.drawable.ic_invite_friend);
                switch (what) {
                    case Constant.FRIEND:
                        new ShareAction(mActivity).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).setCallback(umShareListener)
                                .withText("口可口可")
                                .withMedia(image)
                                .share();
                        break;
                    case Constant.QQ:
                        new ShareAction(mActivity).setPlatform(SHARE_MEDIA.QQ).setCallback(umShareListener)
                                .withText("口可口可")
                                .withMedia(image)
                                .share();
                        break;
                    case Constant.QZONE:
                        new ShareAction(mActivity).setPlatform(SHARE_MEDIA.QZONE).setCallback(umShareListener)
                                .withText("口可口可")
                                .withMedia(image)
                                .share();
                        break;
                    case Constant.WECHAT:
                        new ShareAction(mActivity).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(umShareListener)
                                .withMedia(image)
                                //.withMedia(new UMEmoji(ShareActivity.this,"http://img.newyx.net/news_img/201306/20/1371714170_1812223777.gif"))
                                .withText("口可口可")
                                .share();
                        break;
                    case Constant.WEIBO:
                        new ShareAction(mActivity).setPlatform(SHARE_MEDIA.SINA)
                                .setCallback(umShareListener)
                                .withText("口可口可")
                                .withMedia(image)
//                       .withExtra(new UMImage(CommentActivity.this, R.drawable.ic_launcher))
//                                    .withTargetUrl("http://dev.umeng.com")
                                .share();
                        break;
                    case Constant.SAVE:
                        Redirct.makeToast(mActivity, "保存成功");
//                        ScreenshotUtil.getBitmapByView(mActivity, bitmap);
                        break;
                    default:
                }
            }
        });
        dialog.show();
    }

    public UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat","platform"+platform);
            Toast.makeText(mContext, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(mContext,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if(t!=null){
                Log.d("throw","throw:"+t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(mContext,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

    }
}
