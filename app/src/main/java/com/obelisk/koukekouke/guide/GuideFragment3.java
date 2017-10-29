package com.obelisk.koukekouke.guide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.obelisk.koukekouke.R;
import com.obelisk.koukekouke.activity.LoginActivity;
import com.obelisk.koukekouke.activity.MainActivity;
import com.obelisk.koukekouke.common.Redirct;

public class GuideFragment3 extends Fragment implements OnClickListener {
	private View mView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.guide_fragment_3, container,false);
		mView = view.findViewById(R.id.view);
		mView.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {
		Redirct.openActivity(getActivity(), MainActivity.class);
		getActivity().finish();
	}
}
