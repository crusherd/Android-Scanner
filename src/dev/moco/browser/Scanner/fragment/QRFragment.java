package dev.moco.browser.Scanner.fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.android.Intents;

import dev.moco.browser.Scanner.R;
import dev.moco.browser.Scanner.fragment.HistoryFragment.HistoryType;

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
        rootView.setId(R.id.fragment_id_qr);
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

        //save to history
        HistoryFragment history = (HistoryFragment) getFragmentManager().findFragmentById(R.id.fragment_id_history);

        //URL
        if(contents.contains("http://") || contents.contains("https://")) {
        	history.addEntry(HistoryType.QRCode, "URL", contents);
            createURLDialog(contents, dialogBuilder);
        }
        //Calendar entry
        else if(contents.startsWith("BEGIN:VEVENT")) {
        	history.addEntry(HistoryType.QRCode, "Calendar", contents);
            createCalendarDialog(contents, dialogBuilder);
        }
        //Contact entry
        else if(contents.startsWith("BEGIN:VCARD")) {
        	history.addEntry(HistoryType.QRCode, "Contact", contents);
            createContactDialog(contents, dialogBuilder);
        }
        //normal text in QR-Code
        else {
        	history.addEntry(HistoryType.QRCode, "Text", contents);
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
        dialogBuilder.setPositiveButton(R.string.button_open_link, positiveListener);
        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    private void createTextDialog(final String contents, final AlertDialog.Builder dialogBuilder) {
        dialogBuilder.setTitle(getString(R.string.title_text_found));
        dialogBuilder.setMessage(contents);
        final DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                createTextDialogOnClick(contents);
            }
        };
        dialogBuilder.setPositiveButton(R.string.button_copy, positiveListener);
        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    private void createCalendarDialog(final String contents, final AlertDialog.Builder dialogBuilder) {
        dialogBuilder.setTitle(R.string.title_calendar_event_found);
        dialogBuilder.setMessage(R.string.message_calendar_hint);
        final DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(final DialogInterface dialog, final int which) {
              createCalendarDialogOnClick(contents);
            }
        };
        dialogBuilder.setPositiveButton(R.string.button_add_calendar, positiveListener);
        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    private void createContactDialog(final String contents, final AlertDialog.Builder dialogBuilder) {
    	if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {

    	}
    }

    /**
     * NOTE: Suppress is needed due to Lint warning and setting project to not buildable.
     */
    @SuppressLint("NewApi")
    private void createTextDialogOnClick(final String contents) {
        //check compatibility for API level 11 aka Honeycomb
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            final android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(contents);
        }
        else {
            final android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            final android.content.ClipData clip = android.content.ClipData.newPlainText("", contents);
            clipboard.setPrimaryClip(clip);
        }
    }

    /**
     * IMPORTANT: Uses iCal format.
     * <br/>
     * FORMAT: BEGIN:VEVENT\r\nSUMMARY:\r\nDTSTART:\r\nDTEND:\r\nEND:VEVENT
     * <br/>
     * NOTE: Suppress is needed due to Lint warning and setting project to not buildable.
     */
    @SuppressLint("NewApi")
    private void createCalendarDialogOnClick(final String contents) {
        final String[] splitted = contents.split("\r\n");
        String title = "", description = "", location = "";
        Date begin = new Date(), end = new Date();

        for(final String entry: splitted) {
            final int index = entry.indexOf(":");
            if(entry.contains("SUMMARY")) {
                title = entry.substring(index + 1);
            }
            else if(entry.contains("DESCRIPTION")) {
                description = entry.substring(index + 1);
            }
            else if(entry.contains("LOCATION")) {
                location = entry.substring(index + 1);
            }
            else if(entry.contains("DTSTART")) {
                //date format: 20060910T220000Z
                final SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd'T'kkmmss'Z'");
                try {
                    begin = formater.parse(entry.substring(index + 1));
                } catch (final ParseException e) {
                    //TODO: check exception
                    e.printStackTrace();
                }
            }
            else if(entry.contains("DTEND")) {
                //date format: 20060910T220000Z
                final SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd'T'kkmmss'Z'");
                try {
                    end = formater.parse(entry.substring(index + 1));
                } catch (final ParseException e) {
                    //TODO: check exception
                    e.printStackTrace();
                }
            }
          }
        final Intent calendarIntent = new Intent(Intent.ACTION_INSERT);
        //compatibility
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            calendarIntent.setType("vnd.android.cursor.item/event");
            calendarIntent.putExtra("title", title);
            calendarIntent.putExtra("description", description);
            calendarIntent.putExtra("eventLocation", location);
            calendarIntent.putExtra("availability", 0);
            calendarIntent.putExtra("beginTime", begin.getTime());
            calendarIntent.putExtra("endTime", end.getTime());
        }
        else {
            calendarIntent.setData(Events.CONTENT_URI);
            calendarIntent.putExtra(Events.TITLE, title);
            calendarIntent.putExtra(Events.DESCRIPTION, description);
            calendarIntent.putExtra(Events.EVENT_LOCATION, location);
            calendarIntent.putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY);
            calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, begin.getTime());
            calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, end.getTime());
        }
        startActivity(calendarIntent);
    }
}
