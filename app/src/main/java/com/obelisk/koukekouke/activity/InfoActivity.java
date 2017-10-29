package com.obelisk.koukekouke.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.obelisk.koukekouke.R;
import com.obelisk.koukekouke.adapter.CommentAdapter;
import com.obelisk.koukekouke.base.BaseActivity;
import com.obelisk.koukekouke.bean.Comment;
import com.obelisk.koukekouke.bean.HotLabel;
import com.obelisk.koukekouke.bean.Koukekouke;
import com.obelisk.koukekouke.bean.Message;
import com.obelisk.koukekouke.bean.User;
import com.obelisk.koukekouke.common.Constant;
import com.obelisk.koukekouke.common.Redirct;
import com.obelisk.koukekouke.utils.ScreenshotUtil;
import com.obelisk.koukekouke.view.CharacterConfirmDialog;
import com.obelisk.koukekouke.view.MyLinearLayoutManager;
import com.obelisk.koukekouke.view.MyScrollView;
import com.obelisk.koukekouke.view.ShareDialog;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Jiangwz on 2016/11/17.
 */

public class InfoActivity extends BaseActivity {
    @Bind(R.id.id_tv_title)
    TextView mTvTitle;
    @Bind(R.id.id_iv_share)
    ImageView mIvShare;
    @Bind(R.id.id_iv_icon)
    ImageView mIvIcon;
    @Bind(R.id.id_tv_name)
    TextView mTvName;
    @Bind(R.id.id_tv_time)
    TextView mTvTime;
    @Bind(R.id.id_iv_pic)
    ImageView mIvPic;
    @Bind(R.id.id_iv_like)
    ImageView mIvLike;
    @Bind(R.id.id_tv_like_num)
    TextView mTvLikeNum;
    @Bind(R.id.id_iv_comment)
    ImageView mIvComment;
    @Bind(R.id.id_tv_like_comment)
    TextView mTvLikeComment;
    @Bind(R.id.id_tv_label)
    TextView mTvLabel;
    @Bind(R.id.id_rv)
    RecyclerView mRv;
    @Bind(R.id.id_sv)
    MyScrollView mSv;
    @Bind(R.id.id_et_comment)
    EditText mEtComment;
    @Bind(R.id.id_iv_send)
    ImageView mIvSend;
    @Bind(R.id.id_tv_content)
    TextView tv_content;
    @Bind(R.id.ll_to_share)
    LinearLayout ll_to_share;

