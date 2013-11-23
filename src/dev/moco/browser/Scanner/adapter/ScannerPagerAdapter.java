package dev.moco.browser.Scanner.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.Toast;
import dev.moco.browser.Scanner.R;
import dev.moco.browser.Scanner.fragment.BarcodeFragment;
import dev.moco.browser.Scanner.fragment.HistoryFragment;
import dev.moco.browser.Scanner.fragment.QRFragment;

/**
 *
 * @author Robert Danczak
 *
 */
public class ScannerPagerAdapter extends FragmentPagerAdapter {

    private Context context = null;

	public ScannerPagerAdapter(final FragmentManager fm, final Context context) {
		super(fm);
		this.context = context;
	}

	@Override
	public Fragment getItem(final int i) {
	    Fragment fragment = null;
	    switch (i) {
	        case 0:
	            fragment = new HistoryFragment();
	            return fragment;
	        case 1:
	            fragment = new BarcodeFragment();
	            return fragment;
	        case 2:
	            fragment = new QRFragment();
	            return fragment;
	        default:
	            final String err = context.getString(R.string.error_fragment_not_found);
	            Toast.makeText(context, err, Toast.LENGTH_SHORT).show();
                return fragment;
	    }
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 3;
	}

	@Override
    public CharSequence getPageTitle(final int position) {
//        return "OBJECT " + (position + 1);
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
