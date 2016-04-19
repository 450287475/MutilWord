package com.wangdao.mutilword.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mumuseng on 2016/4/19.
 */
public class RepeatWordDbhelper extends SQLiteOpenHelper {

    public final String table;

    public RepeatWordDbhelper(Context context, String name, int version) {
        super(context, name, null, version);
        table = name.substring(0, name.indexOf("."));
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="create table "+table+" (_id integer primary key autoincrement,word verchar(30)," +
                "trans varchar(1000),phonetic verchar(15),tags verchar(10),repeat integer,date integer);";
        db.execSQL(sql);
       /* String sql="alter table "+table+" add repeat integer default 0";
        db.execSQL(sql);*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

          /*  String sql="alter table "+table+" add repeat integer default 0";
            db.execSQL(sql);*/
           /* String sql2="create table CET_6 (_id integer primary key autoincrement,word verchar(30)," +
                    "trans varchar(1000),phonetic verchar(15),tags verchar(10));";
            db.execSQL(sql2);*/


    }
}
