package com.wangdao.mutilword.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by haijun on 2016/4/18.
 */
public class UserInfo extends BmobObject {
    private String userid;
    private String username;
    private String password;
    private String usericon;
    private String phone;
    private int userrank;
    private int userpoints;

    public UserInfo() {
    }

    public UserInfo(String userid, String username, String password, String usericon, String phone, int userrank, int userpoints) {
        this.userid = userid;
        this.username = username;
        this.password = password;
        this.usericon = usericon;
        this.phone = phone;
        this.userrank = userrank;
        this.userpoints = userpoints;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsericon() {
        return usericon;
    }

    public void setUsericon(String usericon) {
        this.usericon = usericon;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getUserrank() {
        return userrank;
    }

    public void setUserrank(int userrank) {
        this.userrank = userrank;
    }

    public int getUserpoints() {
        return userpoints;
    }

    public void setUserpoints(int userpoints) {
        this.userpoints = userpoints;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "userid='" + userid + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", usericon='" + usericon + '\'' +
                ", phone='" + phone + '\'' +
                ", userrank=" + userrank +
                ", userpoints=" + userpoints +
                '}';
    }
}
