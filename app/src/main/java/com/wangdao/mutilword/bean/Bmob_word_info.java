package com.wangdao.mutilword.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Mumuseng on 2016/4/21.
 */
public class Bmob_word_info extends BmobObject {
        String word;//单词
        String trans;//意思
        String phonetic;//音标
        String tags;//单词等级
        Integer repeat;//重复次数
        Long date;//上次背过的时间
        String username;//此单词所属的用户

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Bmob_word_info{" +
                "word='" + word + '\'' +
                ", trans='" + trans + '\'' +
                ", phonetic='" + phonetic + '\'' +
                ", tags='" + tags + '\'' +
                ", repeat=" + repeat +
                ", date=" + date +
                ", username='" + username + '\'' +
                '}';
    }

    public Bmob_word_info(String word, String trans, String phonetic, String tags, Integer repeat, Long date, String username) {
        this.word = word;
        this.trans = trans;
        this.phonetic = phonetic;
        this.tags = tags;
        this.repeat = repeat;
        this.date = date;
        this.username = username;
    }


    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getTrans() {
        return trans;
    }

    public void setTrans(String trans) {
        this.trans = trans;
    }

    public String getPhonetic() {
        return phonetic;
    }

    public void setPhonetic(String phonetic) {
        this.phonetic = phonetic;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Integer getRepeat() {
        return repeat;
    }

    public void setRepeat(Integer repeat) {
        this.repeat = repeat;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Bmob_word_info() {

    }

}
