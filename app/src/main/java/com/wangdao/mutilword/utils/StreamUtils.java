package com.wangdao.mutilword.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by MonkeyzZi on 2016/4/22.
 */
public class StreamUtils {
    //将输入流读取成String后返回
    public static String readFromStream(InputStream in) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int len = 0;
        byte[] bytes = new byte[1024];

        while ((len= in.read(bytes))!=-1){
            baos.write(bytes,0,len);

        }
        String result = baos.toString();
        in.close();
        baos.close();
        return  result ;
    }
}
