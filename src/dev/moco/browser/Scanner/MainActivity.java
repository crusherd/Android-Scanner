package dev.moco.browser.Scanner;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;
import dev.moco.browser.Scanner.adapter.ScannerPagerAdapter;

/**
 *
 * @author Robert Danczak
 *
 */
public class MainActivity extends FragmentActivity {

    private Context context = null;
    private Camera camera = null;

    // When requested, this adapter returns a HistoryFragment, BarcodeFragment or QRFragment.
    private ScannerPagerAdapter scannerPagerAdapter = null;
    private ViewPager viewPager = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.context = getApplicationContext();

		// ViewPager and its adapters use support library fragments, so use getSupportFragmentManager.
		this.scannerPagerAdapter = new ScannerPagerAdapter(getSupportFragmentManager());
		this.viewPager = (ViewPager) findViewById(R.id.pager);
		this.viewPager.setAdapter(this.scannerPagerAdapter);

		if(checkCameraHardware()) {
		    getCamera();
		    camera.startPreview();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * Check for camera, flash and autofocus
	 * @return true if present, false otherwise
	 */
	private boolean checkCameraHardware() {
	    if (this.context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA) &&
	        this.context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_AUTOFOCUS) &&
	        this.context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
	        return true;
	    } else {
	        return false;
	    }
	}

	private void getCamera() {
	    try {
	        this.camera = Camera.open();
	    }
	    catch (Exception e) {
	        Toast.makeText(this.context, "Camera not found!", Toast.LENGTH_SHORT).show();
	        Log.e("error", "Camera not found!");
	    }
	}
}
