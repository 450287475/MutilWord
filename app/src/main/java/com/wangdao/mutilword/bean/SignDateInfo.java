package com.wangdao.mutilword.bean;

import cn.bmob.v3.BmobObject;

public class SignDateInfo extends BmobObject {
	public String date;
	public String isselct;
	public String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public SignDateInfo() {
	}

	public SignDateInfo(String date, String isselct) {
		this.date = date;
		this.isselct = isselct;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getIsselct() {
		return isselct;
	}

	public void setIsselct(String isselct) {
		this.isselct = isselct;
	}

	@Override
	public boolean equals(Object o) {
		SignDateInfo signDateInfo = (SignDateInfo) o;
		if(date.equals(signDateInfo.date)){
			return true;
		}
		return false;
	}
}
