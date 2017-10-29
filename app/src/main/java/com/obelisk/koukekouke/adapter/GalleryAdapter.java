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
import com.obelisk.koukekouke.bean.Koukekouke;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Jiangwz on 2016/11/17.
 */

public class GalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Koukekouke> mData;
    private LayoutInflater mInflater;

    private OnRVItemClickListener listener;

    public GalleryAdapter(Context context, List<Koukekouke> data) {
        mContext = context;
        mData = data;
        mInflater = LayoutInflater.from(context);
    }

    public void setListener(OnRVItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_gallery, parent, false);
        GalleryViewHolder gvh = new GalleryViewHolder(view);
        return gvh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof GalleryViewHolder) {
            ((GalleryViewHolder) holder).mTvLabel.setText(mData.get(position).getLabel());
            if(null != mData.get(position).getNumOfLike()){
                ((GalleryViewHolder) holder).mTvLikeNum.setText(mData.get(position).getNumOfLike().toString());
            }else{
                ((GalleryViewHolder) holder).mTvLikeNum.setText("0");
            }
            Glide.with(mContext).load(mData.get(position).getPicture().getFileUrl()).placeholder(R.drawable.pic_loading).dontAnimate().into(((GalleryViewHolder) holder).mIvPic);
            if(listener != null){
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = holder.getLayoutPosition();
                        listener.onItemClisk(holder.itemView, position);
                    }
                });
            }


        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class GalleryViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.id_iv_pic)
        ImageView mIvPic;
        @Bind(R.id.id_tv_like_num)
        TextView mTvLikeNum;
        @Bind(R.id.id_tv_label)
        TextView mTvLabel;

        public GalleryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    public interface OnRVItemClickListener{
        void onItemClisk(View view, int positon);
    }
}
