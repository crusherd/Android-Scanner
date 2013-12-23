package dev.moco.browser.Scanner.fragment;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import dev.moco.browser.Database.DbHelper;
import dev.moco.browser.Scanner.R;

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
	private SQLiteDatabase db = null;

	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dbHelper = new DbHelper(getActivity());
		db = dbHelper.getWritableDatabase();
	}

	@Override
    public void onPause() {
		super.onPause();
		db.close();
	}

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        final View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        rootView.setId(R.id.fragment_id_history);
        return rootView;
    }

    public void addEntry(HistoryType type, String title, String content) {
    	ContentValues entry = new ContentValues();
		entry.put(DbHelper.COLUMN_TITLE, title);
		entry.put(DbHelper.COLUMN_CONTENT, content);
    	switch(type) {
	    	case Barcode:
	    		entry.put(DbHelper.COLUMN_TYPE, "barcode");
	    		break;
	    	case QRCode:
				entry.put(DbHelper.COLUMN_TYPE, "qr");
	    		break;
    	}
    	db.insert(DbHelper.TABLE_NAME, "null", entry);
    }
}
