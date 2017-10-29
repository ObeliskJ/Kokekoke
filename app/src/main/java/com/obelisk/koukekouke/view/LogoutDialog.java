package com.obelisk.koukekouke.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.obelisk.koukekouke.R;

public class LogoutDialog extends Dialog {
	private TextView tv_msg, tv_logout, tv_cancel;

	public LogoutDialog(Context context) {
		super(context, R.style.Dialog);
		setContentView(R.layout.logout_dialog);
		initViews();
	}
	public void initViews(){
		tv_msg = (TextView) findViewById(R.id.id_tv_msg);
		tv_logout = (TextView) findViewById(R.id.id_tv_logout);
		tv_cancel = (TextView) findViewById(R.id.id_tv_cancel);
		setCanceledOnTouchOutside(false) ;
	}

	public LogoutDialog setLeftBtn(View.OnClickListener listener){
		tv_logout.setOnClickListener(listener);
		return this;
	}
	public LogoutDialog setRightBtn(View.OnClickListener listener){
		tv_cancel.setOnClickListener(listener);
		return this;
		
	}
}
