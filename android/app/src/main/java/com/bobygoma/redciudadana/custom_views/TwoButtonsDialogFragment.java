package com.bobygoma.redciudadana.custom_views;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bobygoma.redciudadana.R;

/**
 * Created by daniel.streitenberger on 10/05/2017.
 */
public class TwoButtonsDialogFragment extends DialogFragment {
    public static final String TAG = "TwoButtonsDialogFragment";

    public static final String MESSAGE = "message";
    public static final String BUTTON_LEFT_TEXT = "buttonLeftText";
    public static final String BUTTON_RIGHT_TEXT = "buttonRightText";

    String mMessage;

    String mLeftButton;
    String mRightButton;

    private DialogClickListener callback;

    public static TwoButtonsDialogFragment getInstance(String message, String buttonLeftText,String buttonRightText){
        TwoButtonsDialogFragment fragment = new TwoButtonsDialogFragment();

        Bundle args = new Bundle();
        args.putString(MESSAGE, message);
        args.putString(BUTTON_LEFT_TEXT, buttonLeftText);
        args.putString(BUTTON_RIGHT_TEXT, buttonRightText);
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
            mLeftButton = extras.getString(BUTTON_LEFT_TEXT);
            mRightButton = extras.getString(BUTTON_RIGHT_TEXT);
        }
        try {
            callback = (DialogClickListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement DialogClickListener interface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_two_buttons, container, false);

        ((TextView)view.findViewById(R.id.textViewMessage)).setText(mMessage);

        Button buttonLeft = (Button) view.findViewById(R.id.button);
        buttonLeft.setText(mLeftButton);
        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onDialogLeftButtonPressed();
                dismiss();
            }
        });

        Button buttonRight = (Button) view.findViewById(R.id.buttonRight);
        buttonRight.setText(mRightButton);
        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onDialogRightButtonPressed();
                dismiss();
            }
        });

        return view;
    }

    public interface DialogClickListener {
        void onDialogLeftButtonPressed();
        void onDialogRightButtonPressed();
    }
}
