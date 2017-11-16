package com.bobygoma.redciudadana;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bobygoma.redciudadana.controllers.AccountController;
import com.bobygoma.redciudadana.custom_views.DatePickerFragment;
import com.bobygoma.redciudadana.custom_views.IntervalTimePickerDialog;
import com.bobygoma.redciudadana.custom_views.LoadingFragmentDialog;
import com.bobygoma.redciudadana.services.SendLocationService;
import com.bobygoma.redciudadana.utils.Commons;
import com.bobygoma.redciudadana.utils.PermissionsUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ToBeVolunteerFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = "ToBeVolunteerFragment";
    int mStartTime = -1;
    int mEndTime = -1;
    private AppCompatCheckBox mCheckboxFainting;
    private AppCompatCheckBox mCheckboxHeartAttack;
    private AppCompatCheckBox mCheckboxInjuredAccident;
    private AppCompatCheckBox mCheckboxOther;
    private AppCompatCheckBox mCheckboxPanic;
    private TextView mTextViewLastRCPTrainingDate;
    private TextView mTextViewAvailableStartTime;
    private TextView mTextViewAvailableEndTime;
    private AccountController mAccountController;
    private SeekBar mSeekBarMaxDistance;

    public ToBeVolunteerFragment() {
        // Required empty public constructor
    }

    public static ToBeVolunteerFragment newInstance() {
        ToBeVolunteerFragment fragment = new ToBeVolunteerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        mAccountController = AccountController.getInstance(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_to_be_volunteer, container, false);

        BaseActivity baseActivity = (BaseActivity) getActivity();
        ActionBar actionBar = baseActivity.getBaseActivityActionBar();
        if (actionBar != null) {
            baseActivity.setActionBarTitle(getString(R.string.information));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());

        mCheckboxFainting = (AppCompatCheckBox) view.findViewById(R.id.checkBoxFainting);
        mCheckboxFainting.setChecked(sp.getBoolean(Commons.SHARED_PREFERENCE_EMERGENCY_FAINT, false));

        mCheckboxHeartAttack = (AppCompatCheckBox) view.findViewById(R.id.checkBoxHeartAttack);
        mCheckboxHeartAttack.setChecked(sp.getBoolean(Commons.SHARED_PREFERENCE_EMERGENCY_HEART_ATTACK, false));

        mCheckboxInjuredAccident = (AppCompatCheckBox) view.findViewById(R.id.checkBoxInjuredAccident);
        mCheckboxInjuredAccident.setChecked(sp.getBoolean(Commons.SHARED_PREFERENCE_EMERGENCY_WOUND, false));

        mCheckboxOther = (AppCompatCheckBox) view.findViewById(R.id.checkBoxOther);
        mCheckboxOther.setChecked(sp.getBoolean(Commons.SHARED_PREFERENCE_EMERGENCY_OTHER, false));

        mCheckboxPanic = (AppCompatCheckBox) view.findViewById(R.id.checkBoxPanic);
        mCheckboxPanic.setChecked(sp.getBoolean(Commons.SHARED_PREFERENCE_EMERGENCY_PANIC, false));

        mTextViewLastRCPTrainingDate = (TextView) view.findViewById(R.id.textViewLastRCPTrainingDate);
        mTextViewLastRCPTrainingDate.setOnClickListener(this);

        mTextViewAvailableStartTime = (TextView) view.findViewById(R.id.textViewAvailableStartTime);
        mTextViewAvailableStartTime.setOnClickListener(this);
        mStartTime = sp.getInt(Commons.SHARED_PREFERENCE_AVAILABLE_TIME_START, -1);
        mTextViewAvailableStartTime.setText((mStartTime == -1) ? getResources().getString(R.string.insert_date) : (mStartTime / 100) + ":" + (mStartTime % 100) + "hs");

        mTextViewAvailableEndTime = (TextView) view.findViewById(R.id.textViewAvailableEndTime);
        mTextViewAvailableEndTime.setOnClickListener(this);
        mEndTime = sp.getInt(Commons.SHARED_PREFERENCE_AVAILABLE_TIME_END, -1);
        mTextViewAvailableEndTime.setText((mEndTime == -1) ? getResources().getString(R.string.insert_date) : (mEndTime / 100) + ":" + (mEndTime % 100) + "hs");

        final TextView textViewMaxDistance = (TextView) view.findViewById(R.id.textViewMaxDistanceKm);
        textViewMaxDistance.setText(String.format(
                Locale.getDefault(),
                "%.1f %s",
                sp.getInt(Commons.SHARED_PREFERENCE_EMERGENCY_RADIO, 2) / 1000f,
                getResources().getString(R.string.km))
        );

        mSeekBarMaxDistance = (SeekBar) view.findViewById(R.id.seekBar);
        mSeekBarMaxDistance.getProgressDrawable().setColorFilter(ContextCompat.getColor(
                getContext(), R.color.yellow), android.graphics.PorterDuff.Mode.SRC_IN);
        mSeekBarMaxDistance.setProgress(sp.getInt(Commons.SHARED_PREFERENCE_EMERGENCY_RADIO, 20) / 100);
        mSeekBarMaxDistance.getThumb().setColorFilter(ContextCompat.getColor(getContext(), R.color.yellow), PorterDuff.Mode.MULTIPLY);
        mSeekBarMaxDistance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewMaxDistance.setText(String.format(Locale.getDefault(), "%.1f %s", progress / 10f, getResources().getString(R.string.km)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Button buttonSave = (Button) view.findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(this);
        buttonSave.setText(
                (sp.getBoolean(Commons.SHARED_PREFERENCE_IS_VOLUNTEER, false))
                        ? getResources().getString(R.string.update)
                        : getResources().getString(R.string.save)
        );

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textViewLastRCPTrainingDate: {
                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance();
                datePickerFragment.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, day);

                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        mTextViewLastRCPTrainingDate.setText(format.format(calendar.getTime()));
                    }
                });
                datePickerFragment.show(getFragmentManager(), "Date Picker");
            }
            case R.id.textViewAvailableStartTime: {
                int hour = mStartTime / 100;
                int minute = mStartTime % 100;
                IntervalTimePickerDialog mTimePicker;
                mTimePicker = new IntervalTimePickerDialog(getContext(), new IntervalTimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        mTextViewAvailableStartTime.setText(selectedHour + ":" + selectedMinute + "hs");
                        mStartTime = selectedHour * 100 + selectedMinute;
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                break;
            }
            case R.id.textViewAvailableEndTime: {
                int hour = mEndTime / 100;
                int minute = mEndTime % 100;
                IntervalTimePickerDialog mTimePicker;
                mTimePicker = new IntervalTimePickerDialog(getContext(), new IntervalTimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        mTextViewAvailableEndTime.setText(selectedHour + ":" + selectedMinute + "hs");
                        mEndTime = selectedHour * 100 + selectedMinute;
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                break;
            }
            case R.id.buttonSave: {
                if (mStartTime != -1 && mEndTime != -1) {
                    mAccountController.setIsVolunteer(
                            mCheckboxOther.isChecked(),
                            mCheckboxFainting.isChecked(),
                            mCheckboxHeartAttack.isChecked(),
                            mCheckboxInjuredAccident.isChecked(),
                            mCheckboxPanic.isChecked(),
                            mSeekBarMaxDistance.getProgress() * 100, //we send distance in meters -> we need to map  progress value to meters
                            mStartTime,
                            mEndTime);
                } else {
                    Toast.makeText(getContext(), R.string.ingrese_rango_emergencia, Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {
            case PermissionsUtil.PERMISSION_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (PermissionsUtil.hasGrantedPermission(grantResults)) {
                    getActivity().startService(new Intent(getContext(), SendLocationService.class));
                }
            }
        }
    }

    // Called in the same thread (default)
    public void onEvent(Integer eventMessage) {

        switch (eventMessage) {
            case Commons.IS_VOLUNTEER_START: {
                (new LoadingFragmentDialog()).show(getFragmentManager(), LoadingFragmentDialog.TAG);
                break;
            }
            case Commons.IS_VOLUNTEER_FINISH: {
                ((LoadingFragmentDialog) getFragmentManager().findFragmentByTag(LoadingFragmentDialog.TAG)).dismiss();
                Toast.makeText(getContext(), R.string.ahora_eres_voluntario, Toast.LENGTH_LONG).show();
                getActivity().onBackPressed();
                break;
            }
            case Commons.IS_VOLUNTEER_ERROR: {
                ((LoadingFragmentDialog) getFragmentManager().findFragmentByTag(LoadingFragmentDialog.TAG)).dismiss();
                Toast.makeText(getContext(), R.string.error_registro_voluntario, Toast.LENGTH_LONG).show();
                break;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mAccountController.registerForEvents(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mAccountController.unregisterForEvents(this);
    }
}
