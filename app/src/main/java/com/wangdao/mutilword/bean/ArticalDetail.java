package com.wangdao.mutilword.bean;

import java.io.File;

import cn.bmob.v3.BmobObject;

/**
 * Created by haijun on 2016/4/19.
 */
public class ArticalDetail extends BmobObject {
    private File articleUrl;

    public File getArticleUrl() {
        return articleUrl;
    }

    public void setArticleUrl(File articleUrl) {
        this.articleUrl = articleUrl;
    }
}
