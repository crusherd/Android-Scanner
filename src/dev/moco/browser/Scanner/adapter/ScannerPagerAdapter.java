package dev.moco.browser.Scanner.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.widget.Toast;
import dev.moco.browser.Scanner.R;
import dev.moco.browser.Scanner.fragment.BarcodeFragment;
import dev.moco.browser.Scanner.fragment.HistoryFragment;
import dev.moco.browser.Scanner.fragment.QRFragment;

/**
 * This adapter handles the three different views (fragments) of the app.
 * If history shall be displayed it returns a {@link HistoryFragment}.
 * If the user request the barcode part it returns a {@link BarcodeFragment}.
 * Finally, if we have to display the QR-Code part it returns a {@link QRFragment}.
 *
 * @author Robert Danczak
 */
public class ScannerPagerAdapter extends FragmentPagerAdapter {

    private Context context = null;
    private final int NUMBER_PAGES = 3;

    private HistoryFragment historyFragment = null;
    private BarcodeFragment barcodeFragment = null;
    private QRFragment qrFragment = null;

    /**
     * Creates a new {@link ScannerPagerAdapter}.
     * @param fm - FragmentManager to use.
     * @param context -
     */
	public ScannerPagerAdapter(final FragmentManager fm, final Context context) {
		super(fm);
		this.context = context;
	}

	/**
	 * Creates either a {@link HistoryFragment}, {@link BarcodeFragment} or a {@link QRFragment} and returns it.
	 *
	 * @param i - pos of the requested fragment.
	 * @return the associated fragment or null if no fragment found.
	 */
	@Override
	public Fragment getItem(final int i) {
	    switch (i) {
	        case 0:
	            if(historyFragment == null) {
	                historyFragment = new HistoryFragment();
	            }
                return historyFragment;
	        case 1:
	            if(barcodeFragment == null) {
	                barcodeFragment = new BarcodeFragment();
	            }
	            return barcodeFragment;
	        case 2:
	            if(qrFragment == null) {
	                qrFragment = new QRFragment();
	            }
	            return qrFragment;
	        default:
	            final String err = context.getString(R.string.error_fragment_not_found);
	            Toast.makeText(context, err, Toast.LENGTH_SHORT).show();
	            Log.e(context.getPackageName(), err);
                return null;
	    }
	}

	/**
	 * @return the number of fragments in this adapter. The number is determinated on 3.
	 */
	@Override
	public int getCount() {
		return NUMBER_PAGES;
	}

	@Override
    public CharSequence getPageTitle(final int position) {
	    switch (position) {
        case 0:
            return context.getString(R.string.history);
        case 1:
            return context.getString(R.string.barcode);
        case 2:
            return context.getString(R.string.qr_code);
        default:
            final String err = context.getString(R.string.error_page_title_not_found);
            Toast.makeText(context, err, Toast.LENGTH_SHORT).show();
            return null;
        }
	}

}
