package com.wangdao.mutilword.dao.examDao;



/**
 * Created by yxd on 2016/4/21.
 */
//挑战模式结果数据表的相关操作
public class ExamResultColumns extends BaseColumns {
	public final static String TABLE_NAME = "examresult";

	public static String getCreateTableSql() {

		return CREATE_TABLE + TABLE_NAME + START_SQL + COLUMN_TIMU_TITLE + TEXT + COLUMN_TIMU_ONE + VARCHAR_64 + COLUMN_TIMU_TOW
				+ VARCHAR_64 + COLUMN_TIMU_THREE + VARCHAR_64 + COLUMN_TIMU_FOUR + VARCHAR_64 + COLUMN_DAAN_ONE + VARCHAR_64
				+ COLUMN_DAAN_TOW + VARCHAR_64 + COLUMN_DAAN_THREE + VARCHAR_64 + COLUMN_DAAN_FOUR + VARCHAR_64 + COLUMN_DAAN_DETAIL + TEXT
				+ COLUMN_TYPES + VARCHAR_64 + COLUMN_REPLY + END_CREATE_SQL;
	}
}
