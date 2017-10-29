package com.obelisk.koukekouke.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.obelisk.koukekouke.R;
import com.obelisk.koukekouke.base.BaseActivity;
import com.obelisk.koukekouke.bean.Koukekouke;
import com.obelisk.koukekouke.bean.User;
import com.obelisk.koukekouke.common.Redirct;
import com.obelisk.koukekouke.view.CharacterConfirmDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import me.iwf.photopicker.PhotoPicker;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by Jiangwz on 2016/11/8.
 */

public class EditContentActivity extends BaseActivity {
    @Bind(R.id.id_iv_left)
    ImageView ivLeft;
    @Bind(R.id.id_tv_title)
    TextView tvTitle;
    @Bind(R.id.id_iv_share)
    ImageView ivRight;
    @Bind(R.id.id_et_content)
    EditText etContent;
    @Bind(R.id.id_tv_num_text)
    TextView tvNumText;
    @Bind(R.id.id_tv_label)
    TextView tvLabel;
    @Bind(R.id.id_btn_submit)
    Button btnSubmit;
//    @Bind(R.id.fab)
//    FloatingActionButton fab;
    public static final int MAX_COUNT = 500;
    private File picfile = null;
    private CharacterConfirmDialog dialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_content;
    }

    @Override
    protected void initView() {
        tvTitle.setText("发表");
        etContent.addTextChangedListener(mTextWatcher);
        ivRight.setImageResource(R.drawable.ic_camera);
//
//        etContent.setFocusableInTouchMode(true);
//        etContent.requestFocus();
//        InputMethodManager inputManager =
//                (InputMethodManager)etContent.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//        inputManager.showSoftInput(etContent, 0);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        private int editStart;
        private int editEnd;

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            editStart = etContent.getSelectionStart();
            editEnd = etContent.getSelectionEnd();
            etContent.removeTextChangedListener(mTextWatcher);
            while (calculateLength(s.toString()) > MAX_COUNT) { // 当输入字符个数超过限制的大小时，进行截断操作
                s.delete(editStart - 1, editEnd);
                editStart--;
                editEnd--;
                Redirct.makeToast(mActivity, "吼吼吼~字数超过了最大限制，后续输入将无效！");
            }
            if(s.equals("#这里可以输入标签喵~#") || s.equals("")){
                btnSubmit.setBackgroundResource(R.color.gray);
                btnSubmit.setEnabled(false);
            }else{
                btnSubmit.setBackgroundResource(R.color.splash_footer);
                btnSubmit.setEnabled(true);
            }
            String[] content = s.toString().split("#");
            if (content.length > 1) {
                if (content[1].length() > 12) {
                    Redirct.makeToast(mActivity, "标签超过了12个字了喵~");
                    tvLabel.setText("#" + content[1].substring(0, 12));
                } else {
                    tvLabel.setText("#" + content[1]);
                }
            } else {
                tvLabel.setText("#");
            }
            etContent.setText(s);
            etContent.setSelection(editStart);
            etContent.addTextChangedListener(mTextWatcher);
            setLeftCount();
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            // TODO Auto-generated method stub
            // if (StringUtils.isEmpty(ViewUtils.getText(contentEt))) {
            // submmitBtn.setEnabled(false);
            // //submmitBtn.setBackgroundResource(R.drawable.btn_common_noclick);
            // } else {
            // submmitBtn.setEnabled(true);
            // //submmitBtn.setBackgroundResource(R.drawable.btn_common_red);
            // }
        }
    };

    /**
     * 计算分享内容的字数，一个汉字=两个英文字母，一个中文标点=两个英文标点 注意：该函数的不适用于对单个字符进行计算，因为单个字符四舍五入后都是1
     * //做判断 if (tmp > 0 && tmp < 127) { len += 0.5; } else { len++; }
     */
    private long calculateLength(CharSequence c) {
        double len = 0;
        for (int i = 0; i < c.length(); i++) {
            // int tmp = (int) c.charAt(i);
            /*
			 * if (tmp > 0 && tmp < 127) { len += 0.5; } else { len++; }
			 */
            len++;
        }
        return Math.round(len);
    }

    /**
     * 刷新剩余输入字数
     */
    private void setLeftCount() {
        tvNumText.setText(getInputCount() + "/" + MAX_COUNT);
    }

    /**
     * 获取用户输入的分享内容字数
     */
    private long getInputCount() {
        return calculateLength(etContent.getText().toString());
    }

    @OnClick({R.id.id_iv_left, R.id.id_btn_submit,R.id.id_iv_share})
    public void onClick(View view) {
        switch (view.getId()) {
            //返回
            case R.id.id_iv_left:
                finish();
                break;
            //提交
            case R.id.id_btn_submit:
                doSubmit();
                break;
            //打开相册选照片
            case R.id.id_iv_share:
                if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else {
                    getPic();
                }

                break;
        }
    }

    private void doSubmit() {
        String content = etContent.getText().toString();
        String label = tvLabel.getText().toString();
        if(content.equals("") || content.equals("#这里可以输入标签喵~#")){
            Redirct.makeToast(mActivity, "还没有输入任何内容喵~");
            return;
        }
        User mUser = BmobUser.getCurrentUser(User.class);
        dialog = new CharacterConfirmDialog(mActivity, "正在将内容发射到天际...");
        dialog.show();

        final Koukekouke kk = new Koukekouke();
        kk.setAuthor(mUser);
        kk.setContent(content);
        kk.setNumOfComment(0);
        kk.setNumOfLike(0);
        kk.setLabel(label);
        if(picfile == null){
            kk.setViewType("1");
            kk.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if(e == null){
                        dialog.dismiss();
                        Redirct.makeToast(mActivity, "内容发射成功 喵~");
                        finish();
                    }else{
                        dialog.dismiss();
                        Redirct.makeToast(mActivity, "すみません~\n发表失败，请重新再试"  + e.getErrorCode());
                    }
                }
            });

        }else{
            final BmobFile file = new BmobFile(picfile);
            file.uploadblock(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    if(e == null){
                        kk.setPicture(file);
                        kk.setViewType("2");
                        kk.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if(e == null){
                                    dialog.dismiss();
                                    Redirct.makeToast(mActivity, "内容发射成功 喵~");
                                    finish();
                                }else{
                                    dialog.dismiss();
                                    Redirct.makeToast(mActivity, "すみません~\n发表失败，请重新再试"  + e.getErrorCode());
                                }
                            }
                        });

                    }

                }
            });

        }


    }

    private void getPic() {
        PhotoPicker.builder()
                .setPhotoCount(1)
                .setShowCamera(false)
                .setPreviewEnabled(false)
                .start(mActivity, PhotoPicker.REQUEST_CODE);;
    }


