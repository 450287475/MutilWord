package com.wangdao.mutilword.utils;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by gourdboy on 2016/5/16.
 */
public class LocalCacheUtil
{
    private static LruCache<String,Bitmap> cache = new LruCache<>(20000);
    public static void saveImg2Cache(String url, Bitmap bitmap)
    {
        String key = Md5Utils.getMd5Message(url);
        cache.put(key,bitmap);
    }
    public static Bitmap getImgFromCache(String url)
    {
        return cache.get(Md5Utils.getMd5Message(url));
    }
}
