package com.phonehelp.db;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class WordDBUtil {

	private DBOpenHelper mOpenHelper;
	private SQLiteDatabase db;

	public WordDBUtil(Context context) {
		mOpenHelper = new DBOpenHelper(context);
	}

	public Cursor query() {
		db = mOpenHelper.getReadableDatabase();
		String[] columns = new String[] { SqlStatement.COLUMN_WORD,
				SqlStatement.COLUMN_BLOCKED_TIMES };
		Cursor cursor = db.query(SqlStatement.DB_TABLE_WORD, columns,
				SqlStatement.COLUMN_TAG + "=1", null, null, null,
				SqlStatement.COLUMN_BLOCKED_TIMES + " DESC ");
		// while(cursor.moveToNext()){
		// list.add(cursor.getString(0));
		// }

		return cursor;
	}

	public ArrayList<String> query2() {
		db = mOpenHelper.getReadableDatabase();
		String[] columns = new String[] { SqlStatement.COLUMN_WORD };
		ArrayList<String> list = new ArrayList<String>();
		Cursor cursor = db.query(SqlStatement.DB_TABLE_WORD, columns,
				SqlStatement.COLUMN_TAG + "=1", null, null, null,
				SqlStatement.COLUMN_BLOCKED_TIMES + " DESC");
		while (cursor.moveToNext()) {
			list.add(cursor.getString(0));
		}

		return list;
	}

	public void addWord(String word) {
		if (this.isExist(word)) {
			ContentValues contentValues = new ContentValues();
			contentValues.put(SqlStatement.COLUMN_TAG, 1);

			db = mOpenHelper.getWritableDatabase();

			db.update(SqlStatement.DB_TABLE_WORD, contentValues,
					SqlStatement.COLUMN_WORD + " = ?", new String[] { word });
		} else {
			db = mOpenHelper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(SqlStatement.COLUMN_WORD, word);
			db.insert(SqlStatement.DB_TABLE_WORD, null, values);
		}

	}

	public Boolean isExist(String word) {
		db = mOpenHelper.getReadableDatabase();
		String[] columns = new String[] { SqlStatement.COLUMN_WORD };
		Cursor cousor = db.query(SqlStatement.DB_TABLE_WORD, columns,
				"word =?", new String[] { word }, null, null, null);
		if (cousor.getCount() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public void plusOne(String word) {
		db = mOpenHelper.getWritableDatabase();
		String[] col = new String[] { SqlStatement.COLUMN_BLOCKED_TIMES };
		Cursor cursor = db.query(SqlStatement.DB_TABLE_WORD, col,
				SqlStatement.COLUMN_WORD + " = ? and "
						+ SqlStatement.COLUMN_TAG + "= 1",
				new String[] { word }, null, null, null);
		cursor.moveToFirst();
		int i = cursor.getInt(0);
		i++;
		ContentValues values = new ContentValues();
		values.put(SqlStatement.COLUMN_BLOCKED_TIMES, i);
		db.update(SqlStatement.DB_TABLE_WORD, values, SqlStatement.COLUMN_WORD
				+ " = ? and " + SqlStatement.COLUMN_TAG + "= 1",
				new String[] { word });
		db.close();
	}

	public void delete(ArrayList<String> list) {
		db = mOpenHelper.getWritableDatabase();
		db.beginTransaction();
		try {
			for (int i = 0; i < list.size(); i++) {
				ContentValues values = new ContentValues();
				values.put(SqlStatement.COLUMN_TAG, 0);
				db.update(SqlStatement.DB_TABLE_WORD, values,
						SqlStatement.COLUMN_WORD + "=?",
						new String[] { list.get(i) });
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
