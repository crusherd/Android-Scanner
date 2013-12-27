package dev.moco.browser.Scanner.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DbHelper extends SQLiteOpenHelper {

	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "Scanner.db";

	public static final String TABLE_NAME = "history";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_CONTENT = "content";
	public static final String COLUMN_TYPE = "type";

	private final String SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE_NAME +
											  " ( " + BaseColumns._ID + " INTEGER PRIMARY KEY, " +
											  COLUMN_TITLE + " TEXT, " +
											  COLUMN_CONTENT + " TEXT, " +
											  COLUMN_TYPE + " INTEGER )";
	private final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;

	public DbHelper(Context context, String name, CursorFactory factory,
	        int version) {
		super(context, name, factory, version);
	}

	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

//	public DbHelper(Context context, String name, CursorFactory factory,
//	        int version, DatabaseErrorHandler errorHandler) {
//		super(context, name, factory, version, errorHandler);
//		// TODO Auto-generated constructor stub
//	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_ENTRIES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(SQL_DELETE_ENTRIES);
		onCreate(db);
	}
}
