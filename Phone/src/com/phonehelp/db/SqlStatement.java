package com.phonehelp.db;

public class SqlStatement {
	public static final String DATABASE_NAME = "phonehelp.db";
	public static final int VERSION = 1;
	// db name
	public static final String DB_TABLE_BLACKLIST = "blacklist";
	public static final String DB_TABLE_SMS = "blocked_sms";
	public static final String DB_TABLE_WORD = "keyword";
	// column name
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_PHONENUMBER = "phonenumber";
	public static final String COLUMN_BLOCKED_TIMES = "blockedtimes";
	public static final String COLUMN_TAG = "tag";
	public static final String COLUMN_DATE = "date";
	public static final String COLUMN_TYPE = "type";
	public static final String COLUMN_CONTENT = "content";
	public static final String COLUMN_WORD = "word";

	// create table
	public static final String CREATE_TABLE_BLACKLIST = "create table if not exists "
			+ DB_TABLE_BLACKLIST
			+ "("
			+ COLUMN_ID
			+ " integer primary key autoincrement ,"
			+ COLUMN_PHONENUMBER
			+ " text not null, "
			+ COLUMN_BLOCKED_TIMES
			+ " integer default 0, " + COLUMN_TAG + " integer default 1 )";

	public static final String CREATE_TABLE_MSM = "create table if not exists "
			+ DB_TABLE_SMS + "(" + COLUMN_ID
			+ " integer primary key autoincrement ," + COLUMN_PHONENUMBER
			+ " text not null ," + COLUMN_DATE + " integer not null ,"
			+ COLUMN_CONTENT + " text not null ," + COLUMN_TYPE
			+ " integer not null ," + COLUMN_WORD + " text )";

	public static final String CREATE_TABLE_WORD = "create table if not exists "
			+ DB_TABLE_WORD
			+ "("
			+ COLUMN_ID
			+ " integer primary key autoincrement ,"
			+ COLUMN_WORD
			+ " text ,"
			+ COLUMN_BLOCKED_TIMES
			+ "  integer default 0 ,"
			+ COLUMN_TAG
			+ "  integer default 1 )";
}
