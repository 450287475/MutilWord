package com.wangdao.mutilword.utils;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by haijun on 2016/3/28.
 */
public class Md5Utils  {
    public static String getMd5Message(String password){
        String resultPassword="";
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            byte[] digest = md5.digest(password.getBytes());
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b:digest) {
                int i = b & 0xff;
                String s = Integer.toHexString(i);
                if (s.length()==1){
                    stringBuffer.append(0);
                }
                stringBuffer.append(s);
            }
            resultPassword = stringBuffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return resultPassword;
    }

    public static String getApkMd5Message(String path){
        String resultPassword="";
        byte[] digest = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            File file = new File(path);
            if (file.exists()){
                FileInputStream fileInputStream = new FileInputStream(file);
                byte[] bytes = new byte[1025];
                int read= -1;
                while ((read=fileInputStream.read(bytes,0,bytes.length))!=-1){
                    md5.update(bytes,0,read);
                }
                digest = md5.digest();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        if (digest!=null){
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b:digest) {
                int i = b & 0xff;
                String s = Integer.toHexString(i);
                if (s.length()==1){
                    stringBuffer.append(0);
                }
                stringBuffer.append(s);
            }
            resultPassword = stringBuffer.toString();
        }
        return resultPassword;
    }
}
