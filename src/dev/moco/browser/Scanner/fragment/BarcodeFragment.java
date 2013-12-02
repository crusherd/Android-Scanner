package dev.moco.browser.Scanner.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import dev.moco.browser.Scanner.R;

/**
 * Fragment for handling the "barcode"-UI part
 * @author Robert Danczak
 */
public class BarcodeFragment extends Fragment {

//    private Camera camera = null;
//    private Context context = null;

    /**
     * When creating the fragment, initialize everything needed.
     */
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        context = getActivity();
    }

    /**
     * creates the ui for the barcode part and returns it.
     *
     * @return View to display.
     */
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        final View rootView = inflater.inflate(R.layout.fragment_barcode, container, false);
//        final SurfaceView surfaceView = (SurfaceView) rootView;
//        SurfaceHolder holder = (SurfaceHolder) surfaceView.getHandler();
//        try {
//            holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//            camera.setPreviewDisplay(holder);
//            camera.startPreview();
//        } catch (final Exception e) {
//            Log.e("error", e.getMessage());
//        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
//        getCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
//        camera.release();
    }

//    private void getCamera() {
//        try {
//            camera = Camera.open();
//        }
//        catch (final Exception e) {
//            Toast.makeText(context, "Camera not found!", Toast.LENGTH_SHORT).show();
//            Log.e("error", "Camera not found!");
//        }
//    }
//
//    /**
//     * Check for camera, flash and autofocus
//     * @return true if present, false otherwise
//     */
//    private boolean checkCameraHardware() {
//        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA) &&
//            context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_AUTOFOCUS) &&
//            context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
//            return true;
//        } else {
//            return false;
//        }
//    }
}
