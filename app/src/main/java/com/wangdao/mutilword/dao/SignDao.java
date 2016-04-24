package com.wangdao.mutilword.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wangdao.mutilword.bean.SignDateInfo;
import com.wangdao.mutilword.db.SignDbHelper;

import java.util.ArrayList;
import java.util.List;

public class SignDao
{
    private SignDbHelper helper;
    private SQLiteDatabase db;

    public SignDao(Context context)
    {
        helper = new SignDbHelper(context);
        // 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
        // mFactory);
        // 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }

    //根据日期查询数据库里是否存在该日期
    public SignDateInfo isSign(String date){
        Cursor cursor = db.query(SignDbHelper.TABLE_NAME, null, "date1=?", new String[]{date}, null, null, null);
        while (cursor.moveToNext()){
            SignDateInfo person = new SignDateInfo();
            person.date = cursor.getString(cursor.getColumnIndex("date1"));
            person.isselct = cursor.getString(cursor.getColumnIndex("isselct"));
            return person;
        }
        return null;
    }

    /**
     * add persons
     * 
     * @param persons
     */
    public void add(List<SignDateInfo> persons)
    {
        // 采用事务处理，确保数据完整性
        db.beginTransaction(); // 开始事务
        try
        {
            for (SignDateInfo person : persons)
            {
                db.execSQL("INSERT INTO " + SignDbHelper.TABLE_NAME
                        + " VALUES(?, ?)", new Object[] { person.date,
                        person.isselct});
                // 带两个参数的execSQL()方法，采用占位符参数？，把参数值放在后面，顺序对应
                // 一个参数的execSQL()方法中，用户输入特殊字符时需要转义
                // 使用占位符有效区分了这种情况
            }
            db.setTransactionSuccessful(); // 设置事务成功完成
        }
        finally
        {
            db.endTransaction(); // 结束事务
        }
    }

    /**
     * update person's age
     * 
     * @param person
     */
    public void updateAge(SignDateInfo person)
    {
        ContentValues cv = new ContentValues();
        cv.put("isselct", person.isselct);
        db.update(SignDbHelper.TABLE_NAME, cv, "isselct = ?",
                new String[] { person.isselct });
    }

    /**
     * delete old person
     * 
     * @param person
     */
    public void deleteOldPerson(SignDateInfo person)
    {
        db.delete(SignDbHelper.TABLE_NAME, "isselct= ?",
                new String[] { String.valueOf(person.isselct) });
    }

    /**
     * query all persons, return list
     * 
     * @return List<Person>
     */

    //获得所有日期的集合
    public List<SignDateInfo> query()
    {
        ArrayList<SignDateInfo> persons = new ArrayList<SignDateInfo>();
        Cursor c = queryTheCursor();
        while (c.moveToNext())
        {
        	SignDateInfo person = new SignDateInfo();
            person.date = c.getString(c.getColumnIndex("date1"));
            person.isselct = c.getString(c.getColumnIndex("isselct"));
            persons.add(person);
        }
        c.close();
        return persons;
    }

    /**
     * query all persons, return cursor
     * 
     * @return Cursor
     */
    public Cursor queryTheCursor()
    {
        Cursor c = db.rawQuery("SELECT * FROM " + SignDbHelper.TABLE_NAME,
                null);
        return c;
    }

    /**
     * close database
     */
    public void closeDB()
    {
        // 释放数据库资源
        db.close();
    }

}
