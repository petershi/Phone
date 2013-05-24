package com.phonehelp.db;

import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class SmsDBUtil {

	private DBOpenHelper mOpenHelper;
	private SQLiteDatabase db;
	private Context context;

	public SmsDBUtil(Context context) {
		this.context = context;
	}

	public SmsDBUtil open() throws SQLException {
		mOpenHelper = new DBOpenHelper(context, DBOpenHelper.TABLE_SMS, null,
				DBOpenHelper.VERSION);
		return this;
	}

	public Cursor querySMS() {
		db = mOpenHelper.getReadableDatabase();
		String[] col = new String[] { DBOpenHelper.PHONENUMBER,
				DBOpenHelper.DATE, DBOpenHelper.CONTENT, DBOpenHelper.TYPE,
				DBOpenHelper.WORD };
		Cursor cursor = db.query(DBOpenHelper.TABLE_SMS, col, null, null, null,
				null, DBOpenHelper.DATE + " DESC");
		return cursor;
	}

	public void addSmsRecord(String num, Date date, String content, int type) {
		db = mOpenHelper.getWritableDatabase();
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// String time = sdf.format(date);
		ContentValues values = new ContentValues();
		values.put(DBOpenHelper.PHONENUMBER, num);
		values.put(DBOpenHelper.DATE, date.getTime());
		values.put(DBOpenHelper.CONTENT, content);
		values.put(DBOpenHelper.TYPE, type);
		db.insert(DBOpenHelper.TABLE_SMS, null, values);
		// db.close();
	}

	public void addSmsRecord(String num, Date date, String content, int type,
			String word) {
		db = mOpenHelper.getWritableDatabase();
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// String time = sdf.format(date);
		ContentValues values = new ContentValues();
		values.put(DBOpenHelper.PHONENUMBER, num);
		values.put(DBOpenHelper.DATE, date.getTime());
		values.put(DBOpenHelper.CONTENT, content);
		values.put(DBOpenHelper.TYPE, type);
		values.put(DBOpenHelper.WORD, word);
		db.insert(DBOpenHelper.TABLE_SMS, null, values);
		// db.close();
	}

	public void deleteRecord(ArrayList<Date> dates) {
		db = mOpenHelper.getWritableDatabase();
		db.beginTransaction();
		try {
			for (int i = 0; i < dates.size(); i++) {
				db.delete(DBOpenHelper.TABLE_SMS, DBOpenHelper.DATE + " = ?",
						new String[] { dates.get(i).getTime() + "" });
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
