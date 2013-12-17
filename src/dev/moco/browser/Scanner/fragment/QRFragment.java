package dev.moco.browser.Scanner.fragment;

import android.app.AlertDialog;
import android.content.Context;
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

/**
 * Class for managing QR-Codes.
 * @author Robert Danczak
 */
public class QRFragment extends Fragment {

    AlertDialog dialog = null;

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
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.putExtra(Intents.Scan.MODE, Intents.Scan.QR_CODE_MODE);
        intent.putExtra(Intents.Scan.SAVE_HISTORY, false);
        intent.putExtra(Intents.Scan.RESULT_DISPLAY_DURATION_MS, 0L);
        intent.putExtra(Intents.Scan.PROMPT_MESSAGE, getString(R.string.view_scan_qr));
        final Bundle args = getArguments();
        args.putInt(getString(R.string.fragment_id), R.id.fragment_id_qr);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        final String contents = data.getStringExtra(Intents.Scan.RESULT);
        //create new dialog for user
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setNegativeButton(R.string.button_abort, getAbortListener());

        //URL
        if(contents.contains("http://") || contents.contains("https://")) {
            createURLDialog(contents, dialogBuilder);
        }
        //Calendar entry
        else if(contents.startsWith("BEGIN:VEVENT")) {
            createCalendarDialog(contents, dialogBuilder);
        }
        //Contact entry
        else if(contents.startsWith("BEGIN:VCARD")) {
            createContactDialog(contents, dialogBuilder);
        }
        //normal text in QR-Code
        else {
            createTextDialog(contents, dialogBuilder);
        }
    }

    private DialogInterface.OnClickListener getAbortListener() {
        return new DialogInterface.OnClickListener() {

            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                // TODO Auto-generated method stub

            }
        };
    }

    private void createURLDialog(final String contents, final AlertDialog.Builder dialogBuilder) {
        dialogBuilder.setTitle(getString(R.string.title_url_found));
        dialogBuilder.setMessage(contents);
        final DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                final Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(contents));
                startActivity(urlIntent);

            }
        };
        dialogBuilder.setPositiveButton(R.string.button_proceed, positiveListener);
        dialog = dialogBuilder.create();
        dialog.show();
    }

    private void createTextDialog(final String contents, final AlertDialog.Builder dialogBuilder) {
        dialogBuilder.setTitle(getString(R.string.title_text_found));
        dialogBuilder.setMessage(contents);
        final DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                //check compatibility
                if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                    final android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setText(contents);
                } else {
                    final android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    final android.content.ClipData clip = android.content.ClipData.newPlainText("", contents);
                    clipboard.setPrimaryClip(clip);
                }
            }
        };
        dialogBuilder.setPositiveButton(R.string.button_copy, positiveListener);
        dialog = dialogBuilder.create();
        dialog.show();
    }

    /**
     * decode a calendar QR-Code.
     *
     * FORMAT: BEGIN:VEVENT\r\nSUMMARY:\r\nDTSTART:\r\nDTEND:\r\nEND:VEVENT
     *
     * @param contents
     * @param dialogBuilder
     */
    private void createCalendarDialog(final String contents, final AlertDialog.Builder dialogBuilder) {

    }

    private void createContactDialog(final String contents, final AlertDialog.Builder dialogBuilder) {

    }
}
