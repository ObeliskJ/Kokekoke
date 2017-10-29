package com.obelisk.koukekouke.bean;

import java.io.Serializable;
import java.util.zip.Inflater;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

public class Koukekouke extends BmobObject implements Serializable {
	
	//首页每个item的内容
	
	private static final long serialVersionUID = -2208737306829103909L;
	
	private User author;
	private String content;
	private String label; // 标签
	private BmobRelation userOfLove;//赞了的用户
	private BmobRelation comment; // 评论
	//以下为2.0新增
	private String viewType; //数据类型：0为标题  1 纯文字内容  2带图片的文字内容  3、话题导引
	private BmobFile picture;	 //内容带有的图片

	private Integer numOfLike;   //点赞的数量
	private Integer numOfComment;      //评论的数量

    public Integer getNumOfLike() {
        return numOfLike;
    }

    public void setNumOfLike(Integer numOfLike) {
        this.numOfLike = numOfLike;
    }

    public Integer getNumOfComment() {
        return numOfComment;
    }

    public void setNumOfComment(Integer numOfComment) {
        this.numOfComment = numOfComment;
    }

    public String getViewType() {
		return viewType;
	}

	public void setViewType(String viewType) {
		this.viewType = viewType;
	}

	public BmobFile getPicture() {
		return picture;
	}

	public void setPicture(BmobFile picture) {
		this.picture = picture;
	}

	public BmobRelation getUserOfLove() {
		return userOfLove;
	}

	public void setUserOfLove(BmobRelation userOfLove) {
		this.userOfLove = userOfLove;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}


	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public BmobRelation getComment() {
		return comment;
	}

	public void setComment(BmobRelation comment) {
		this.comment = comment;
	}



}
