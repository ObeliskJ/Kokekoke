package com.obelisk.koukekouke.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Jiangwz on 2016/10/24.
 */

public class CharacterSelect extends BmobObject {
    private String name;
    private BmobFile avater;
    private BmobFile bigPic;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BmobFile getAvater() {
        return avater;
    }

    public void setAvater(BmobFile avater) {
        this.avater = avater;
    }

    public BmobFile getBigPic() {
        return bigPic;
    }

    public void setBigPic(BmobFile bigPic) {
        this.bigPic = bigPic;
    }
}
