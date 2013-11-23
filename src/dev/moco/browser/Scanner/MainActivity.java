package dev.moco.browser.Scanner;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.WindowManager;
import android.widget.Toast;
import dev.moco.browser.Scanner.adapter.ScannerPagerAdapter;

/**
 *
 * @author Robert Danczak
 *
 */
public class MainActivity extends ActionBarActivity implements ActionBar.TabListener {

    private Context context = null;
    private Camera camera = null;
    private ActionBar actionbar = null;

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

	}

	@Override
    public void onResume() {
	    super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		return true;
	}

	/**
	 * Check for camera, flash and autofocus
	 * @return true if present, false otherwise
	 */
	private boolean checkCameraHardware() {
	    if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA) &&
	        context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_AUTOFOCUS) &&
	        context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
	        return true;
	    } else {
	        return false;
	    }
	}

	private void getCamera() {
	    try {
	        camera = Camera.open();
	    }
	    catch (final Exception e) {
	        Toast.makeText(context, "Camera not found!", Toast.LENGTH_SHORT).show();
	        Log.e("error", "Camera not found!");
	    }
	}

    @Override
    public void onTabReselected(final Tab tab, final FragmentTransaction fragmentTransaction) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTabSelected(final Tab tab, final FragmentTransaction fragmentTransaction) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(final Tab tab, final FragmentTransaction fragmentTransaction) {
        // TODO Auto-generated method stub

    }
}
