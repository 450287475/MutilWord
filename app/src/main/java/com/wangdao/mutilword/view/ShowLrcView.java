package com.wangdao.mutilword.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;


import com.wangdao.mutilword.bean.LrcContentBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gourdboy on 2016/4/22.
 */
public class ShowLrcView extends TextView
{
    private int width;
    private int height;
    private int index;
    private Paint currentPaint;
    private Paint noCurrentPaint;
    private float textSize;//默认字体大小;
    private float textHeight = 45;
    private float padding = 20;
    private List<LrcContentBean> lrcbeanList = new ArrayList<>();
    private String text;
    public ShowLrcView(Context context)
    {
        super(context);
        initView();
    }
    public ShowLrcView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initView();
    }

    public ShowLrcView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initView();
    }
    private void initView()
    {
        setFocusable(true);//设置可对焦
        //高亮
        currentPaint = new Paint();
        currentPaint.setAntiAlias(true);//抗锯齿，字体更饱满
        currentPaint.setTextAlign(Paint.Align.CENTER);//居中显示
        //非高亮部分
        noCurrentPaint = new Paint();
        noCurrentPaint.setAntiAlias(true);
        noCurrentPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
//        Log.i("ShowLrcView","ONDRAW");
        super.onDraw(canvas);
        if(canvas==null)
        {
            return;
        }
        currentPaint.setColor(Color.argb(210,251, 248, 29));
        noCurrentPaint.setColor(Color.argb(139, 255, 255, 255));
        currentPaint.setTextSize(50);
        currentPaint.setTypeface(Typeface.SERIF);
        textSize = getTextSize();
        noCurrentPaint.setTextSize(textSize);
        noCurrentPaint.setTypeface(Typeface.DEFAULT);
        setText("");
//        Log.i("ShowLrcView",lrcbeanList.get(index).content+"   "+index);
        if(lrcbeanList!=null&&lrcbeanList.size()!=0)
        {
            float tempY = height/2;
            float tempDownY = tempY;
            float tempUpY = tempY;
            String str = lrcbeanList.get(index).content;
            int mindex = getMaxIndex(str,textSize);
//            Log.i("getMaxIndex",str);
            if(mindex==0)
            {
                canvas.drawText(str, width / 2, height / 2, currentPaint);
            }
            else
            {
                tempDownY = tempDownY+textHeight;
                canvas.drawText(str.substring(0,mindex), width / 2, height / 2, currentPaint);
                canvas.drawText(str.substring(mindex),width/2,tempDownY,currentPaint);
            }
            for(int i=index-1;i>=0;i--)
            {
                str = lrcbeanList.get(i).content;
                mindex = getMaxIndex(str,textSize);
                tempUpY = tempUpY-textHeight;
                if(mindex==0)
                {
                    canvas.drawText(str, width / 2, tempUpY, noCurrentPaint);
                }
                else
                {
                    canvas.drawText(str.substring(mindex),width/2,tempUpY,noCurrentPaint);
                    tempUpY = tempUpY-textHeight;
                    canvas.drawText(str.substring(0,mindex), width / 2, tempUpY, noCurrentPaint);
                }
            }
            for(int i=index+1;i<lrcbeanList.size();i++)
            {
                str = lrcbeanList.get(i).content;
                mindex = getMaxIndex(str,textSize);
                tempDownY = tempDownY+textHeight;
                if(mindex==0)
                {
                    canvas.drawText(str, width / 2, tempDownY, noCurrentPaint);
                }
                else
                {
                    canvas.drawText(str.substring(0,mindex), width / 2, tempDownY, noCurrentPaint);
                    tempDownY = tempDownY+textHeight;
                    canvas.drawText(str.substring(mindex),width/2,tempDownY,noCurrentPaint);
                }
            }
//            Log.i("ShowLrcView",lrcbeanList.get(index).content);

        }
        else if(text!=null&&!text.isEmpty())
        {
            canvas.drawText(text, width / 2, height / 2, currentPaint);
        }
        else
        {
            canvas.drawText("准备中，请稍候", width / 2, height / 2, currentPaint);
        }
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        this.height = h;
    }
    public void setIndex(int index)
    {
        this.index = index;
    }
    public void setLrclist(List<LrcContentBean> lrcbeanList)
    {
        this.lrcbeanList = lrcbeanList;
    }
    public void setStr(String str)
    {
        this.text = str;
    }
    private int getMaxIndex(String str,float charwidth)
    {
        float textPixel = str.length()*charwidth;
        float textWidth = width-padding*2;
        int mindex = 0;
        if(textPixel>textWidth)
        {
            mindex = (int) (textWidth/charwidth);
            if(str.charAt(mindex)==' ')
            {
                return mindex;
            }
            else if(str.charAt(mindex)>='a'&&str.charAt(mindex)<='z'||str.charAt(mindex)>='A'&&str.charAt(mindex)<='Z')
            {
                for(int i=mindex;i<str.length();i++)
                {
                    if(str.charAt(i)==' ')
                    {
                        return i;
                    }
                    else if(i==str.length()-1)
                    {
                        return 0;
                    }
                }
            }
            else
            {
                return mindex;
            }
        }
        return mindex;
    }
}
