package com.phonehelp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "phonehelp.db";
	public static final int VERSION = 1;
	public static final String TABLE_BLACKLIST = "blacklist";
	public static final String ID = "id";
	public static final String PHONENUMBER = "phonenumber";
	public static final String BLOCKEDTIMES = "blockedtimes";
	public static final String TAG = "tag";
	public static final String TABLE_CALL = "blocked_call";
	public static final String DATE = "date";
	public static final String TABLE_SMS = "blocked_sms";
	public static final String TYPE = "type";
	public static final String CONTENT = "content";
	public static final String TABLE_WORD = "keyword";
	public static final String WORD = "word";
	public static final String TABLE_BACKUP = "backup";
	public static final String NAME = "name";

	public DBOpenHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, DATABASE_NAME, null, VERSION);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table if not exists " + TABLE_BLACKLIST + "(" + ID
				+ " integer primary key autoincrement ," + PHONENUMBER
				+ " text not null, " + BLOCKEDTIMES + " integer default 0, "
				+ TAG + " integer default 1 )";
		db.execSQL(sql);

		// String csql = "create table if not exists " + TABLE_CALL + "(" + ID
		// + " integer primary key autoincrement ," + PHONENUMBER
		// + " text not null ," + DATE + " integer not null )";
		// db.execSQL(csql);

		String ssql = "create table if not exists " + TABLE_SMS + "(" + ID
				+ " integer primary key autoincrement ," + PHONENUMBER
				+ " text not null ," + DATE + " integer not null ," + CONTENT
				+ " text not null ," + TYPE + " integer not null ," + WORD
				+ " text )";
		db.execSQL(ssql);

		String wsql = "create table if not exists " + TABLE_WORD + "(" + ID
				+ " integer primary key autoincrement ," + WORD + " text ,"
				+ BLOCKEDTIMES + "  integer default 0 ," + TAG
				+ "  integer default 1 )";
		db.execSQL(wsql);

		// String bsql = "create table if not exists " + TABLE_BACKUP + "(" + ID
		// + " integer primary key autoincrement ," + NAME + " text ,"
		// + PHONENUMBER + " text )";
		// db.execSQL(bsql);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
