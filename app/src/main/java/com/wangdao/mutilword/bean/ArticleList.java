package com.wangdao.mutilword.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by haijun on 2016/4/19.
 */
public class ArticleList extends BmobObject {

    private String id;
    private String title;
    private String type;
    private int count;
    private String image;
    private String articleurl;
    private String location;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getArticleurl() {
        return articleurl;
    }

    public void setArticleurl(String articleurl) {
        this.articleurl = articleurl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "ArticleList{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", count=" + count +
                ", image='" + image + '\'' +
                ", articleurl='" + articleurl + '\'' +
                '}';
    }
}
