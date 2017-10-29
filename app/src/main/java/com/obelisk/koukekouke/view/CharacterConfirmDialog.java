package com.obelisk.koukekouke.view;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import com.obelisk.koukekouke.R;

import org.w3c.dom.Text;

public class CharacterConfirmDialog extends Dialog {
	private TextView tv_tip;

	public CharacterConfirmDialog(Context context) {
		super(context, R.style.Dialog);
		setContentView(R.layout.dialog_character);
		setCancelable(false);
	}
	public CharacterConfirmDialog(Context context, String text) {
		super(context, R.style.Dialog);
		setContentView(R.layout.dialog_character);
		tv_tip = (TextView) findViewById(R.id.tv_tip);
		tv_tip.setText(text);
		setCancelable(false);
//		setCanceledOnTouchOutside(false);
	}
	
	

}
