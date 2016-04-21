package com.wangdao.mutilword.db.ExamDb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.wangdao.mutilword.dao.examDao.AnswerColumns;
import com.wangdao.mutilword.dao.examDao.CollectColumns;
import com.wangdao.mutilword.dao.examDao.ErrorColumns;
import com.wangdao.mutilword.dao.examDao.ExamErrorColumns;
import com.wangdao.mutilword.dao.examDao.ExamResultColumns;
import com.wangdao.mutilword.dao.examDao.HistoryResultColumns;

/**
 * Created by yxd on 2016/4/21 for exam part.
 */
public class MySqliteHelper extends SQLiteOpenHelper{

	private final static int VERSION = 1;
	
	public MySqliteHelper(Context context) {
		super(context, "RailwaySystem.db", null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(AnswerColumns.getCreateTableSql());
		db.execSQL(CollectColumns.getCreateTableSql());
		db.execSQL(ErrorColumns.getCreateTableSql());
		db.execSQL(ExamResultColumns.getCreateTableSql());
		db.execSQL(ExamErrorColumns.getCreateTableSql());
		db.execSQL(HistoryResultColumns.getCreateTableSql());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
