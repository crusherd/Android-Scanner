package dev.moco.browser.Scanner.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import dev.moco.browser.Scanner.R;

/**
 * Class for managing QR-Codes.
 * @author Robert Danczak
 */
public class QRFragment extends Fragment {


    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated properly.
        final View rootView = inflater.inflate(R.layout.fragment_qr_code, container, false);
        return rootView;
    }

    public void onClick(final View view) {

        Log.d("QR-Code Fragment", "click works!");

        //TODO: currently not working
        //create Scan intent and wait for result
//        final Intent intent = new Intent(getActivity().getApplicationContext(), CaptureActivity.class);
//        intent.setAction("com.google.zxing.client.android.SCAN");
//        intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
//        intent.putExtra("SAVE_HISTORY", false);
//        startActivityForResult(intent, 0);
    }
}
