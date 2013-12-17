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
import dev.moco.browser.Scanner.fragment.BarcodeFragment;
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
    private Bundle args = null;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//set fullscreen and deactivate standby
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_main);

		args = new Bundle();
		context = getApplicationContext();
		actionbar = getSupportActionBar();

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
		scannerPagerAdapter = new ScannerPagerAdapter(getSupportFragmentManager(), context, args);
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
		return super.onCreateOptionsMenu(menu);
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
            case R.id.barcode_button:
                final BarcodeFragment barFragment = (BarcodeFragment) scannerPagerAdapter.getItem(1);
                barFragment.onClick(view);
                break;
            case R.id.qr_button:
                final QRFragment qrFragment = (QRFragment) scannerPagerAdapter.getItem(2);
                qrFragment.onClick(view);
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
	    if(resultCode == RESULT_OK) {
	        final int fragmentNumber = args.getInt("fragmentID");
	        switch (fragmentNumber) {
            case R.id.fragment_id_barcode:
                final BarcodeFragment barFragment = (BarcodeFragment) scannerPagerAdapter.getItem(1);
                barFragment.onActivityResult(requestCode, resultCode, data);
                break;
            case R.id.fragment_id_qr:
                final QRFragment qrFragment = (QRFragment) scannerPagerAdapter.getItem(2);
                qrFragment.onActivityResult(requestCode, resultCode, data);
                break;
            default:
                final String err = getString(R.string.error_fragment_to_call_not_found);
                Toast.makeText(context, err, Toast.LENGTH_SHORT).show();
                Log.e(context.getPackageName(), err);
                break;
            }
	    }
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
