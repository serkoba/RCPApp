package com.bobygoma.redciudadana.custom_views;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.bobygoma.redciudadana.R;

/**
 * Created by daniel.streitenberger on 15/03/2017.
 */
public class LoadingFragmentDialog extends DialogFragment {
    public static final String TAG = "LoadingFragmentDialog";

    public static final String MESSAGE = "message";

    String mMessage;

    private ProgressDialog dialog;

    public static LoadingFragmentDialog newInstance(String message) {
        LoadingFragmentDialog fragment = new LoadingFragmentDialog();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString(MESSAGE, message);
        fragment.setArguments(args);

        return fragment;
    }

    public static LoadingFragmentDialog newInstance() {
        LoadingFragmentDialog fragment = new LoadingFragmentDialog();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getArguments();
        if(extras != null && extras.containsKey(MESSAGE)){
            mMessage = extras.getString(MESSAGE);
        }else{
            mMessage = getString(R.string.please_wait);
        }
        setCancelable(false);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = new ProgressDialog(getActivity(), getTheme());
        dialog.setMessage(mMessage);
        dialog.setIndeterminate(true);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        return dialog;
    }

    public void dismiss() {
        //android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
        //fragmentManager.popBackStack();
        dialog.dismiss();
    }
}