    private Koukekouke entity;
    private CommentAdapter mAdapter;
    private List<Comment> data;
    private Boolean isLike = false;
    private User author;
    private CharacterConfirmDialog dialog;
    CharacterConfirmDialog loadingDialog ;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_info_comment;
    }

    @Override
    protected void initView() {
        // 禁止软件盘自动弹出
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                        | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        if(getIntent().getSerializableExtra("data") == null){
            return;
        }else{
            entity = (Koukekouke) getIntent().getSerializableExtra("data");
        }
        mTvTitle.setText("详情");
        mIvShare.setBackgroundResource(R.drawable.ic_share);
        mTvName.setText(entity.getAuthor().getCharacter().getName());
        mTvTime.setText(entity.getCreatedAt().toString());
        tv_content.setText(entity.getContent());
        Glide.with(mActivity).load(entity.getAuthor().getCharacter().getAvater().getFileUrl()).placeholder(R.drawable.avater_loading).dontAnimate().into(mIvIcon);
        if("2".equals(entity.getViewType())){
            Glide.with(mActivity).load(entity.getPicture().getFileUrl()).placeholder(R.drawable.pic_loading).dontAnimate().into(mIvPic);
        }else{
            mIvPic.setVisibility(View.GONE);
        }
        mTvLikeNum.setText(entity.getNumOfLike().toString());
        mTvLikeComment.setText(entity.getNumOfComment().toString());
        mTvLabel.setText(entity.getLabel());
        author = entity.getAuthor();
        
        //根据用户是否赞了该内容显示不同的图标
        getLikeIcon();
        loadingDialog = new CharacterConfirmDialog(mActivity, "正在加载分享图片...");

        data = new ArrayList<Comment>();
        mAdapter = new CommentAdapter(mActivity, data);
        MyLinearLayoutManager manager = new MyLinearLayoutManager(mActivity);
        mRv.setLayoutManager(manager);
        mRv.setAdapter(mAdapter);
        mAdapter.setmOnRVItemClickListener(new CommentAdapter.OnRVItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String text = "@" + (position + 1) + "楼 ";
                mEtComment.setText(text);
            }
        });

        mAdapter.setmOnRVItemLongClickListener(new CommentAdapter.OnRVItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                String text = data.get(position).getCommentContent();
                ClipboardManager cmb = (ClipboardManager) mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(text);
                Redirct.makeToast(mActivity, "内容已复制到剪贴板");
            }
        });
    }

    private void getLikeIcon() {
        User user = BmobUser.getCurrentUser(User.class);
        if(user == null){
            mIvLike.setBackgroundResource(R.drawable.ic_unlike);
        }else{
            BmobQuery<User> query = new BmobQuery<User>();
            query.addWhereRelatedTo("userOfLove", new BmobPointer(entity));
            query.addWhereEqualTo("username", user.getUsername());
            query.findObjects(new FindListener<User>() {
                @Override
                public void done(List<User> list, BmobException e) {
                    if(e == null){
                        if(list.size() == 1){
                            mIvLike.setBackgroundResource(R.drawable.ic_like);
                            isLike = true;
                        }else{
                            mIvLike.setBackgroundResource(R.drawable.ic_unlike);
                            isLike = false;
                        }
                    }else{
                        Redirct.makeToast(mActivity, e.toString());
                    }

                }
            });

        }

    }

    @Override
    protected void initData() {
        getComment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }
    public void getComment(){
        BmobQuery<Comment> query = new BmobQuery<Comment>();
        query.addWhereRelatedTo("comment", new BmobPointer(entity));
        query.include("commenter,commenter.character");
        query.order("createdAt");
        query.findObjects(new FindListener<Comment>() {
            @Override
            public void done(List<Comment> list, BmobException e) {
                if(e == null){
                    if (list.size() > 0 && list.get(list.size() - 1) != null){
                        data.addAll(list);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

    }

    private void doLike() {
        User user = BmobUser.getCurrentUser(User.class);
        if(user == null){
            Redirct.makeToast(mActivity, "点赞前请先登录~");
            return;
        }
        BmobRelation userOfLove = new BmobRelation();
        dialog = new CharacterConfirmDialog(mActivity, "提交中...");
        dialog.show();
        if(isLike){
            //取消点赞
            mIvLike.setBackgroundResource(R.drawable.ic_unlike);
            userOfLove.remove(user);
            entity.setUserOfLove(userOfLove);
            entity.update(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if(e == null){
                        Redirct.makeToast(mActivity, "取消点赞");
                        isLike = false;
                        mTvLikeNum.setText(String.valueOf(Integer.parseInt(mTvLikeNum.getText().toString())-1));
                        entity.increment("numOfLike", -1);
                        entity.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if(e == null){
                                    dialog.dismiss();
                                    doLiskMsg();
                                }
                            }
                        });
                    }else{
                        dialog.dismiss();
                        Redirct.makeToast(mActivity, e.toString());
                    }
                }
            });

        }else{
            //点赞
            mIvLike.setBackgroundResource(R.drawable.ic_like);
            userOfLove.add(user);
            entity.setUserOfLove(userOfLove);
            entity.update(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if(e == null){
                        isLike = true;
                        mTvLikeNum.setText(String.valueOf(Integer.parseInt(mTvLikeNum.getText().toString())+1));
                        entity.increment("numOfLike", 1);
                        entity.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if(e == null){
                                    dialog.dismiss();
                                    Redirct.makeToast(mActivity, "点赞成功");
                                }
                            }
                        });
                    }else{
                        dialog.dismiss();
                        Redirct.makeToast(mActivity, e.toString());
                    }
                }
            });

        }
    }
    //生成一条点赞的通知信息
    public void doLiskMsg(){
        final Message likeMsg = new Message();
        likeMsg.setFromWho(BmobUser.getCurrentUser(User.class));
        likeMsg.setMsgContent(entity.getContent());
        likeMsg.setRead(false);
        likeMsg.setMsgType("1");
        likeMsg.setTargetId(entity.getObjectId());
        likeMsg.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e == null){
                    BmobRelation likeRelation = new BmobRelation();
                    likeRelation.add(likeMsg);
                    User newUser = new User();
                    newUser.setMsg(likeRelation);
                    newUser.update(author.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e == null){
                                //Redirct.makeToast(mActivity, "121212");
                            }
                        }
                    });
                }

            }
        });
    }
    //生成一条评论的通知信息
    public void doCommentMsg(){
        final Message commentMsg = new Message();
        commentMsg.setFromWho(BmobUser.getCurrentUser(User.class));
        commentMsg.setMsgContent(entity.getContent());
        commentMsg.setRead(false);
        commentMsg.setMsgType("2");
        commentMsg.setTargetId(entity.getObjectId());
        commentMsg.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e == null){
                    BmobRelation commentRelation = new BmobRelation();
                    commentRelation.add(commentMsg);
                    User newUser = new User();
                    newUser.setMsg(commentRelation);
                    newUser.update(author.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e == null){
                               // Redirct.makeToast(mActivity, "121212");
                            }
                        }
                    });
                }

            }
        });
    }


    private void submitComment(String commentStr){
        User user = BmobUser.getCurrentUser(User.class);
        if(user == null){
            Redirct.makeToast(mActivity, "评论前请先登录~");
            Redirct.openActivity(mActivity, LoginActivity.class);
            return;
        }
        if("".equals(commentStr)){
            Redirct.makeToast(mActivity, "评论内容不能为空~");
            return;
        }
        final Comment comment = new Comment();
        comment.setCommentContent(commentStr);
        comment.setCommenter(user);
        dialog = new CharacterConfirmDialog(mActivity, "评论中...");
        dialog.show();
        comment.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e == null){
                    BmobRelation relation = new BmobRelation();
                    relation.add(comment);
                    entity.setComment(relation);
                    entity.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e == null){
                                entity.increment("numOfComment", 1);
                                entity.update(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if(e == null){
                                            dialog.dismiss();
                                            Redirct.makeToast(mActivity, "评论成功~");
                                            doCommentMsg();
                                        }else{
                                            dialog.dismiss();
                                            Redirct.makeToast(mActivity, "评论失败！");
                                        }
                                    }
                                });
                                data.clear();
                                mEtComment.setText("");
                                mEtComment.clearFocus();
                                hideSoftInput();
                                //TODO
                                getComment();
                            }else{
                                Redirct.makeToast(mActivity, e.toString());
                                dialog.dismiss();
                            }
                        }
                    });
                }
            }
        });

    }

    @OnClick({R.id.id_iv_like, R.id.id_iv_send, R.id.id_iv_left, R.id.id_iv_share,  R.id.id_tv_label})
    public void onClick(View view) {
        switch (view.getId()) {
            //点赞
            case R.id.id_iv_like:
               doLike();
               break;
            //发送评论
            case R.id.id_iv_send:
                String comment = mEtComment.getText().toString();
                submitComment(comment);
                break;
            //返回
            case R.id.id_iv_left:
                finish();
                break;
            //标签
            case R.id.id_tv_label:
                HotLabel label = new HotLabel();
                label.setLabelText(entity.getLabel().substring(1, entity.getLabel().length()));
                Bundle bundle = new Bundle();
                bundle.putSerializable("label", label);
                Redirct.openActivity(mActivity, LabelCollectionActivity.class, bundle);
                break;
            //分享
            case R.id.id_iv_share:
                ShareDialog dialog = new ShareDialog(mActivity, new ShareDialog.OnButtonClickListener() {
                    @Override
                    public void setPlatform(int what) {
//                        Bitmap bitmap = ScreenshotUtil.getBitmapByView(mActivity, mSv);
                        Bitmap bitmap = ScreenshotUtil.getBitmapByView(mActivity, ll_to_share);
                        UMImage image = new UMImage(mActivity, bitmap);

                        switch (what) {
                            case Constant.FRIEND:
                                loadingDialog.show();
                                new ShareAction(mActivity).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).setCallback(umShareListener)
                                        .withText("title")
                                        .withMedia(image)
                                        .share();

                                break;
                            case Constant.QQ:
                                loadingDialog.show();
                                new ShareAction(mActivity).setPlatform(SHARE_MEDIA.QQ).setCallback(umShareListener)
                                        .withText("title")
                                        .withMedia(image)
                                        .share();
                                break;
                            case Constant.QZONE:
                                loadingDialog.show();
                                new ShareAction(mActivity).setPlatform(SHARE_MEDIA.QZONE).setCallback(umShareListener)
                                        .withText("title")
                                        .withMedia(image)
                                        .share();
                                break;
                            case Constant.WECHAT:
                                loadingDialog.show();
                                new ShareAction(mActivity).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(umShareListener)
                                        .withMedia(image)
                                        //.withMedia(new UMEmoji(ShareActivity.this,"http://img.newyx.net/news_img/201306/20/1371714170_1812223777.gif"))
                                        .withText("title")
                                        .share();
                                break;
                            case Constant.WEIBO:
                                loadingDialog.show();
                                new ShareAction(mActivity).setPlatform(SHARE_MEDIA.SINA)
                                        .setCallback(umShareListener)
//                                        .withText("口可口可")
                                        .withMedia(image)
//                       .withExtra(new UMImage(CommentActivity.this, R.drawable.ic_launcher))
//                                    .withTargetUrl("http://dev.umeng.com")
                                        .share();
                                break;
                            case Constant.SAVE:
                                loadingDialog.show();
                                ScreenshotUtil.getBitmapByView(mActivity, ll_to_share);
                                loadingDialog.dismiss();
                                break;
                            default:
                        }
                    }
                });
                dialog.show();
                break;
        }
    }
    private void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEtComment.getWindowToken(), 0);
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

    }


    public UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat","platform"+platform);
            loadingDialog.dismiss();
            Toast.makeText(mActivity, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            loadingDialog.dismiss();
            Toast.makeText(mActivity,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if(t!=null){
                Log.d("throw","throw:"+t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            loadingDialog.dismiss();
            Toast.makeText(mActivity,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };


}
