package com.phonehelp.db;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class BlacklistDBUtil {

	private DBOpenHelper mOpenHelper;
	private SQLiteDatabase db;

	public BlacklistDBUtil(Context context) {
		mOpenHelper = new DBOpenHelper(context);
	}

	/**
	 * query all num in DB where tag=1
	 * 
	 * @return
	 */
	public Cursor queryNum() {
		db = mOpenHelper.getReadableDatabase();
		String[] col = { SqlStatement.COLUMN_PHONENUMBER,
				SqlStatement.COLUMN_BLOCKED_TIMES };
		Cursor cursor = db.query(SqlStatement.DB_TABLE_BLACKLIST, col,
				SqlStatement.COLUMN_ID + "=1", null, null, null,
				SqlStatement.COLUMN_BLOCKED_TIMES + " DESC");

		return cursor;
	}

	/**
	 * insert a phone number in DB
	 * <p/>
	 * if the phone number exist in DB, set tag 0 to 1 else insert it
	 * 
	 * @param str
	 * @throws SQLException
	 */
	public void addNum(String str) {

		if (isNumExist(str)) {
			ContentValues contentValues = new ContentValues();
			contentValues.put(SqlStatement.COLUMN_TAG, 1);

			db = mOpenHelper.getWritableDatabase();

			db.update(SqlStatement.DB_TABLE_BLACKLIST, contentValues,
					SqlStatement.COLUMN_PHONENUMBER + " = ?",
					new String[] { str });
		} else {

			try {
				db = mOpenHelper.getWritableDatabase();
				ContentValues values = new ContentValues();
				values.put(SqlStatement.COLUMN_PHONENUMBER, str);
				db.insert(SqlStatement.DB_TABLE_BLACKLIST, null, values);
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

	}

	public void addNum(ArrayList<String> list) {
		db = mOpenHelper.getWritableDatabase();
		db.beginTransaction();
		try {
			for (int i = 0; i < list.size(); i++) {
				ContentValues values = new ContentValues();
				values.put(SqlStatement.COLUMN_PHONENUMBER, list.get(i));
				db.insert(SqlStatement.DB_TABLE_BLACKLIST, null, values);
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}

	public void setNumTag1(ArrayList<String> list) {
		db = mOpenHelper.getWritableDatabase();
		db.beginTransaction();
		try {
			for (int i = 0; i < list.size(); i++) {
				ContentValues contentValues = new ContentValues();
				contentValues.put(SqlStatement.COLUMN_TAG, 1);
				db.update(SqlStatement.DB_TABLE_BLACKLIST, contentValues,
						SqlStatement.COLUMN_PHONENUMBER + " = ?",
						new String[] { list.get(i) });
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}

	}

	/**
	 * if phonenumber exist return true else return false
	 * 
	 * @param str
	 * @return
	 */
	public boolean isNumExist(String str) {
		db = mOpenHelper.getReadableDatabase();
		String[] col = { SqlStatement.COLUMN_PHONENUMBER };

		Cursor cursor = db.query(SqlStatement.DB_TABLE_BLACKLIST, col,
				SqlStatement.COLUMN_PHONENUMBER + " = ?", new String[] { str },
				null, null, null);

		if (cursor.getCount() == 0) {
			Log.i("isNumExist", "false");
			cursor.close();

			return false;
		} else {

			Log.i("isNumExist", "true");
			cursor.close();

			return true;
		}
	}

	public boolean isBlock(String str) {
		db = mOpenHelper.getReadableDatabase();
		String[] col = { SqlStatement.COLUMN_PHONENUMBER };
		Cursor cursor = db.query(SqlStatement.DB_TABLE_BLACKLIST, col,
				SqlStatement.COLUMN_PHONENUMBER + " = ? and "
						+ SqlStatement.COLUMN_TAG + "= 1",
				new String[] { str }, null, null, null);
		if (cursor.getCount() == 0) {
			cursor.close();
			db.close();
			return false;
		} else {
			cursor.close();
			db.close();
			return true;
		}
	}

	/**
	 * if a phonenumber is not longer to use,set the tag=0
	 * 
	 * @param str
	 */
	public void deleteNum(String str) {
		db = mOpenHelper.getWritableDatabase();
		try {
			ContentValues values = new ContentValues();
			values.put(SqlStatement.COLUMN_TAG, 0);
			db.update(SqlStatement.DB_TABLE_BLACKLIST, values,
					SqlStatement.COLUMN_PHONENUMBER + " = ?",
					new String[] { str });
		} catch (SQLException e) {
		}
	}

	public void deleteNum(ArrayList<String> list) {
		db = mOpenHelper.getWritableDatabase();
		db.beginTransaction();
		try {
			for (int i = 0; i < list.size(); i++) {
				ContentValues values = new ContentValues();
				values.put(SqlStatement.COLUMN_TAG, 0);
				db.update(SqlStatement.DB_TABLE_BLACKLIST, values,
						SqlStatement.COLUMN_PHONENUMBER + " = ?",
						new String[] { list.get(i) });
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}

	}

	public void plusOne(String str) {
		db = mOpenHelper.getWritableDatabase();
		String[] col = new String[] { SqlStatement.COLUMN_BLOCKED_TIMES };
		Cursor cursor = db.query(SqlStatement.DB_TABLE_BLACKLIST, col,
				SqlStatement.COLUMN_PHONENUMBER + " = ? and "
						+ SqlStatement.COLUMN_TAG + "= 1",
				new String[] { str }, null, null, null);
		// int i=cursor.getCount();
		cursor.moveToFirst();
		int i = cursor.getInt(0);
		i++;
		ContentValues values = new ContentValues();
		values.put(SqlStatement.COLUMN_BLOCKED_TIMES, i);
		db.update(SqlStatement.DB_TABLE_BLACKLIST, values,
				SqlStatement.COLUMN_PHONENUMBER + " = ? and "
						+ SqlStatement.COLUMN_TAG + "= 1", new String[] { str });

	}

	public void closeDB() {
		if (db != null) {
			db.close();
		}
	}

}
