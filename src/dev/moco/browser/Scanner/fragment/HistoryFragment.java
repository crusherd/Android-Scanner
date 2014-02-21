package dev.moco.browser.Scanner.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;
import android.widget.ProgressBar;
import dev.moco.browser.Scanner.R;
import dev.moco.browser.Scanner.database.DbHelper;

/**
 * This class manages the scanned barcodes or QR-Codes in a history manner.
 * Data which has to be saved will be stored in a SQLite database.
 * @author Robert Danczak
 *
 */
public class HistoryFragment extends Fragment {

	/**
	 * Enum to determine the type of code.
	 */
	public enum HistoryType{
		Barcode,
		QRCode
	};

	private DbHelper dbHelper = null;
//	private SQLiteDatabase db = null;

	@Override
    public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dbHelper = new DbHelper(getActivity());

		{
            dbHelper.addEntry(HistoryType.Barcode, "a barcode", "1234567890");
            dbHelper.addEntry(HistoryType.QRCode, "a simple QR code", "qr code");
            dbHelper.addEntry(HistoryType.QRCode, "a url in qr", "http://www.google.de");
            dbHelper.addEntry(HistoryType.Barcode, "a barcode", "test");
        }

//		db = dbHelper.getWritableDatabase();
//		db.close();
	}

	/**
	 * Fill the list view with all elements in db on start.
	 */
	@Override
    public void onStart() {
		super.onStart();

		final ListView listView = (ListView) getActivity().findViewById(R.id.fragment_id_history);
		final ProgressBar progressBar = new ProgressBar(getActivity());
		progressBar.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		progressBar.setIndeterminate(true);
		listView.setEmptyView(progressBar);

		final Cursor cursor = dbHelper.getDBContents();
		cursor.moveToFirst();
		Log.d("HISTORY", String.valueOf(cursor.getCount()));
		while(!cursor.isAfterLast()) {
		    Log.d("HISTORY", cursor.getString(0));
		}

//		final String[] from = {DbHelper.COLUMN_TITLE, DbHelper.COLUMN_CONTENT, DbHelper.COLUMN_TYPE};
//		final int[] to = {android.R.id.text1, android.R.id.text1, android.R.id.text1};

//		final SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(getActivity(),
//												android.R.layout.simple_list_item_1,
//												cursor,
//												from,
//												to);
//		listView.setAdapter(simpleCursorAdapter);

//		List<DBEntry> contents = getDBContents();
	}

	@Override
    public void onPause() {
		super.onPause();
//		if(db != null) {
//	        db.close();
//        }
	}

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated properly.
        final View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        rootView.setId(R.id.fragment_id_history);
        return rootView;
    }

    /**
     * Adds an entry to the database with the given information if it is not in the database.
     *
     * @param type - wether its a barcode or QR-Code
     * @param title - Name to display for the element
     * @param content - data from barcode or QR-Code
     */
    public void addEntry(final HistoryType type, final String title, final String content) {
    	dbHelper.addEntry(type, title, content);
//    	if(!isEntryInDB(content)) {
//	    	ContentValues entry = new ContentValues();
//			entry.put(DbHelper.COLUMN_TITLE, title);
//			entry.put(DbHelper.COLUMN_CONTENT, content);
//	    	switch(type) {
//		    	case Barcode:
//		    		entry.put(DbHelper.COLUMN_TYPE, 0);
//		    		break;
//		    	case QRCode:
//					entry.put(DbHelper.COLUMN_TYPE, 1);
//		    		break;
//	    	}
//	    	if(!db.isOpen()) {
//	    		db = dbHelper.getWritableDatabase();
//	    	}
//	    	db.insert(DbHelper.TABLE_NAME, null, entry);
//    	}
    }

    /**
     * Removes an entry with the given content.
     * @param content - Entry to delete with the given content.
     */
    public void removeEntry(final String content) {
    	dbHelper.removeEntry(content);
    }

//    private List<DBEntry> getDBContents(){
//    	if(!db.isOpen()) {
//    		db = dbHelper.getReadableDatabase();
//    	}
//    	String[] columns = null;
//    	String orderBy = BaseColumns._ID + " DESC";
//		Cursor c = db.query(DbHelper.TABLE_NAME,
//    						columns,
//    						null,
//    						null,
//    						null,
//    						null,
//    						orderBy,
//    						null);
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
//    	return entries;
//    }

//    /**
//     * Checks if contents is present in database.
//     * @param contents - Contents to add to database.
//     * @return true if contents is present in database false otherwise.
//     */
//    private boolean isEntryInDB(String contents) {
//    	String selection = " = " + contents;
//    	String orderBy = BaseColumns._ID + " DESC";
//    	Cursor c = db.query(DbHelper.TABLE_NAME,
//				null,
//				selection,
//				null,
//				null,
//				null,
//				orderBy,
//				null);
//    	c.moveToFirst();
//    	while(!c.isAfterLast()) {
//    		if(contents.equals(c.getString(0))) {
//    			return true;
//    		}
//    	}
//		return false;
//    }
}
