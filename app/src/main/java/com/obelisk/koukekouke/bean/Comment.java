package com.obelisk.koukekouke.bean;

import cn.bmob.v3.BmobObject;

public class Comment extends BmobObject {
	private String commentContent;
	private Koukekouke kkkk;
	private User commenter;

	public String getCommentContent() {
		return commentContent;
	}

	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}

	public Koukekouke getKkkk() {
		return kkkk;
	}

	public void setKkkk(Koukekouke kkkk) {
		this.kkkk = kkkk;
	}

	public User getCommenter() {
		return commenter;
	}

	public void setCommenter(User commenter) {
		this.commenter = commenter;
	}

}
