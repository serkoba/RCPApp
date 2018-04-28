package com.pixeldifusiones.redciudadana.custom_views;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pixeldifusiones.redciudadana.R;

/**
 * Created by daniel.streitenberger on 29/05/2017.
 */

public class OneButtonDialogFragment extends DialogFragment {
    public static final String TAG = "OneButtonDialogFragment";

    public static final String MESSAGE = "message";
    public static final String BUTTON_TEXT = "buttonText";

    String mMessage;

    String mButtonText;

    public static OneButtonDialogFragment getInstance(String message, String buttonText){
        OneButtonDialogFragment fragment = new OneButtonDialogFragment();

        Bundle args = new Bundle();
        args.putString(MESSAGE, message);
        args.putString(BUTTON_TEXT, buttonText);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);

        Bundle extras = getArguments();

        if (extras != null) {
            mMessage = extras.getString(MESSAGE);
            mButtonText = extras.getString(BUTTON_TEXT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_one_button, container, false);

        ((TextView)view.findViewById(R.id.textViewMessage)).setText(mMessage);

        Button buttonLeft = (Button) view.findViewById(R.id.button);
        buttonLeft.setText(mButtonText);
        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }
}
