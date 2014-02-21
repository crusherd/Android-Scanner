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
import android.provider.ContactsContract;
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
        final String content = data.getStringExtra(Intents.Scan.RESULT);
        //create new dialog for user
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setNegativeButton(R.string.button_abort, getAbortListener());

        //save to history
        //NOTE from docs: This is either the android:id value supplied in a layout or the container view ID supplied when adding the fragment.
        //TODO: Change this work-around to get the fragment by id not by index.
        final HistoryFragment history = (HistoryFragment) getFragmentManager().getFragments().get(0);
        //URL
        if(content.contains("http://") || content.contains("https://")) {
        	history.addEntry(HistoryType.QRCode, "URL", content);
            createURLDialog(content, dialogBuilder);
        }
        //Calendar entry
        else if(content.startsWith("BEGIN:VEVENT")) {
        	history.addEntry(HistoryType.QRCode, "Calendar", content);
            createCalendarDialog(content, dialogBuilder);
        }
        //Contact entry
        else if(content.startsWith("BEGIN:VCARD")) {
        	history.addEntry(HistoryType.QRCode, "Contact", content);
            createContactDialog(content, dialogBuilder);
        }
        //suppose normal text in QR-Code
        else {
        	history.addEntry(HistoryType.QRCode, "Text", content);
            createTextDialog(content, dialogBuilder);
        }
    }

    private DialogInterface.OnClickListener getAbortListener() {
        return new DialogInterface.OnClickListener() {

            @Override
            public void onClick(final DialogInterface dialog, final int which) { }
        };
    }

    private void createURLDialog(final String content, final AlertDialog.Builder dialogBuilder) {
        dialogBuilder.setTitle(getString(R.string.title_url_found));
        dialogBuilder.setMessage(content);
        final DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                final Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(content));
                startActivity(urlIntent);

            }
        };
        dialogBuilder.setPositiveButton(R.string.button_open_link, positiveListener);
        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    private void createTextDialog(final String content, final AlertDialog.Builder dialogBuilder) {
        dialogBuilder.setTitle(getString(R.string.title_text_found));
        dialogBuilder.setMessage(content);
        final DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                createTextDialogOnClick(content);
            }
        };
        dialogBuilder.setPositiveButton(R.string.button_copy, positiveListener);
        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    private void createCalendarDialog(final String content, final AlertDialog.Builder dialogBuilder) {
        dialogBuilder.setTitle(R.string.title_calendar_event_found);
        dialogBuilder.setMessage(R.string.message_calendar_hint);
        final DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(final DialogInterface dialog, final int which) {
              createCalendarDialogOnClick(content);
            }
        };
        dialogBuilder.setPositiveButton(R.string.button_add_to_calendar, positiveListener);
        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    private void createContactDialog(final String content, final AlertDialog.Builder dialogBuilder) {
        dialogBuilder.setTitle(R.string.title_contact_found);
        final DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                createContactDialogOnClick(content);
            }
        };

        dialogBuilder.setPositiveButton(R.string.button_add_to_contacts, positiveListener);
        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    /**
     * NOTE: Suppress is needed due to Lint warning and setting project to not buildable.
     */
    @SuppressLint("NewApi")
    private void createTextDialogOnClick(final String content) {
        //check compatibility for API level 11 aka Honeycomb
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            final android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(content);
        }
        else {
            final android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            final android.content.ClipData clip = android.content.ClipData.newPlainText("", content);
            clipboard.setPrimaryClip(clip);
        }
    }

    /**
     * IMPORTANT: Uses iCal format.
     * <br/>
     * FORMAT: BEGIN:VEVENT\r\nSUMMARY:\r\nDTSTART:\r\nDTEND:\r\nEND:VEVENT
     * <br/>
     * NOTE: Suppress is needed due to Lint warning and setting project to not buildable.
     *
     *
     */
    @SuppressLint("NewApi")
    private void createCalendarDialogOnClick(final String content) {
        final String[] splitted = content.split("\r\n");
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
        //check compatibility for API level 14 aka Ice Cream Sandwich
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

    /**
     * IMPORTANT: Uses vCard v3.0 format.
     * <br/>
     * FORMAT (example from wikipedia):
     * BEGIN:VCARD
     * VERSION:3.0
     * N:Mustermann;Max
     * FN:Max Mustermann
     * ORG:Wikipedia
     * URL:http://de.wikipedia.org/
     * EMAIL;TYPE=INTERNET:max.mustermann@example.org
     * TEL;TYPE=voice,work,pref:+49 1234 56788
     * ADR;TYPE=intl,work,postal,parcel:;;Musterstraße 1;Musterstadt;;12345;Germany
     * END:VCARD
     * <br/>
     * @param content
     */
    private void createContactDialogOnClick(final String content) {
        final Intent contactIntent = new Intent(android.provider.ContactsContract.Intents.Insert.ACTION);
        contactIntent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

        //TODO: implement insertion!

        startActivity(contactIntent);
    }
}
