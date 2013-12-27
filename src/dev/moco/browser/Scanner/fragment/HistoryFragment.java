package dev.moco.browser.Scanner.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.app.Fragment;
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

	public class DBEntry {
		public String id = null, title = null, content = null;
		public HistoryType type;

		public DBEntry(String id, HistoryType type, String title, String content) {
			this.id = id;
			this.type = type;
			this.title = title;
			this.content = content;
        }
	};

	private DbHelper dbHelper = null;
	private SQLiteDatabase db = null;

	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dbHelper = new DbHelper(getActivity());
		db = dbHelper.getWritableDatabase();
		db.close();
	}

	/**
	 * Fill the list view with all elements in db on start.
	 */
	@Override
    public void onStart() {
		super.onStart();
		ListView listView = (ListView) getActivity().findViewById(R.id.fragment_id_history);
		ProgressBar progressBar = new ProgressBar(getActivity());
		progressBar.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		progressBar.setIndeterminate(true);
		listView.setEmptyView(progressBar);

		List<DBEntry> contents = getDBContents();
	}

	@Override
    public void onPause() {
		super.onPause();
		if(db != null) {
	        db.close();
        }
	}

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated properly.
        final View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        rootView.setId(R.id.fragment_id_history);
        return rootView;
    }

    /**
     * Adds an entry to the database with the given information.
     *
     * @param type - wether its a barcode or QR-Code
     * @param title - Name to display for the element
     * @param content - data from barcode or QR-Code
     */
    public void addEntry(HistoryType type, String title, String content) {
    	ContentValues entry = new ContentValues();
		entry.put(DbHelper.COLUMN_TITLE, title);
		entry.put(DbHelper.COLUMN_CONTENT, content);
    	switch(type) {
	    	case Barcode:
	    		entry.put(DbHelper.COLUMN_TYPE, 0);
	    		break;
	    	case QRCode:
				entry.put(DbHelper.COLUMN_TYPE, 1);
	    		break;
    	}
    	if(!db.isOpen()) {
    		db = dbHelper.getWritableDatabase();
    	}
    	db.insert(DbHelper.TABLE_NAME, "null", entry);
    }

    private List<DBEntry> getDBContents(){
    	if(!db.isOpen()) {
    		db = dbHelper.getReadableDatabase();
    	}
    	String orderBy = BaseColumns._ID + " DESC";
		Cursor c = db.query(DbHelper.TABLE_NAME,
    						null,
    						null,
    						null,
    						null,
    						null,
    						orderBy,
    						null);
		c.moveToFirst();
		List<DBEntry> entries = new ArrayList<DBEntry>();
		while(!c.isAfterLast()) {
			String title = c.getString(1);
			String content = c.getString(2);
			c.moveToNext();
		}
    	return entries;
    }
}
