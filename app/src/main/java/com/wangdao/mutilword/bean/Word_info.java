package com.wangdao.mutilword.bean;

/**
 * Created by Mumuseng on 2016/4/19.
 */
public class Word_info {
    String word;//单词
    String trans;//意思
    String phonetic;//音标
    String tags;//单词等级
    int repeat;//重复次数
    int id;
    long date;

    public Word_info(String word, String trans, String phonetic, String tags, int repeat, int id, long date) {
        this.word = word;
        this.trans = trans;
        this.phonetic = phonetic;
        this.tags = tags;
        this.repeat = repeat;
        this.id = id;
        this.date = date;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }



    public Word_info() {
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

}
