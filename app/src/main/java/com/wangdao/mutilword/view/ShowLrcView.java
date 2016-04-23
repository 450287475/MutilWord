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
    private float textSize = 40;//默认字体大小;
    private float textHeight = 45;
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
        currentPaint.setColor(Color.argb(210, 0, 0, 0));
        noCurrentPaint.setColor(Color.argb(139, 111, 110, 110));
        currentPaint.setTextSize(50);
        currentPaint.setTypeface(Typeface.SERIF);
        textSize = getTextSize();
        noCurrentPaint.setTextSize(textSize);
        noCurrentPaint.setTypeface(Typeface.DEFAULT);
        setText("");
//        Log.i("ShowLrcView",lrcbeanList.get(index).content+"   "+index);
        if(lrcbeanList!=null&&lrcbeanList.size()!=0)
        {
            canvas.drawText(lrcbeanList.get(index).content, width / 2, height / 2, currentPaint);
            float tempY = height/2;
            for(int i=index-1;i>0;i--)
            {
                tempY = tempY - textHeight;
                canvas.drawText(lrcbeanList.get(i).content,width/2,tempY,noCurrentPaint);
            }
            tempY = height/2;
            for(int i=index+1;i<lrcbeanList.size();i++)
            {
                tempY = tempY + textHeight;
                canvas.drawText(lrcbeanList.get(i).content,width/2,tempY,noCurrentPaint);
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
}
