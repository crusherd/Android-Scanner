package dev.moco.browser.Scanner.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import dev.moco.browser.Scanner.R;

/**
 *
 * @author Robert Danczak
 *
 */
public class BarcodeFragment extends Fragment {


    public BarcodeFragment() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        final View rootView = inflater.inflate(R.layout.fragment_barcode, container, false);
        return rootView;
    }
}
