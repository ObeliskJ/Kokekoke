package com.obelisk.koukekouke.bean;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;

public class User extends BmobUser {

	public static final String TAG = "User";

	private CharacterSelect character;// 角色
	private BmobRelation myLabel;
	private BmobRelation msg;

	public BmobRelation getMsg() {
		return msg;
	}

	public void setMsg(BmobRelation msg) {
		this.msg = msg;
	}

	public BmobRelation getMyLabel() {
		return myLabel;
	}

	public void setMyLabel(BmobRelation myLabel) {
		this.myLabel = myLabel;
	}

	public CharacterSelect getCharacter() {
		return character;
	}

	public void setCharacter(CharacterSelect character) {
		this.character = character;
	}

}
