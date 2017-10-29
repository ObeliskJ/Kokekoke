package com.obelisk.koukekouke.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.obelisk.koukekouke.R;
import com.obelisk.koukekouke.bean.HotLabel;
import com.obelisk.koukekouke.view.FlowLayout.FlowLayout;
import com.obelisk.koukekouke.view.FlowLayout.TagAdapter;
import com.obelisk.koukekouke.view.FlowLayout.TagFlowLayout;

import java.util.List;

public class HotLabelAdapter extends TagAdapter<HotLabel> {
	private Context mContext;
	private List<HotLabel> data;
	private LayoutInflater mInflater;
	private TagFlowLayout mFlowLayout;

	public HotLabelAdapter(Context mContext, List<HotLabel> list) {
		super(mContext, list);
	}

	@Override
	public View getView(FlowLayout parent, int position, HotLabel t) {
		
		mInflater = LayoutInflater.from(mContext);
		mFlowLayout = (TagFlowLayout) parent.findViewById(R.id.id_flowlayout);
		TextView tv = (TextView) mInflater.inflate(R.layout.item_hot_tv, mFlowLayout, false);
//		tv.setText("2");
		return tv;
	}

}
