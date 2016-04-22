package com.wangdao.mutilword.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by haijun on 2016/4/22.
 */
public class ShoppingCart extends BmobObject {
    private String userid;
    private String goodId;
    private String iconurl;
    private String Detailurl ;
    private String name;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getGoodId() {
        return goodId;
    }

    public void setGoodId(String goodId) {
        this.goodId = goodId;
    }

    public String getIconurl() {
        return iconurl;
    }

    public void setIconurl(String iconurl) {
        this.iconurl = iconurl;
    }

    public String getDetailurl() {
        return Detailurl;
    }

    public void setDetailurl(String detailurl) {
        Detailurl = detailurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
