package com.phonehelp.db;

import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SmsDBUtil {

	private DBOpenHelper mOpenHelper;
	private SQLiteDatabase db;

	public SmsDBUtil(Context context) {
		mOpenHelper = new DBOpenHelper(context);
	}

	public Cursor querySMS() {
		db = mOpenHelper.getReadableDatabase();
		String[] col = new String[] { SqlStatement.COLUMN_PHONENUMBER,
				SqlStatement.COLUMN_DATE, SqlStatement.COLUMN_CONTENT,
				SqlStatement.COLUMN_TYPE, SqlStatement.COLUMN_WORD };
		Cursor cursor = db.query(SqlStatement.DB_TABLE_SMS, col, null, null,
				null, null, SqlStatement.COLUMN_DATE + " DESC");
		return cursor;
	}

	public void addSmsRecord(String num, Date date, String content, int type) {
		db = mOpenHelper.getWritableDatabase();
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// String time = sdf.format(date);
		ContentValues values = new ContentValues();
		values.put(SqlStatement.COLUMN_PHONENUMBER, num);
		values.put(SqlStatement.COLUMN_DATE, date.getTime());
		values.put(SqlStatement.COLUMN_CONTENT, content);
		values.put(SqlStatement.COLUMN_TYPE, type);
		db.insert(SqlStatement.DB_TABLE_SMS, null, values);
		// db.close();
	}

	public void addSmsRecord(String num, Date date, String content, int type,
			String word) {
		db = mOpenHelper.getWritableDatabase();
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// String time = sdf.format(date);
		ContentValues values = new ContentValues();
		values.put(SqlStatement.COLUMN_PHONENUMBER, num);
		values.put(SqlStatement.COLUMN_DATE, date.getTime());
		values.put(SqlStatement.COLUMN_CONTENT, content);
		values.put(SqlStatement.COLUMN_TYPE, type);
		values.put(SqlStatement.COLUMN_WORD, word);
		db.insert(SqlStatement.DB_TABLE_SMS, null, values);
		// db.close();
	}

	public void deleteRecord(ArrayList<Date> dates) {
		db = mOpenHelper.getWritableDatabase();
		db.beginTransaction();
		try {
			for (int i = 0; i < dates.size(); i++) {
				db.delete(SqlStatement.DB_TABLE_SMS, SqlStatement.COLUMN_DATE
						+ " = ?", new String[] { dates.get(i).getTime() + "" });
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}

	}

	public void closeDB() {
		db.close();
	}
}
