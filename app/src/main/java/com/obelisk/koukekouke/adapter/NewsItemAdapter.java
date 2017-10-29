package com.obelisk.koukekouke.adapter;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.obelisk.koukekouke.R;
import com.obelisk.koukekouke.bean.Koukekouke;
import com.obelisk.koukekouke.common.Redirct;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jiangwz on 2016/10/24.
 */

public class NewsItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Koukekouke> data;
    private LayoutInflater inflater;

    public static int HEADER = 0; //头部标题内容
    public static int TEXT = 1;    //纯文字的内容
    public static int PIC = 2;     //带图的内容
    public static int TOPIC = 3;     //话题引导

    private OnRVItemClickListener mOnRVItemClickListener;

    public NewsItemAdapter(Context context, List<Koukekouke> data) {
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }

    public void setOnRVItemClickListener(OnRVItemClickListener listener){
        this.mOnRVItemClickListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == PIC) {
            View view = inflater.inflate(R.layout.item_kk_pic, parent, false);
            NewsViewHolderPic nvhp = new NewsViewHolderPic(view);
            return nvhp;
        } else if (viewType == TEXT) {
            View view = inflater.inflate(R.layout.item_kk_text, parent, false);
            NewsViewHolderText nvht = new NewsViewHolderText(view);
            return nvht;
        } else if (viewType == HEADER) {
            View view = inflater.inflate(R.layout.item_kk_header, parent, false);
            NewsViewHolderHeader nvhh = new NewsViewHolderHeader(view);
            return nvhh;
        } else if (viewType == TOPIC) {
            View view = inflater.inflate(R.layout.item_kk_topic, parent, false);
            NewsViewHolderTopic nvht = new NewsViewHolderTopic(view);
            return nvht;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NewsViewHolderPic) {
            if(data.get(position).getAuthor().getCharacter() == null){
                ((NewsViewHolderPic) holder).mTvName.setText("口可口可");
                Glide.with(context).load(R.drawable.ic_navi_unlogin).into(((NewsViewHolderPic) holder).mIvIcon);
            }else{
                //.skipMemoryCache(true)
                Glide.with(context).load(data.get(position).getAuthor().getCharacter().getAvater().getFileUrl()).placeholder(R.drawable.avater_loading).dontAnimate().into(((NewsViewHolderPic) holder).mIvIcon);
                ((NewsViewHolderPic) holder).mTvName.setText(data.get(position).getAuthor().getCharacter().getName());
            }
            ((NewsViewHolderPic) holder).mTvTime.setText(data.get(position).getCreatedAt().substring(11, 16));
            ((NewsViewHolderPic) holder).mTvContent.setText(data.get(position).getContent());
            ((NewsViewHolderPic) holder).mTvLabel.setText(data.get(position).getLabel());
            Glide.with(context).load(data.get(position).getPicture().getFileUrl()).placeholder(R.drawable.pic_loading).dontAnimate().into(((NewsViewHolderPic) holder).mIvPic);
            if (data.get(position).getNumOfLike() != null) {
                ((NewsViewHolderPic) holder).mTvLikeNum.setText(data.get(position).getNumOfLike().toString());
            } else {
                ((NewsViewHolderPic) holder).mTvLikeNum.setText("0");
            }
            if (data.get(position).getNumOfComment() != null) {
                ((NewsViewHolderPic) holder).tv_num_comment.setText(data.get(position).getNumOfComment().toString());
            } else {
                ((NewsViewHolderPic) holder).tv_num_comment.setText("0");
            }


        } else if (holder instanceof NewsViewHolderText) {
            if(data.get(position).getAuthor().getCharacter() == null){
                ((NewsViewHolderText) holder).mTvName.setText("口可口可");
                Glide.with(context).load(R.drawable.ic_navi_unlogin).into(((NewsViewHolderText) holder).mIvIcon);
            }else{
                Glide.with(context).load(data.get(position).getAuthor().getCharacter().getAvater().getFileUrl()).placeholder(R.drawable.avater_loading).dontAnimate().into(((NewsViewHolderText) holder).mIvIcon);
                ((NewsViewHolderText) holder).mTvName.setText(data.get(position).getAuthor().getCharacter().getName());
            }
            ((NewsViewHolderText) holder).mTvTime.setText(data.get(position).getCreatedAt().substring(11, 16));
            ((NewsViewHolderText) holder).mTvContent.setText(data.get(position).getContent());
            ((NewsViewHolderText) holder).mTvLabel.setText(data.get(position).getLabel());
            if (data.get(position).getNumOfLike() != null) {
                ((NewsViewHolderText) holder).mTvLikeNum.setText(data.get(position).getNumOfLike().toString());
            } else {
                ((NewsViewHolderText) holder).mTvLikeNum.setText("0");
            }
            if (data.get(position).getNumOfComment() != null) {
                ((NewsViewHolderText) holder).tv_num_comment.setText(data.get(position).getNumOfComment().toString());
            } else {
                ((NewsViewHolderText) holder).tv_num_comment.setText("0");
            }

        } else if (holder instanceof NewsViewHolderHeader) {
            Glide.with(context).load(data.get(position).getPicture().getFileUrl()).placeholder(R.drawable.pic_loading).dontAnimate().into(((NewsViewHolderHeader) holder).iv_pic);


        } else if (holder instanceof NewsViewHolderTopic) {
            Glide.with(context).load(data.get(position).getPicture().getFileUrl()).placeholder(R.drawable.pic_loading).dontAnimate().into(((NewsViewHolderTopic) holder).iv_pic);

        }
        if(mOnRVItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition();
                    mOnRVItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }

    }

    @Override
    public int getItemViewType(int position) {
        if(data.get(position).getViewType() != null){
            if ("0".equals(data.get(position).getViewType())) {
                return HEADER;

            } else if ("2".equals(data.get(position).getViewType())) {
                return PIC;

            } else if ("3".equals(data.get(position).getViewType())) {
                return TOPIC;

            } else {
                return TEXT;
            }
        }else{
            return TEXT;
        }

    }

    @Override
    public int getItemCount() {
        return data.size();

    }

    @OnClick(R.id.id_iv_like)
    public void onClick() {

    }

    //带图的评论内容
    public static class NewsViewHolderPic extends RecyclerView.ViewHolder {
        @Bind(R.id.id_iv_icon)
        ImageView mIvIcon;
        @Bind(R.id.id_tv_name)
        TextView mTvName;
        @Bind(R.id.id_tv_time)
        TextView mTvTime;
        @Bind(R.id.id_tv_content)
        TextView mTvContent;
        @Bind(R.id.id_iv_like)
        ImageView mIvLike;
        @Bind(R.id.id_tv_like_num)
        TextView mTvLikeNum;
        @Bind(R.id.id_tv_label)
        TextView mTvLabel;
        @Bind(R.id.id_iv_pic)
        ImageView mIvPic;
        @Bind(R.id.id_tv_like_comment)
        TextView tv_num_comment;

        public NewsViewHolderPic(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    //纯文字的内容
    public static class NewsViewHolderText extends RecyclerView.ViewHolder {
        @Bind(R.id.id_iv_icon)
        ImageView mIvIcon;
        @Bind(R.id.id_tv_name)
        TextView mTvName;
        @Bind(R.id.id_tv_time)
        TextView mTvTime;
        @Bind(R.id.id_tv_content)
        TextView mTvContent;
        @Bind(R.id.id_iv_like)
        ImageView mIvLike;
        @Bind(R.id.id_tv_like_num)
        TextView mTvLikeNum;
        @Bind(R.id.id_tv_label)
        TextView mTvLabel;
        @Bind(R.id.id_tv_like_comment)
        TextView tv_num_comment;

        public NewsViewHolderText(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    //标题内容
    public static class NewsViewHolderHeader extends RecyclerView.ViewHolder {
        @Bind(R.id.id_iv_pic)
        ImageView iv_pic;

        public NewsViewHolderHeader(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //话题内容
    public static class NewsViewHolderTopic extends RecyclerView.ViewHolder {
        @Bind(R.id.id_iv_pic)
        ImageView iv_pic;

        public NewsViewHolderTopic(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public interface OnRVItemClickListener{
        void onItemClick(View view, int position);
    }
}
