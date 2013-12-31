package dev.moco.browser.Scanner.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import dev.moco.browser.Scanner.fragment.HistoryFragment.HistoryType;

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

	public class DBEntry {
		public String id = null, title = null, content = null;
		public HistoryType type;

		public DBEntry(final String id, final HistoryType type, final String title, final String content) {
			this.id = id;
			this.type = type;
			this.title = title;
			this.content = content;
        }
	};

	public DbHelper(final Context context, final String name, final CursorFactory factory,
	        final int version) {
		super(context, name, factory, version);
	}

	public DbHelper(final Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

//	public DbHelper(Context context, String name, CursorFactory factory,
//	        int version, DatabaseErrorHandler errorHandler) {
//		super(context, name, factory, version, errorHandler);
//		// TODO Auto-generated constructor stub
//	}

	@Override
	public void onCreate(final SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_ENTRIES);
	}

	@Override
	public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
		db.execSQL(SQL_DELETE_ENTRIES);
		onCreate(db);
	}

	/**
     * Adds an entry to the database with the given information if it is not in the database.
     *
     * @param type - wether its a barcode or QR-Code
     * @param title - Name to display for the element
     * @param content - data from barcode or QR-Code
     */
    public void addEntry(final HistoryType type, final String title, final String content) {
		final SQLiteDatabase db = getWritableDatabase();
    	if(!isEntryInDB(db, content)) {
    		db.beginTransaction();
	    	final ContentValues entry = new ContentValues();
			entry.put(COLUMN_TITLE, title);
			entry.put(COLUMN_CONTENT, content);
	    	switch(type) {
		    	case Barcode:
		    		entry.put(COLUMN_TYPE, 0);
		    		break;
		    	case QRCode:
					entry.put(COLUMN_TYPE, 1);
		    		break;
	    	}
	    	db.insert(TABLE_NAME, null, entry);
	    	db.endTransaction();
    	}
    	db.close();
    }

    /**
     * Removes an entry with the given content.
     * @param content - Entry to delete with the given content.
     */
    public void removeEntry(final String content) {
        final SQLiteDatabase db = getWritableDatabase();
    	db.beginTransaction();
    	final String whereClause = COLUMN_CONTENT + " = \"" + content + "\"";
    	db.delete(TABLE_NAME, whereClause, null);
    	db.endTransaction();
    	db.close();
    }

    /**
     * Returns a cursor which contains the title, content and type of each entry.
     * @return - cursor which contains the title, content and type of each entry
     */
    public Cursor getDBContents(){
		final SQLiteDatabase db = getReadableDatabase();
		final String[] columns = {COLUMN_TITLE, COLUMN_CONTENT, COLUMN_TYPE};
		final String orderBy = BaseColumns._ID + " DESC";
		final Cursor c = db.query(DbHelper.TABLE_NAME,
							columns,
							null,
							null,
							null,
							null,
							orderBy,
							null);
//		c.moveToFirst();
//		List<DBEntry> entries = new ArrayList<DBEntry>();
//		while(!c.isAfterLast()) {
//			String title = c.getString(1);
//			String content = c.getString(2);
//			HistoryType type;
//			if(c.getInt(3) == 0) {
//				type = HistoryType.Barcode;
//			}
//			else {
//				type = HistoryType.QRCode;
//			}
//			DBEntry entry = new DBEntry(String.valueOf(c.getInt(0)), type, title, content);
//			entries.add(entry);
//			c.moveToNext();
//		}
		db.close();
		return c;
	}

    /**
     * Checks if contents is present in database.
     * @param db - DB to operate on.
     * @param content - Contents to add to database.
     * @return true if contents is present in database false otherwise.
     */
    private boolean isEntryInDB(final SQLiteDatabase db, final String content) {
        final String selection = COLUMN_CONTENT + " = \"" + content + "\"";
        final Cursor c = db.query(TABLE_NAME,
                null,
                selection,
                null,
                null,
                null,
                null,
                null);
        if(c.getCount() != 0) {
            return true;
        }
        else {
            return false;
        }
    }
}
