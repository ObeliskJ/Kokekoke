package com.obelisk.koukekouke.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Environment;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.obelisk.koukekouke.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 截图操作
 * Created by Shall on 2015-07-22.
 */
public class ScreenshotUtil {

    private final static String FILE_SAVEPATH = Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/kokekoke" ;
    public static String pathfile = FILE_SAVEPATH + "/share.png";
    public static int h = 0;

    /**
     * 因为课表是可以滑动 的所以截取
     * 截取scrollview的屏幕
     **/
    public static Bitmap getBitmapByView(Context mContext, final ScrollView scrollView) {
        // 获取listView实际高度
        h = 0;
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
            scrollView.getChildAt(i).setBackgroundResource(android.R.color.white);
        }
        // 创建对应大小的bitmap
        Bitmap bitmap = Bitmap.createBitmap(scrollView.getWidth(), h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);

        Bitmap foot = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_share_footer);
        Bitmap v = toConformBitmap(bitmap, foot);


        File savedir = new File(FILE_SAVEPATH);
        if (!savedir.exists()) {
            savedir.mkdirs();
        }

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(pathfile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            //Toast.makeText(mContext,"保存失败", Toast.LENGTH_SHORT).show();
        }
        try {
            if (null != out) {
                v.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
            }
            Toast.makeText(mContext,"保存成功", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(mContext,"保存失败", Toast.LENGTH_SHORT).show();
        }
        return v;

    }

    /**
     * 因为课表是可以滑动 的所以截取
     * 截取LinearLayout的屏幕
     **/
    public static Bitmap getBitmapByView(Context mContext, final LinearLayout view) {
        // 获取listView实际高度
        /*h = 0;
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
            scrollView.getChildAt(i).setBackgroundResource(android.R.color.white);
        }*/
        // 创建对应大小的bitmap
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        Bitmap foot = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_share_footer);
        Bitmap v = toConformBitmap(bitmap, foot);


        File savedir = new File(FILE_SAVEPATH);
        if (!savedir.exists()) {
            savedir.mkdirs();
        }

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(pathfile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            //Toast.makeText(mContext,"保存失败", Toast.LENGTH_SHORT).show();
        }
        try {
            if (null != out) {
                v.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
            }
            Toast.makeText(mContext,"保存成功", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(mContext,"保存失败", Toast.LENGTH_SHORT).show();
        }
        return v;

    }

    /**
     * 合并图片
     *
     * @return
     */
    public static Bitmap toConformBitmap(Bitmap content, Bitmap footer) {
        if (content == null) {
            return null;
        }
        int contentWidth = content.getWidth();
        int footerWidth = footer.getWidth();

        int contentHeight = content.getHeight();
        int footerHeight = footer.getHeight();
        //生成三个图片合并大小的Bitmap
        Bitmap newbmp = Bitmap.createBitmap(contentWidth, contentHeight + footerHeight, Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(newbmp);
        cv.drawBitmap(content, 0, 0, null);// 在 0，0坐标开始画入headBitmap

        //因为手机不同图片的大小的可能小了 就绘制白色的界面填充剩下的界面
        if (contentWidth < footerWidth) {
            System.out.println("绘制头");
            Bitmap ne = Bitmap.createBitmap(footerWidth - contentWidth, contentHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(ne);
            canvas.drawColor(Color.WHITE);
            cv.drawBitmap(ne, contentWidth, 0, null);
        }
        cv.drawBitmap(footer, 0, contentHeight, null);// 在 0，headHeight坐标开始填充课表的Bitmap
        cv.save(Canvas.ALL_SAVE_FLAG);// 保存
        cv.restore();// 存储
        //回收
        content.recycle();
        footer.recycle();
        return newbmp;
    }


}
