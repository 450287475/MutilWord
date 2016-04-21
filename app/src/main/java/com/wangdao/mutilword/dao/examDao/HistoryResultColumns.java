package com.wangdao.mutilword.dao.examDao;


/**
 * Created by yxd on 2016/4/21.
 */
//挑战模式历史数据表的相关操作
public class HistoryResultColumns extends BaseColumns {
	public final static String TABLE_NAME = "historyResult";
	public final static String COLUMN_USER_NAME = "userName";
	public final static String COLUMN_CUR_TIME = "curTime";
	public final static String COLUMN_USE_TIME = "useTime";
	public final static String COLUMN_HIS_RESULT = "hisResult";

	public static String getCreateTableSql() {

		return CREATE_TABLE + TABLE_NAME + START_SQL + COLUMN_USER_NAME + VARCHAR_64 + COLUMN_CUR_TIME + VARCHAR_64 + COLUMN_USE_TIME + VARCHAR_64
				+ COLUMN_HIS_RESULT + " varchar(64)" + ")";
	}
}
