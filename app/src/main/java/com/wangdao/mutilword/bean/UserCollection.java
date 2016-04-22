package com.wangdao.mutilword.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by haijun on 2016/4/20.
 */
public class UserCollection extends BmobObject {
    private String userId;
    private String type;
    private String articleUrl;
    private String title;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "UserCollection{" +
                "userId='" + userId + '\'' +
                ", type='" + type + '\'' +
                ", articleUrl='" + articleUrl + '\'' +
                '}';
    }
}
