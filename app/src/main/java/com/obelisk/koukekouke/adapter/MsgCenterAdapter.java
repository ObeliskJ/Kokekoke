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
import com.obelisk.koukekouke.R;
import com.obelisk.koukekouke.bean.Message;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Jiangwz on 2016/12/4.
 */

public class MsgCenterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<Message> data;
    private LayoutInflater inflater;

    private OnRVItemClickListener mOnRVItemClickListener;

    public MsgCenterAdapter(Context context, List<Message> list){
        data = list;
        mContext = context;
        inflater = LayoutInflater.from(mContext);
    }

    public void setmOnRVItemClickListener(OnRVItemClickListener mOnRVItemClickListener) {
        this.mOnRVItemClickListener = mOnRVItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_msg_center, parent, false);
        MsgViewHolder holder = new MsgViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MsgViewHolder){
            ((MsgViewHolder) holder).tv_content.setText(data.get(position).getMsgContent());
            if(!data.get(position).getRead()){
                ((MsgViewHolder) holder).tv_is_read.setText("未读");
                ((MsgViewHolder) holder).tv_is_read.setTextColor(mContext.getResources().getColor(R.color.splash_footer));
            }else{
                ((MsgViewHolder) holder).tv_is_read.setText("已读");
                ((MsgViewHolder) holder).tv_is_read.setTextColor(mContext.getResources().getColor(R.color.black_666));
            }
            if("1".equals(data.get(position).getMsgType())){
                ((MsgViewHolder) holder).tv_title.setText(data.get(position).getFromWho().getCharacter().getName() + "  赞了你的话题");
            }else if("2".equals(data.get(position).getMsgType())){
                ((MsgViewHolder) holder).tv_title.setText(data.get(position).getFromWho().getCharacter().getName() + "  评论了你的话题");
            }else if("3".equals(data.get(position).getMsgType())){
                ((MsgViewHolder) holder).tv_title.setText(data.get(position).getFromWho().getCharacter().getName() + "  回复了你的评论");
            }

            Glide.with(mContext).load(data.get(position).getFromWho().getCharacter().getAvater().getFileUrl()).placeholder(R.drawable.avater_loading).dontAnimate().into(((MsgViewHolder) holder).iv_icon);
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
    public int getItemCount() {
        return data.size();
    }

    public static class MsgViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.iv_icon)
        ImageView iv_icon;
        @Bind(R.id.tv_title)
        TextView tv_title;
        @Bind(R.id.tv_is_read)
        TextView tv_is_read;
        @Bind(R.id.tv_content)
        TextView tv_content;
        public MsgViewHolder(View viewItem){
            super(viewItem);
            ButterKnife.bind(this, itemView);
        }

    }
    public interface OnRVItemClickListener{
        void onItemClick(View view, int position);
    }
}
