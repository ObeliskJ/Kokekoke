package com.obelisk.koukekouke.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class HotLabel extends BmobObject {
    private String LabelText;
    private Boolean isRedLabel;            //是否标红
    private Boolean isActivation;   //是否可见
    private BmobFile labelPic;            //带图片标签的背景
    private Boolean isPic;       //是否显示图片
    private BmobFile headerPic; //标签详情的头部背景
    private String describe; //标签描述

    public BmobFile getHeaderPic() {
        return headerPic;
    }

    public void setHeaderPic(BmobFile headerPic) {
        this.headerPic = headerPic;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getLabelText() {
        return LabelText;
    }

    public void setLabelText(String labelText) {
        LabelText = labelText;
    }

    public Boolean getIsRedLabel() {
        return isRedLabel;
    }

    public void setIsRedLabel(Boolean isRedLabel) {
        this.isRedLabel = isRedLabel;
    }

    public Boolean getIsActivation() {
        return isActivation;
    }

    public void setIsActivation(Boolean isActivation) {
        this.isActivation = isActivation;
    }


    public BmobFile getLabelPic() {
        return labelPic;
    }

    public void setLabelPic(BmobFile labelPic) {
        this.labelPic = labelPic;
    }

    public Boolean getPic() {
        return isPic;
    }

    public void setPic(Boolean pic) {
        isPic = pic;
    }
}
