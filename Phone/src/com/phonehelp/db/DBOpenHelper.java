package com.phonehelp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {

	protected DBOpenHelper(Context context) {
		super(context, SqlStatement.DATABASE_NAME, null, SqlStatement.VERSION);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL(SqlStatement.CREATE_TABLE_BLACKLIST);
		db.execSQL(SqlStatement.CREATE_TABLE_MSM);
		db.execSQL(SqlStatement.CREATE_TABLE_WORD);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
