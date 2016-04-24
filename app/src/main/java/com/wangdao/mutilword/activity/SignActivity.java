package com.wangdao.mutilword.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wangdao.mutilword.R;
import com.wangdao.mutilword.application.ApplicationInfo;
import com.wangdao.mutilword.bean.ProgressInfo;
import com.wangdao.mutilword.bean.SignDateInfo;
import com.wangdao.mutilword.calendar.SignCalendar;
import com.wangdao.mutilword.dao.SignDao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class SignActivity extends Activity {

    private String date = null;// 设置默认选中的日期  格式为 “2014-04-05” 标准DATE格式
    private TextView popupwindow_calendar_month;
    private SignCalendar calendar;
    private Button btn_signIn;
    private List<String> list = new ArrayList<String>(); //设置标记列表
    SignDao dbManager;
    boolean isinput=false;
    private String date1 = null;//单天日期
    public Button bt_calendar_sync;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_calendar);

        // 初始化DBManager
        dbManager = new SignDao(this);
        SimpleDateFormat    formatter    =   new    SimpleDateFormat    ("yyyy-MM-dd");
        Date    curDate    =   new    Date(System.currentTimeMillis());//获取当前时间
        date1 =formatter.format(curDate);

        popupwindow_calendar_month = (TextView) findViewById(R.id.popupwindow_calendar_month);
        btn_signIn = (Button) findViewById(R.id.btn_signIn);
        calendar = (SignCalendar) findViewById(R.id.popupwindow_calendar);
        bt_calendar_sync = (Button) findViewById(R.id.bt_calendar_sync);
        popupwindow_calendar_month.setText(calendar.getCalendarYear() + "年"
                + calendar.getCalendarMonth() + "月");
        if (null != date) {
            int years = Integer.parseInt(date.substring(0,
                    date.indexOf("-")));
            int month = Integer.parseInt(date.substring(
                    date.indexOf("-") + 1, date.lastIndexOf("-")));
            popupwindow_calendar_month.setText(years + "年" + month + "月");

            calendar.showCalendar(years, month);
            calendar.setCalendarDayBgColor(date,
                    R.drawable.calendar_date_focused);
        }

        final int signDays = query();
        bt_calendar_sync.setText("共签到了"+signDays+"天,与云端同步");
        bt_calendar_sync.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                bt_calendar_sync.setText("准备开始同步...");
                bt_calendar_sync.setEnabled(false);
                final List<SignDateInfo> signDateInfoList = dbManager.query();
                final BmobQuery<SignDateInfo> query = new BmobQuery<>();
                query.addWhereEqualTo("username", ApplicationInfo.userInfo.getUsername());
                query.setLimit(Integer.MAX_VALUE);
                query.findObjects(SignActivity.this, new FindListener<SignDateInfo>() {
                    @Override
                    public void onSuccess(List<SignDateInfo> list) {
                        ArrayList<SignDateInfo> insertDbsignDate = new ArrayList<>();
                        for(SignDateInfo signDateInfo:list){
                                SignDateInfo isSignInfo = dbManager.isSign(signDateInfo.getDate());
                                if(isSignInfo!=null){
                                    signDateInfoList.remove(isSignInfo);
                                }else {
                                    insertDbsignDate.add(signDateInfo);
                                }
                            }
                        dbManager.add(insertDbsignDate);
                        final ProgressInfo progressInfo = new ProgressInfo();
                        progressInfo.total = signDateInfoList.size();
                        reFreshSignDate(progressInfo);
                        for (SignDateInfo signDateInfo:signDateInfoList){
                            signDateInfo.setUsername(ApplicationInfo.userInfo.getUsername());
                            signDateInfo.save(SignActivity.this, new SaveListener() {
                                @Override
                                public void onSuccess() {
                                    progressInfo.total--;
                                    reFreshSignDate(progressInfo);
                                }

                                @Override
                                public void onFailure(int i, String s) {
                                    progressInfo.total--;
                                    progressInfo.failure++;
                                    reFreshSignDate(progressInfo);
                                }
                            });
                        }

                    }

                    private void reFreshSignDate(ProgressInfo progressInfo) {
                        if(progressInfo.total==0){
                            Toast.makeText(SignActivity.this, "同步完成,同步失败"+progressInfo.failure+"个", Toast.LENGTH_SHORT).show();
                            List<SignDateInfo> query1 = dbManager.query();
                            int signDays=query1.size();
                            bt_calendar_sync.setText("共签到了"+signDays+"天,与云端同步");
                            bt_calendar_sync.setEnabled(true);
                            query();
                        }else {
                            bt_calendar_sync.setText("正在同步,还有"+progressInfo.total+"天需要同步");
                        }
                    }

                    @Override
                    public void onError(int i, String s) {
                        Toast.makeText(SignActivity.this, "网络异常,请稍后再同步", Toast.LENGTH_SHORT).show();
                        List<SignDateInfo> query1 = dbManager.query();
                        int signDays=query1.size();
                        bt_calendar_sync.setText("共签到了"+signDays+"天,与云端同步");
                        bt_calendar_sync.setEnabled(true);
                    }
                });

            }
        });

        if(isinput){
            btn_signIn.setText("今日已签，明日继续");
            btn_signIn.setBackgroundResource(R.drawable.button_gray);
            btn_signIn.setEnabled(false);
        }
        btn_signIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Date today= calendar.getThisday();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
           /* calendar.removeAllMarks();
           list.add(df.format(today));
           calendar.addMarks(list, 0);*/
                //将当前日期标示出来
                add(df.format(today));
                //calendar.addMark(today, 0);
                query();
                HashMap<String, Integer> bg = new HashMap<String, Integer>();

                calendar.setCalendarDayBgColor(today, R.drawable.bg_sign_today);
                btn_signIn.setText("今日已签，明日继续");
                btn_signIn.setBackgroundResource(R.drawable.button_gray);
                btn_signIn.setEnabled(false);
                if(ApplicationInfo.userInfo!=null){
                    ApplicationInfo.userInfo.userpoints+=100;
                }
                System.out.println("你今天获得了100积分,当前总积分是"+ApplicationInfo.userInfo.userpoints);
                Toast.makeText(SignActivity.this,"你今天获得了100积分,当前总积分是"+ApplicationInfo.userInfo,Toast.LENGTH_SHORT).show();
                ApplicationInfo.userInfo.update(SignActivity.this,      ApplicationInfo.userInfo.getObjectId(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(SignActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
                        System.out.println( "更新成功");
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(SignActivity.this, "更新失败", Toast.LENGTH_SHORT).show();
                        System.out.println( "更新失败"+s);
                    }
                });
            }
        });
        //监听所选中的日期
        //		calendar.setOnCalendarClickListener(new OnCalendarClickListener() {
        //
        //			public void onCalendarClick(int row, int col, String dateFormat) {
        //				int month = Integer.parseInt(dateFormat.substring(
        //						dateFormat.indexOf("-") + 1,
        //						dateFormat.lastIndexOf("-")));
        //
        //				if (calendar.getCalendarMonth() - month == 1//跨年跳转
        //						|| calendar.getCalendarMonth() - month == -11) {
        //					calendar.lastMonth();
        //
        //				} else if (month - calendar.getCalendarMonth() == 1 //跨年跳转
        //						|| month - calendar.getCalendarMonth() == -11) {
        //					calendar.nextMonth();
        //
        //				} else {
        //					list.add(dateFormat);
        //					calendar.addMarks(list, 0);
        //					calendar.removeAllBgColor();
        //					calendar.setCalendarDayBgColor(dateFormat,
        //							R.drawable.calendar_date_focused);
        //					date = dateFormat;//最后返回给全局 date
        //				}
        //			}
        //		});

        //监听当前月份
        calendar.setOnCalendarDateChangedListener(new SignCalendar.OnCalendarDateChangedListener() {
            public void onCalendarDateChanged(int year, int month) {
                popupwindow_calendar_month
                        .setText(year + "年" + month + "月");
            }
        });
    }

    public void add(String date)
    {
        ArrayList<SignDateInfo> persons = new ArrayList<SignDateInfo>();

        SignDateInfo person1 = new SignDateInfo(date,"true");

        persons.add(person1);

        dbManager.add(persons);
    }

    public int query()
    {
        List<SignDateInfo> persons = dbManager.query();

        for (SignDateInfo person : persons)
        {
            list.add(person.date);
            if(date1.equals(person.getDate())){
                isinput=true;
            }
        }
        calendar.addMarks(list, 0);
        return list.size();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        dbManager.closeDB();// 释放数据库资源
    }

}
