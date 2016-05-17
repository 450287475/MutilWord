package com.wangdao.mutilword.utils;

import com.wangdao.mutilword.bean.LrcBean;
import com.wangdao.mutilword.bean.LrcLineContentBean;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by gourdboy on 2016/5/13.
 */
public class ParseLyric
{
    public static LrcBean transformString(String string, String url)
    {
        String[] str1 = string.split("\n");
        LrcBean lrcBean = new LrcBean(url);
        List<LrcLineContentBean> infos = new ArrayList<>();
        for(String str:str1)
        {
            long milliseconds;
            String currentContent = "";
            if(str.startsWith("[ti:"))
            {
                lrcBean.title = str.substring(4,str.length()-1);
            }
            else if(str.startsWith("[ar:"))
            {
                lrcBean.singer = str.substring(4,str.length()-1);
            }
            else if(str.startsWith("[al:"))
            {
                lrcBean.album = str.substring(4,str.length()-1);
            }
            else
            {
                String reg = "\\[(\\d{2}:\\d{2}\\.\\d{2})\\]";
                Pattern pattern = Pattern.compile(reg);
                Matcher matcher = pattern.matcher(str);
                while(matcher.find())
                {
                    String msg = matcher.group(1);
                    milliseconds  = strToLong(msg);
                    String[] content = pattern.split(str);
                    for(int i=1;i<content.length;i++)
                    {
                        if(i==content.length-1)
                        {
                            currentContent = content[i];
                        }
                    }
                    infos.add(new LrcLineContentBean(milliseconds, currentContent));
                }
            }
        }
        lrcBean.infos = infos;
        return lrcBean;
    }
    private static long strToLong(String msg)
    {
        String[] s = msg.split(":");
        int min = Integer.parseInt(s[0]);
        String[] ss = s[1].split("\\.");
        int sec = Integer.parseInt(ss[0]);
        int mill = Integer.parseInt(ss[1]);
        return min*60*1000+sec*1000+mill*10;
    }
}
