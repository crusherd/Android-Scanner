package dev.moco.browser.Scanner.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.android.Intents;

import dev.moco.browser.Scanner.R;
import dev.moco.browser.Scanner.fragment.HistoryFragment.HistoryType;

/**
 * Fragment for handling the "barcode"-UI part
 * @author Robert Danczak
 */
public class BarcodeFragment extends Fragment {

    //TODO: complete query
    private static final String amazonQuery = "http://www.amazon.com";

    /**
     * When creating the fragment, initialize everything needed.
     */
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * creates the ui for the barcode part and returns it.
     *
     * @return View to display.
     */
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated properly.
        final View rootView = inflater.inflate(R.layout.fragment_barcode, container, false);
        return rootView;
    }

    public void onClick(final View view) {
        //create Scan intent and wait for result
        final Intent intent = new Intent(getActivity().getApplicationContext(), CaptureActivity.class);
        intent.setAction(Intents.Scan.ACTION);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.putExtra(Intents.Scan.MODE, Intents.Scan.PRODUCT_MODE);
        intent.putExtra(Intents.Scan.SAVE_HISTORY, false);
        intent.putExtra(Intents.Scan.RESULT_DISPLAY_DURATION_MS, 0L);
        final Bundle args = getArguments();
        args.putInt(getString(R.string.fragment_id), R.id.fragment_id_barcode);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        final String content = data.getStringExtra(Intents.Scan.RESULT);
        //create new dialog for user
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setNegativeButton(R.string.button_abort, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(final DialogInterface dialog, final int which) {}
        });

        //save to history
        //NOTE from docs: This is either the android:id value supplied in a layout or the container view ID supplied when adding the fragment.
        //TODO: Change this work-around to get the fragment by id not by index.
        final HistoryFragment history = (HistoryFragment) getFragmentManager().getFragments().get(0);
        history.addEntry(HistoryType.Barcode, "Barcode", content);

        dialogBuilder.setTitle(getString(R.string.title_barcode_found));
        dialogBuilder.setMessage(content);
        final DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                final Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(amazonQuery + content));
                startActivity(urlIntent);

            }
        };
        dialogBuilder.setPositiveButton(R.string.button_open_link, positiveListener);
        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }
}
