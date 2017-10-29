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
import com.obelisk.koukekouke.bean.CharacterSelect;
import com.obelisk.koukekouke.common.Redirct;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Jiangwz on 2016/10/24.
 */

public class CharacterSelectAdapter extends RecyclerView.Adapter<CharacterSelectAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    private List<CharacterSelect> data;

    private OnItemClickListener mOnItemClickListener;

    public CharacterSelectAdapter(Context context, List<CharacterSelect> data) {
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_character_select, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mTvName.setText(data.get(position).getName());
        Glide.with(context)
                .load(data.get(position).getAvater().getFileUrl())
                .dontAnimate()
                .placeholder(R.drawable.avater_loading)
                .into(holder.mIvAvater);

        if(mOnItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
       // public View rootView;
        @Bind(R.id.id_iv_avater)
        ImageView mIvAvater;
        @Bind(R.id.id_tv_name)
        TextView mTvName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
          //  rootView = itemView;
//            mTvName = (TextView) itemView.findViewById(R.id.id_tv_name);
//            mIvAvater = (ImageView) itemView.findViewById(R.id.id_iv_avater);
        }

    }
    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }


}
