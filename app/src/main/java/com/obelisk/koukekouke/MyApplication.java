package com.obelisk.koukekouke;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

//import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
//import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
//import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
//import com.nostra13.universalimageloader.core.assist.ImageScaleType;
//import com.nostra13.universalimageloader.utils.StorageUtils;
//import com.obelisk.koukekouke.entity.User;
//import com.obelisk.koukekouke.utils.ActivityManagerUtils;
//import com.umeng.socialize.PlatformConfig;

import com.obelisk.koukekouke.utils.ActivityManagerUtils;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.io.File;

//import cn.bmob.v3.Bmob;
//import cn.bmob.v3.BmobUser;

public class MyApplication extends Application {

	public static String TAG;
	
	private static MyApplication myApplication = null;
	
	
	public static MyApplication getInstance(){
		return myApplication;
	}
//	public User getCurrentUser() {
//		User user = BmobUser.getCurrentUser(myApplication, User.class);
//		if(user!=null){
//			return user;
//		}
//		return null;
//	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
//		Bmob.initialize(this, "bd35c5a85c46e47521c9121483caa2df");
//		TAG = this.getClass().getSimpleName();
		//由于Application类本身已经单例，所以直接按以下处理即可。
		myApplication = this;
		UMShareAPI.get(this);
        refWatcher = LeakCanary.install(this);

        Config.REDIRECT_URL = "http://sns.whalecloud.com/sina2/callback";


	}
    {
        //微信    wx12342956d1cab4f9,a5ae111de7d9ea137e88a5e02c07c94d
        PlatformConfig.setWeixin("wx5cbecb1cf26c81e3", "0ef6b88ea4d949eb92bcdfe4af90e829");
        //新浪微博
        PlatformConfig.setSinaWeibo("3001925150", "07c78510de01f99712d23cb7eb9d1b55");
        PlatformConfig.setQQZone("1105295203", "bg8BcpIFh3aiHO0E");
    }
	public void exit(){
		ActivityManagerUtils.getInstance().removeAllActivity();
	}

    public static RefWatcher getRefWatcher(Context context) {
        MyApplication application = (MyApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    private RefWatcher refWatcher;
}
