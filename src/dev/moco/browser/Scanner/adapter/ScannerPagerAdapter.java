package dev.moco.browser.Scanner.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import dev.moco.browser.Scanner.fragment.HistoryFragment;

/**
 *
 * @author Robert Danczak
 *
 */
public class ScannerPagerAdapter extends FragmentPagerAdapter {

	public ScannerPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int i) {
	    switch (i) {
	        default:
	            Fragment fragment = new HistoryFragment();
	            Bundle args = new Bundle();
                // Our object is just an integer :-P
                args.putInt(HistoryFragment.ARG_OBJECT, i + 1);
                fragment.setArguments(args);
                return fragment;
	    }
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 3;
	}

	@Override
    public CharSequence getPageTitle(int position) {
		// TODO Auto-generated method stub
        return "OBJECT " + (position + 1);
	}

}
