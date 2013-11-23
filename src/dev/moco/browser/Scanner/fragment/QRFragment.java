package dev.moco.browser.Scanner.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import dev.moco.browser.Scanner.R;

/**
 * @author Robert Danczak
 *
 */
public class QRFragment extends Fragment {

    public static final String ARG_OBJECT = "object";

    /**
     *
     */
    public QRFragment() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        final View rootView = inflater.inflate(R.layout.fragment_qr_code, container, false);
        return rootView;
    }
}
