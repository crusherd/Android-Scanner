package dev.moco.browser.Scanner.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.android.Intents;

import dev.moco.browser.Scanner.R;

/**
 * Class for managing QR-Codes.
 * @author Robert Danczak
 */
public class QRFragment extends Fragment {

    /**
     * When creating the fragment, initialize everything needed.
     */
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * creates the ui for the QR-Code part and returns it.
     *
     * @return View to display.
     */
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated properly.
        final View rootView = inflater.inflate(R.layout.fragment_qr_code, container, false);
        return rootView;
    }

    public void onClick(final View view) {
        //create Scan intent and wait for result
        final Intent intent = new Intent(getActivity().getApplicationContext(), CaptureActivity.class);
        intent.setAction(Intents.Scan.ACTION);
        intent.putExtra(Intents.Scan.MODE, Intents.Scan.QR_CODE_MODE);
        intent.putExtra(Intents.Scan.SAVE_HISTORY, false);
        intent.putExtra(Intents.Scan.RESULT_DISPLAY_DURATION_MS, 0L);
        intent.putExtra(Intents.Scan.PROMPT_MESSAGE, getString(R.string.view_scan_qr));
//        final Bundle bundle = getActivity().getIntent().getExtras();
        final Bundle args = getArguments();
        args.putInt("fragmentID", 2);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        Log.d("qr", "test");
    }
}
