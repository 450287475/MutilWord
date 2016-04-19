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
}
