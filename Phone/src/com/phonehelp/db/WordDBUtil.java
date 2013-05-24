package com.phonehelp.db;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class WordDBUtil {

	private DBOpenHelper mOpenHelper;
	private SQLiteDatabase db;
	private Context context;

	public WordDBUtil(Context context) {
		this.context = context;
	}

	public WordDBUtil open() throws SQLException {
		mOpenHelper = new DBOpenHelper(context, DBOpenHelper.DATABASE_NAME,
				null, DBOpenHelper.VERSION);

		return this;
	}

	public Cursor query() {
		db = mOpenHelper.getReadableDatabase();
		String[] columns = new String[] { DBOpenHelper.WORD,
				DBOpenHelper.BLOCKEDTIMES };
		Cursor cursor = db.query(DBOpenHelper.TABLE_WORD, columns,
				DBOpenHelper.TAG + "=1", null, null, null,
				DBOpenHelper.BLOCKEDTIMES + " DESC ");
		// while(cursor.moveToNext()){
		// list.add(cursor.getString(0));
		// }

		return cursor;
	}

	public ArrayList<String> query2() {
		db = mOpenHelper.getReadableDatabase();
		String[] columns = new String[] { DBOpenHelper.WORD };
		ArrayList<String> list = new ArrayList<String>();
		Cursor cursor = db.query(DBOpenHelper.TABLE_WORD, columns,
				DBOpenHelper.TAG + "=1", null, null, null,
				DBOpenHelper.BLOCKEDTIMES + " DESC");
		while (cursor.moveToNext()) {
			list.add(cursor.getString(0));
		}

		return list;
	}

	public void addWord(String word) {
		if (this.isExist(word)) {
			ContentValues contentValues = new ContentValues();
			contentValues.put(DBOpenHelper.TAG, 1);

			db = mOpenHelper.getWritableDatabase();

			db.update(DBOpenHelper.TABLE_WORD, contentValues, DBOpenHelper.WORD
					+ " = ?", new String[] { word });
		} else {
			db = mOpenHelper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(DBOpenHelper.WORD, word);
			db.insert(DBOpenHelper.TABLE_WORD, null, values);
		}

	}

	public Boolean isExist(String word) {
		db = mOpenHelper.getReadableDatabase();
		String[] columns = new String[] { DBOpenHelper.WORD };
		Cursor cousor = db.query(DBOpenHelper.TABLE_WORD, columns, "word =?",
				new String[] { word }, null, null, null);
		if (cousor.getCount() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public void plusOne(String word) {
		db = mOpenHelper.getWritableDatabase();
		String[] col = new String[] { DBOpenHelper.BLOCKEDTIMES };
		Cursor cursor = db.query(DBOpenHelper.TABLE_WORD, col,
				DBOpenHelper.WORD + " = ? and " + DBOpenHelper.TAG + "= 1",
				new String[] { word }, null, null, null);
		cursor.moveToFirst();
		int i = cursor.getInt(0);
		i++;
		ContentValues values = new ContentValues();
		values.put(DBOpenHelper.BLOCKEDTIMES, i);
		db.update(DBOpenHelper.TABLE_WORD, values, DBOpenHelper.WORD
				+ " = ? and " + DBOpenHelper.TAG + "= 1", new String[] { word });
		db.close();
	}

	public void delete(ArrayList<String> list) {
		db = mOpenHelper.getWritableDatabase();
		db.beginTransaction();
		try {
			for (int i = 0; i < list.size(); i++) {
				ContentValues values = new ContentValues();
				values.put(DBOpenHelper.TAG, 0);
				db.update(DBOpenHelper.TABLE_WORD, values, DBOpenHelper.WORD
						+ "=?", new String[] { list.get(i) });
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}

	}

	public void closeDB() {
		if (db.isOpen()) {
			db.close();
		}

	}
}
