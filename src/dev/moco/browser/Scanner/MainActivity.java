package dev.moco.browser.Scanner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import dev.moco.browser.Scanner.adapter.ScannerPagerAdapter;
import dev.moco.browser.Scanner.fragment.QRFragment;

/**
 * Main entry point for the "Scanner"-app.
 * @author Robert Danczak
 */
public class MainActivity extends ActionBarActivity implements ActionBar.TabListener {

    private Context context = null;
    private ActionBar actionbar = null;

    private SettingsActivity settings = null;

    // When requested, this adapter returns a HistoryFragment, BarcodeFragment or QRFragment.
    private ScannerPagerAdapter scannerPagerAdapter = null;
    private ViewPager viewPager = null;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = getApplicationContext();
		actionbar = getSupportActionBar();

		//set fullscreen and deactivate standby
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		//disable Home/Up button
		actionbar.setHomeButtonEnabled(false);

		//activate navigation mode
		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		//hide actionbar title and icon
		actionbar.setDisplayShowTitleEnabled(false);
		actionbar.setDisplayShowHomeEnabled(false);

		actionbar.setDisplayHomeAsUpEnabled(false);
        actionbar.setDisplayUseLogoEnabled(false);

		// ViewPager and its adapters use support library fragments, so use getSupportFragmentManager.
		scannerPagerAdapter = new ScannerPagerAdapter(getSupportFragmentManager(), context);
		viewPager = (ViewPager) findViewById(R.id.pager);
		viewPager.setAdapter(scannerPagerAdapter);
		viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
		    @Override
            public void onPageSelected(final int position) {
		        actionbar.setSelectedNavigationItem(position);
		    }
		});

		//add tab for each section in app
        for(int i = 0; i < scannerPagerAdapter.getCount(); i++) {
            final Tab tab = actionbar.newTab();
            tab.setText(scannerPagerAdapter.getPageTitle(i));
            tab.setTabListener(this);
            actionbar.addTab(tab);
        }

        settings = new SettingsActivity();
	}

	/**
	 * add context options menu
	 */
	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
    public boolean onOptionsItemSelected(final MenuItem item) {
	    switch (item.getItemId()) {
        case R.id.action_settings:
            final Intent intent = new Intent(context, SettingsActivity.class);
            startActivity(intent);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
	}

	/**
	 * Called if the user presses either the barcode or QR-Code button.
	 * @param view - initiating view
	 */
	public void onClick(final View view) {
	    switch (view.getId()) {
            case R.id.qr_button:
                final QRFragment fragment = (QRFragment) scannerPagerAdapter.getItem(2);
                fragment.onClick(view);
                break;
            default:
                final String err = getString(R.string.error_button_not_found);
                Toast.makeText(context, err, Toast.LENGTH_SHORT).show();
                Log.e(context.getPackageName(), err);
                break;
        }
    }

	@Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {

	}


    @Override
    public void onTabReselected(final Tab tab, final FragmentTransaction fragmentTransaction) {
        // TODO Auto-generated method stub

    }

    /**
     * Select the item to view.
     */
    @Override
    public void onTabSelected(final Tab tab, final FragmentTransaction fragmentTransaction) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(final Tab tab, final FragmentTransaction fragmentTransaction) {
        // TODO Auto-generated method stub

    }
}
