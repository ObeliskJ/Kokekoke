package com.obelisk.koukekouke.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Jiangwz on 2016/11/21.
 */

public class Feedback extends BmobObject{
    private String content;
    private String contact;
    private User user;

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
