package com.wangdao.mutilword.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by haijun on 2016/4/22.
 */
public class GoodsOrder extends BmobObject {
    private String orderId;
    private String goodId;
    private String userid;
    private String goodsname;
    private int points;
    private float money;
    private String userAddress;
    private String userPhone;
    private String username;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getGoodId() {
        return goodId;
    }

    public void setGoodId(String goodId) {
        this.goodId = goodId;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    @Override
    public String toString() {
        return "GoodsOrder{" +
                "orderId='" + orderId + '\'' +
                ", goodId='" + goodId + '\'' +
                ", userid='" + userid + '\'' +
                ", goodsname='" + goodsname + '\'' +
                ", points=" + points +
                ", money=" + money +
                ", userAddress='" + userAddress + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
