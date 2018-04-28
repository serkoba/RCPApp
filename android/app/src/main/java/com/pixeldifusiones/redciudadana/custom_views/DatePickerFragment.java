package com.pixeldifusiones.redciudadana.custom_views;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by daniel.streitenberger on 18/03/2017.
 */
public class DatePickerFragment extends DialogFragment {

    DatePickerDialog.OnDateSetListener mListener;

    public static DatePickerFragment newInstance() {
        DatePickerFragment fragment = new DatePickerFragment();
        return fragment;
    }

    public void setOnDateSetListener(DatePickerDialog.OnDateSetListener listener) {
        mListener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(getActivity(), mListener, year, month, day);
        dpd.getDatePicker().setMaxDate(new Date().getTime());

        return dpd;
    }

}
