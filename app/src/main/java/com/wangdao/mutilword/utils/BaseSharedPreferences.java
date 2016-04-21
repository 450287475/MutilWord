package com.wangdao.mutilword.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by yxd on 2016/4/21 for exam part.
 */
public abstract class BaseSharedPreferences {
	
	/**
	 * 设置回调接口
	 *
	 */
	public interface PreferenceChangedCallback {
		public void onSettingChanged(SharedPreferences sp, String key);
	}
	
	private static SharedPreferences sp;
	private Map<String, PreferenceChangedCallback> callbacks = new HashMap<String, PreferenceChangedCallback>();
	
	public BaseSharedPreferences(Context ctx) {
		sp = ctx.getSharedPreferences(getSpNmae(), Context.MODE_PRIVATE);
		OnSharedPreferenceChangeListener listener = new OnSharedPreferenceChangeListener() {
			
			@Override
			public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
					String key) {
				PreferenceChangedCallback cb = callbacks.get(key);
				if (cb != null) {
					cb.onSettingChanged(sharedPreferences, key);
				}
			}
		};
		sp.registerOnSharedPreferenceChangeListener(listener);
	}

	public void addCallback(String key, PreferenceChangedCallback callback) {
		callbacks.put(key, callback);
	}
	
	public abstract String getSpNmae();

	public static SharedPreferences getSp() {
		return sp;
	}
}
