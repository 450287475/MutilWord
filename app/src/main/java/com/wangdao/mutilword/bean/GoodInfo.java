package com.wangdao.mutilword.bean;

import android.os.Parcel;
import android.os.Parcelable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by haijun on 2016/4/22.
 */
public class GoodInfo extends BmobObject implements Parcelable{
    private String goodId;
    private String name;
    private String iconurl;
    private String Detailurl ;
    private BmobFile icon;
    private int points;
    private int changedcount;
    private int leftcount;
    private float money;

    public GoodInfo() {
    }

    protected GoodInfo(Parcel in) {
        goodId = in.readString();
        name = in.readString();
        iconurl = in.readString();
        Detailurl = in.readString();
        points = in.readInt();
        changedcount = in.readInt();
        leftcount = in.readInt();
        money = in.readFloat();
    }

    public static final Creator<GoodInfo> CREATOR = new Creator<GoodInfo>() {
        @Override
        public GoodInfo createFromParcel(Parcel in) {
            return new GoodInfo(in);
        }

        @Override
        public GoodInfo[] newArray(int size) {
            return new GoodInfo[size];
        }
    };

    public String getGoodId() {
        return goodId;
    }

    public void setGoodId(String goodId) {
        this.goodId = goodId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconurl() {
        return iconurl;
    }

    public void setIconurl(String iconurl) {
        this.iconurl = iconurl;
    }

    public BmobFile getIcon() {
        return icon;
    }

    public void setIcon(BmobFile icon) {
        this.icon = icon;
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

    public String getDetailurl() {
        return Detailurl;
    }

    public void setDetailurl(String detailurl) {
        Detailurl = detailurl;
    }

    public int getLeftcount() {
        return leftcount;
    }

    public void setLeftcount(int leftcount) {
        this.leftcount = leftcount;
    }

    public int getChangedcount() {

        return changedcount;
    }

    public void setChangedcount(int changedcount) {
        this.changedcount = changedcount;
    }

    @Override
    public String toString() {
        return "GoodInfo{" +
                "goodId='" + goodId + '\'' +
                ", name='" + name + '\'' +
                ", iconurl='" + iconurl + '\'' +
                ", icon=" + icon +
                ", points=" + points +
                ", money=" + money +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(goodId);
        dest.writeString(name);
        dest.writeString(iconurl);
        dest.writeString(Detailurl);
        dest.writeInt(points);
        dest.writeInt(changedcount);
        dest.writeInt(leftcount);
        dest.writeFloat(money);
    }
}
