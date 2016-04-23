package com.wangdao.mutilword.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wangdao.mutilword.bean.Word_info;
import com.wangdao.mutilword.constant.Constant;
import com.wangdao.mutilword.db.RepeatWordDbhelper;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Mumuseng on 2016/4/19.
 */
public class RepeatWordDao {

    public final RepeatWordDbhelper helper;
    public final String tableName;

    public RepeatWordDao(Context context, String dbName, int version) {
        helper = new RepeatWordDbhelper(context, dbName, version);
        tableName = dbName.substring(0, dbName.indexOf("."));
    }

    //查询是否已经有相同的单词在数据库里,若有,返回该单词.否则返回null
    public Word_info isExist(String wordName){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(tableName, null, "word=?", new String[]{wordName}, null, null, null);
        while (cursor.moveToNext()){
            int repeat = cursor.getInt(cursor.getColumnIndex("repeat"));
            long date = cursor.getLong(cursor.getColumnIndex("date"));
            String word = cursor.getString(cursor.getColumnIndex("word"));
            String trans = cursor.getString(cursor.getColumnIndex("trans"));
            String phonetic = cursor.getString(cursor.getColumnIndex("phonetic"));
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String tags = cursor.getString(cursor.getColumnIndex("tags"));
             cursor.close();
            db.close();
            return new Word_info(word, trans, phonetic, tags, repeat, id, date);
        }
        cursor.close();
        db.close();
        return null;
    }

    //增加背过的单词
    public void insert(Word_info word_info){
        SQLiteDatabase db = helper.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("word",word_info.getWord());
        contentValues.put("trans",word_info.getTrans());
        contentValues.put("phonetic",word_info.getPhonetic());
        contentValues.put("tags",word_info.getTags());
        contentValues.put("repeat",word_info.getRepeat());
      //  contentValues.put("id",word_info.getId());
        contentValues.put("date",word_info.getDate());

        db.insert(tableName, null, contentValues);

        db.close();
    }

    //更新背过的单词的时间和次数
    public void update(int id,int repeat,long date){
        SQLiteDatabase db = helper.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("date",date);
        contentValues.put("repeat",repeat);
        db.update(tableName,contentValues,"_id=?",new String[]{id+""});
        db.close();
    }

    //取出老单词库里需要背诵的单词
    public ArrayList<Word_info> getOldWord(){
        ArrayList<Word_info> word_infos = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(true, tableName, null, null, null, null, null, null, null);
        long time = new Date().getTime();
        while (cursor.moveToNext()){
            long date = cursor.getLong(cursor.getColumnIndex("date"));
            int repeat = cursor.getInt(cursor.getColumnIndex("repeat"));
            long interval = time - date;//距离上一次背诵的间隔

            //判断是否已经到达背诵时间
            switch (repeat){
                case 0://0表示还没背过,则直接加入集合
                    break;
                case 1:
                    if(interval< Constant.fiveMin){
                        continue;
                    }
                    break;
                case 2:
                    if(interval< Constant.halfHour){
                        continue;
                    }
                    break;
                case 3:
                    if(interval< Constant.halfDay){
                        continue;
                    }
                    break;
                case 4:
                    if(interval< Constant.oneDay){
                        continue;
                    }
                    break;
                case 5:
                    if(interval< Constant.twoDay){
                        continue;
                    }
                    break;
                case 6:
                    if(interval< Constant.fourDay){
                        continue;
                    }
                    break;
                case 7:
                    if(interval< Constant.sevenDay){
                        continue;
                    }
                    break;
                case 8:
                    if(interval< Constant.halfMonth){
                        continue;
                    }
                    break;
            }
            if(repeat>=9&&interval< Constant.halfMonth){
                continue;
            }
            //经过所有判断都没有continue,则将该单词加入集合
            String word = cursor.getString(cursor.getColumnIndex("word"));
            String trans = cursor.getString(cursor.getColumnIndex("trans"));
            String phonetic = cursor.getString(cursor.getColumnIndex("phonetic"));
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String tags = cursor.getString(cursor.getColumnIndex("tags"));
            System.out.println(new Word_info(word, trans, phonetic, tags, repeat, id, date));
            word_infos.add(new Word_info(word, trans, phonetic, tags, repeat, id, date));
        }
        cursor.close();
        db.close();
        return word_infos;
    }

    //取出所有背过的单词
    public ArrayList<Word_info> getWord(){
        ArrayList<Word_info> word_infos = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(true, tableName, null, null, null, "word", null, null, null);
        while (cursor.moveToNext()){
            int repeat = cursor.getInt(cursor.getColumnIndex("repeat"));
            if(repeat==0){
                continue;
            }
            long date = cursor.getLong(cursor.getColumnIndex("date"));
            String word = cursor.getString(cursor.getColumnIndex("word"));
            String trans = cursor.getString(cursor.getColumnIndex("trans"));
            String phonetic = cursor.getString(cursor.getColumnIndex("phonetic"));
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String tags = cursor.getString(cursor.getColumnIndex("tags"));
         //   System.out.println(new Word_info(word, trans, phonetic, tags, repeat, id, date));
            word_infos.add(new Word_info(word, trans, phonetic, tags, repeat, id, date));
        }
        cursor.close();
        db.close();
        return word_infos;
    }
}
