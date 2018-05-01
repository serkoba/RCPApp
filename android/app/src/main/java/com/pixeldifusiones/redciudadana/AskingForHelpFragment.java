package com.pixeldifusiones.redciudadana;


import android.content.SharedPreferences;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pixeldifusiones.redciudadana.backend.EmergencyNotification;
import com.pixeldifusiones.redciudadana.controllers.AccountController;
import com.pixeldifusiones.redciudadana.custom_views.LoadingFragmentDialog;
import com.pixeldifusiones.redciudadana.utils.Commons;

import java.util.Calendar;
import java.util.List;

import io.nlopez.smartlocation.OnReverseGeocodingListener;
import io.nlopez.smartlocation.SmartLocation;

public class AskingForHelpFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = "AskingForHelpFragment";
    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    AccountController mAccountController;
    EditText mEditTextAddress;
    private Location mLastLocation;
    private int emergencyType = 0;

    public AskingForHelpFragment() {
        // Required empty public constructor
    }

    public static AskingForHelpFragment newInstance() {
        return new AskingForHelpFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAccountController = AccountController.getInstance(getContext());
    }

    @Override
    public void onPause() {
        super.onPause();
        mAccountController.unregisterForEvents(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mAccountController.registerForEvents(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_asking_for_help, container, false);

        BaseActivity baseActivity = (BaseActivity) getActivity();
        ActionBar actionBar = baseActivity.getBaseActivityActionBar();
        if (actionBar != null) {
            baseActivity.setActionBarTitle(getString(R.string.asking_for_help));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        view.findViewById(R.id.buttonFainting).setOnClickListener(this);
        view.findViewById(R.id.buttonHeartAttack).setOnClickListener(this);
        view.findViewById(R.id.buttonInjuredAccident).setOnClickListener(this);
        view.findViewById(R.id.buttonOther).setOnClickListener(this);

        mLastLocation = ((BaseActivity) getActivity()).mLocationService.getCurrentLocation();
        mEditTextAddress = (EditText) view.findViewById(R.id.editTextAddress);
        if (mLastLocation != null) {
            SmartLocation.with(getContext()).geocoding()
                    .reverse(mLastLocation, new OnReverseGeocodingListener() {
                        @Override
                        public void onAddressResolved(Location location, List<Address> list) {
                            if (list != null && list.size() > 0 && list.get(0).getMaxAddressLineIndex() != -1) {
                                Address address = list.get(0);
                                String thoroughfare = address.getThoroughfare() != null ? address.getThoroughfare() : "";
                                String subThoroughfare = address.getSubThoroughfare() != null ? address.getSubThoroughfare() : "";
                                mEditTextAddress.setText(String.format("%s %s", thoroughfare, subThoroughfare));
                            }
                        }
                    });
        }
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonFainting: {
                emergencyType = Commons.EMERGENCY_FAINT;
                askForHelp(emergencyType);
                break;
            }
            case R.id.buttonHeartAttack: {
                emergencyType = Commons.EMERGENCY_HEART_ATTACK;
                askForHelp(emergencyType);
                break;
            }
            case R.id.buttonInjuredAccident: {
                emergencyType = Commons.EMERGENCY_WOUND;
                askForHelp(emergencyType);
                break;
            }
            case R.id.buttonOther: {
                emergencyType = Commons.EMERGENCY_OTHER;
                askForHelp(emergencyType);
                break;
            }
        }
    }

    private void askForHelp(int emergencyType) {
        if (mLastLocation != null) {
            mAccountController.askForHelp(mLastLocation.getLatitude(), mLastLocation.getLongitude(), emergencyType, mEditTextAddress.getText().toString());
        } else {
            Toast.makeText(getContext(), R.string.encienda_gps, Toast.LENGTH_SHORT).show();
        }
    }

    // Called in the same thread (default)
    public void onEvent(Integer eventMessage) {
        switch (eventMessage) {
            case Commons.ASK_FOR_HELP_START: {
                (new LoadingFragmentDialog()).show(getFragmentManager(), LoadingFragmentDialog.TAG);
                break;
            }
            case Commons.ASK_FOR_HELP_FINISH: {
                ((LoadingFragmentDialog) getFragmentManager().findFragmentByTag(LoadingFragmentDialog.TAG)).dismiss();

                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());

                EmergencyNotification emergencyNotification = new EmergencyNotification(
                        mLastLocation.getLatitude(),
                        mLastLocation.getLongitude(),
                        emergencyType,
                        Calendar.getInstance().getTimeInMillis(),
                        sp.getString(Commons.SHARED_PREFERENCE_EMAIL, ""),
                        sp.getString(Commons.SHARED_PREFERENCE_FIRST_NAME, ""),
                        sp.getString(Commons.SHARED_PREFERENCE_LAST_NAME, ""),
                        mEditTextAddress.getText().toString(),
                        mAccountController.getAskForHelpResponse().getCount()
                );

                Gson gson = new Gson();
                sp.edit().putString(Commons.SHARED_PREFERENCE_EMERGENCY_CREATED,
                        gson.toJson(emergencyNotification)
                ).apply();

                getFragmentManager().beginTransaction()
                        .replace(R.id.container, AskingForHelpMapFragment.newInstance(
                                emergencyNotification
                                ), AskingForHelpMapFragment.TAG
                        ).commit();
                break;
            }
            case Commons.ASK_FOR_HELP_ERROR: {
                ((LoadingFragmentDialog) getFragmentManager().findFragmentByTag(LoadingFragmentDialog.TAG)).dismiss();
                break;
            }
        }
    }
}
