package com.obelisk.koukekouke.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.obelisk.koukekouke.R;
import com.obelisk.koukekouke.bean.Comment;


import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.obelisk.koukekouke.R.id.view;

/**
 * Created by Jiangwz on 2016/11/17.
 */

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private List<Comment> data;
    private OnRVItemClickListener mOnRVItemClickListener;
    private OnRVItemLongClickListener mOnRVItemLongClickListener;

    public CommentAdapter(Context context, List<Comment> data) {
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }
    public void setmOnRVItemClickListener(OnRVItemClickListener listener){
        mOnRVItemClickListener = listener;
    }
    public void setmOnRVItemLongClickListener(OnRVItemLongClickListener listenr){
        mOnRVItemLongClickListener = listenr;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_comment, parent, false);
        CommentViewHolder cvh = new CommentViewHolder(view);
        return cvh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof CommentViewHolder){
            ((CommentViewHolder) holder).mTvName.setText(data.get(position).getCommenter().getCharacter().getName());
            ((CommentViewHolder) holder).mTvFloor.setText(position + 1 + "楼");
            ((CommentViewHolder) holder).mTvCommentContent.setText(data.get(position).getCommentContent());
            Glide.with(context).load(data.get(position).getCommenter().getCharacter().getAvater().getFileUrl()).placeholder(R.drawable.avater_loading).dontAnimate().into(((CommentViewHolder) holder).mIvIcon);
        if(mOnRVItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition();
                    mOnRVItemClickListener.onItemClick(holder.itemView, position);
                }
            });

        }
            if(mOnRVItemLongClickListener != null){
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int position = holder.getLayoutPosition();
                        mOnRVItemLongClickListener.onItemLongClick(holder.itemView, position);
                        return true;
                    }
                });
            }

        }


    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.id_iv_icon)
        ImageView mIvIcon;
        @Bind(R.id.id_tv_name)
        TextView mTvName;
        @Bind(R.id.id_tv_floor)
        TextView mTvFloor;
        @Bind(R.id.id_tv_comment_content)
        TextView mTvCommentContent;

        public CommentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    public interface OnRVItemClickListener{
        void onItemClick(View view, int position);
    }
    public interface OnRVItemLongClickListener{
        void onItemLongClick(View view, int position);
    }


}
