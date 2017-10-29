package com.obelisk.koukekouke.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by Jiangwz on 2016/12/4.
 */

public class Message extends BmobObject {
    private String targetId;
    private String msgType;  //1 赞的消息   2 评论的消息  3 回复的消息  4 系统消息
    private Boolean isRead;
    private User fromWho;
    private String msgContent;

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public Boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }

    public User getFromWho() {
        return fromWho;
    }

    public void setFromWho(User fromWho) {
        this.fromWho = fromWho;
    }
}