//    private void getPic() {
//        Intent intent = new Intent();
//            /* 开启Pictures画面Type设定为image */
//        intent.setType("image/*");
//            /* 使用Intent.ACTION_GET_CONTENT这个Action */
//        intent.setAction(Intent.ACTION_PICK);
//            /* 取得相片后返回本画面 */
//        startActivityForResult(intent, 1);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == RESULT_OK) {
//            Uri uri = data.getData();
////            Log.e("uri", uri.toString());
//            ContentResolver contentResolver = this.getContentResolver();
//            try {
//                Bitmap bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri));
//                Redirct.makeToast(mActivity, getRealFilePath(mActivity, uri));
//              // getRealFilePath(mActivity, uri);
////                imageView = (ImageView) findViewById(R.id.image);
//                 /* 将Bitmap设定到ImageView */
////                imageView.setImageBitmap(bitmap);
//            }
//            catch (FileNotFoundException e){
////                Log.e("Exception", e.getMessage(),e);
//            }
//        }
        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);

                File imgFile = new File(photos.get(0));
               // fileSize.setText(imgFile.length() / 1024 + "k");
               // imageSize.setText(Luban.get(this).getImageSize(imgFile.getPath())[0] + " * " + Luban.get(this).getImageSize(imgFile.getPath())[1]);

                for (int i = 0; i < photos.size(); i++)
                    compressWithLs(new File(photos.get(i)));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 压缩单张图片 Listener 方式
     */
    private void compressWithLs(File file) {
        Luban.get(this)
                .load(file)
                .putGear(Luban.THIRD_GEAR)
                .setFilename(System.currentTimeMillis() + "")
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                       // Toast.makeText(MainActivity.this, "I'm start", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(File file) {
                        Log.i("path", file.getAbsolutePath());
                        Glide.with(mActivity).load(file).into(ivRight);
                        picfile = file;

//                        thumbFileSize.setText(file.length() / 1024 + "k");
//                        thumbImageSize.setText(Luban.get(getApplicationContext()).getImageSize(file.getPath())[0] + " * " + Luban.get(getApplicationContext()).getImageSize(file.getPath())[1]);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                }).launch();
    }

    /**
     * Try to return the absolute file path from the given Uri
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePath(final Context context, final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
}
