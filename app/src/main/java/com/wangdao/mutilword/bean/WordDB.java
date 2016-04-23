package com.wangdao.mutilword.bean;

import java.io.File;

import cn.bmob.v3.BmobObject;

/**
 * Created by haijun on 2016/4/21.
 */
public class WordDB extends BmobObject {

    private File db;
    private String id;
    private Boolean ispay;

    public File getDb() {
        return db;
    }

    public void setDb(File db) {
        this.db = db;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getIspay() {
        return ispay;
    }

    public void setIspay(Boolean ispay) {
        this.ispay = ispay;
    }
}
