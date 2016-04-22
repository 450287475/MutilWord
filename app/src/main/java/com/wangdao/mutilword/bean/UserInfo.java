package com.wangdao.mutilword.bean;

import android.os.Parcel;
import android.os.Parcelable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by haijun on 2016/4/18.
 */
public class UserInfo extends BmobObject implements Parcelable{
    private String userid;
    private String username;
    private String password;
    private String usericon;
    private String phone;
    private String autograph;


    private BmobFile icon;

    private int collectedArticle;
    private int collectedWord;
    private int exchangeAwarded;
    private int exchangeAward;
    private int articleCount;
    private int wordCount;

    private int userrank;
    private int userpoints;

    public UserInfo() {
    }


    public UserInfo(String objectId, String userid, String username, String password, String usericon, String phone, String autograph,
                    int collectedArticle, int collectedWord, int exchangeAwarded, int exchangeAward, int articleCount,
                    int wordCount, int userrank, int userpoints) {
        this.userid = userid;
        this.username = username;
        this.password = password;
        this.usericon = usericon;
        this.phone = phone;
        this.autograph = autograph;
        this.collectedArticle = collectedArticle;
        this.collectedWord = collectedWord;
        this.exchangeAwarded = exchangeAwarded;
        this.exchangeAward = exchangeAward;
        this.articleCount = articleCount;
        this.wordCount = wordCount;
        this.userrank = userrank;
        this.userpoints = userpoints;
        this.setObjectId(objectId);
    }

    protected UserInfo(Parcel in) {
        userid = in.readString();
        username = in.readString();
        password = in.readString();
        usericon = in.readString();
        phone = in.readString();
        autograph = in.readString();
        collectedArticle = in.readInt();
        collectedWord = in.readInt();
        exchangeAwarded = in.readInt();
        exchangeAward = in.readInt();
        articleCount = in.readInt();
        wordCount = in.readInt();
        userrank = in.readInt();
        userpoints = in.readInt();
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };

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

    public String getAutograph() {
        return autograph;
    }

    public void setAutograph(String autograph) {
        this.autograph = autograph;
    }

    public int getCollectedArticle() {
        return collectedArticle;
    }

    public void setCollectedArticle(int collectedArticle) {
        this.collectedArticle = collectedArticle;
    }

    public int getCollectedWord() {
        return collectedWord;
    }

    public void setCollectedWord(int collectedWord) {
        this.collectedWord = collectedWord;
    }

    public int getExchangeAwarded() {
        return exchangeAwarded;
    }

    public void setExchangeAwarded(int exchangeAwarded) {
        this.exchangeAwarded = exchangeAwarded;
    }

    public int getExchangeAward() {
        return exchangeAward;
    }

    public void setExchangeAward(int exchangeAward) {
        this.exchangeAward = exchangeAward;
    }

    public int getArticleCount() {
        return articleCount;
    }

    public void setArticleCount(int articleCount) {
        this.articleCount = articleCount;
    }

    public int getWordCount() {
        return wordCount;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }

    public BmobFile getIcon() {
        return icon;
    }

    public void setIcon(BmobFile icon) {
        this.icon = icon;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userid);
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(usericon);
        dest.writeString(phone);
        dest.writeString(autograph);
        dest.writeInt(collectedArticle);
        dest.writeInt(collectedWord);
        dest.writeInt(exchangeAwarded);
        dest.writeInt(exchangeAward);
        dest.writeInt(articleCount);
        dest.writeInt(wordCount);
        dest.writeInt(userrank);
        dest.writeInt(userpoints);
    }
}
