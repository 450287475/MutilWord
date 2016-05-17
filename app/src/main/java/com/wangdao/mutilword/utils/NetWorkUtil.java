package com.wangdao.mutilword.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;


import com.wangdao.mutilword.constant.IConstants;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by gourdboy on 2016/5/16.
 */
public class NetWorkUtil implements IConstants
{
    public static void getImageFromNet(String imageurl, Handler handler, File pathDir)
    {
        new AsyncTask<Object,Integer,Bitmap>()
        {
            private String imageurl;
            private Handler mHandler;
            private File pathDir;
            @Override
            protected Bitmap doInBackground(Object... params)
            {
                imageurl = (String) params[0];
                mHandler = (Handler) params[1];
                pathDir = (File) params[2];
                Bitmap bitmap = null;
                HttpURLConnection conn = null;
                InputStream is = null;
                try
                {
                    URL url = new URL(imageurl);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(5000);
                    int responseCode = conn.getResponseCode();
                    if(responseCode==200)
                    {
                        is = conn.getInputStream();
                        bitmap = BitmapFactory.decodeStream(is);
                        LocalDirUtil.saveImg2Dir(bitmap,imageurl,pathDir);
                    }
                }
                catch (MalformedURLException e)
                {
                    e.printStackTrace();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    if(conn!=null)
                    {
                        conn.disconnect();
                    }
                    if(is!=null)
                    {
                        try
                        {
                            is.close();
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
                return bitmap;
            }
            @Override
            protected void onPostExecute(Bitmap bitmap)
            {
                LocalCacheUtil.saveImg2Cache(imageurl,bitmap);
                Message msg = mHandler.obtainMessage();
                msg.what = DOWNLOAD_IMG_COMPLETE;
                msg.obj = bitmap;
                mHandler.sendMessage(msg);
            }
        }.execute(imageurl,handler,pathDir);
    }
}
