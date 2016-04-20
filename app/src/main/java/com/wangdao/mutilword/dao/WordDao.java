package com.wangdao.mutilword.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wangdao.mutilword.bean.Word_info;

import java.util.ArrayList;

/**
 * Created by Mumuseng on 2016/4/19.
 */
public class WordDao {


    //查找n个未背诵过的单词返回
    public static ArrayList<Word_info> selectNoRepeatWord(Context context,String dbName,int num) {
        ArrayList<Word_info> word_infos = new ArrayList<>();
        SQLiteDatabase db = context.openOrCreateDatabase(context.getFilesDir() + "/" + dbName, Context.MODE_PRIVATE, null);
        String tableName = dbName.substring(0, dbName.indexOf("."));
        Cursor cursor = db.query(tableName, null, "repeat=?", new String[]{"0"}, null, null, null, num + "");
        while (cursor.moveToNext()) {
            String word = cursor.getString(cursor.getColumnIndex("word"));
            String trans = cursor.getString(cursor.getColumnIndex("trans"));
            String phonetic = cursor.getString(cursor.getColumnIndex("phonetic"));
            int repeat = cursor.getInt(cursor.getColumnIndex("repeat"));
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String tags = cursor.getString(cursor.getColumnIndex("tags"));
            word_infos.add(new Word_info(word, trans, phonetic, tags, repeat, id, -1));
        }
        cursor.close();
        db.close();
        return word_infos;
    }

    //删
    public static void deleteWord(Context context,String dbName,int id){
        SQLiteDatabase db = context.openOrCreateDatabase(context.getFilesDir() + "/" + dbName, Context.MODE_PRIVATE, null);
        String tableName = dbName.substring(0, dbName.indexOf("."));
        db.delete(tableName,"_id=?",new String[]{id+""});
        db.close();
    }
    //改
    //查
}
