package com.pixeldifusiones.redciudadana;


import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pixeldifusiones.redciudadana.backend.EmergencyNotification;
import com.pixeldifusiones.redciudadana.controllers.AccountController;
import com.pixeldifusiones.redciudadana.custom_views.LoadingFragmentDialog;
import com.pixeldifusiones.redciudadana.custom_views.OneButtonDialogFragment;
import com.pixeldifusiones.redciudadana.custom_views.TwoButtonsDialogFragment;
import com.pixeldifusiones.redciudadana.services.SendLocationService;
import com.pixeldifusiones.redciudadana.utils.Commons;
import com.pixeldifusiones.redciudadana.utils.PermissionsUtil;

import java.util.List;

import io.nlopez.smartlocation.OnReverseGeocodingListener;
import io.nlopez.smartlocation.SmartLocation;


public class MainFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = "MainFragment";

    MainActivityListener mainActivityListener;

    AccountController mAccountController;

    Button mButtonVolunteer;
    Button mButtonAskForHelp;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAccountController = AccountController.getInstance(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());

        mButtonAskForHelp = (Button) view.findViewById(R.id.buttonAskForHelp);
        mButtonAskForHelp.setOnClickListener(this);
        mButtonAskForHelp.setText(
                showCreatedEmergency()
                        ? getResources().getString(R.string.ver_emergencia)
                        : getResources().getString(R.string.ask_for_help)
        );

        mButtonVolunteer = (Button) view.findViewById(R.id.buttonToBeVolunteer);
        mButtonVolunteer.setOnClickListener(this);
        mButtonVolunteer.setText(
                (sp.getBoolean(Commons.SHARED_PREFERENCE_IS_VOLUNTEER, false))
                        ? getResources().getString(R.string.perfil_de_voluntario)
                        : getResources().getString(R.string.to_be_volunteer)
        );
        ((TextView) view.findViewById(R.id.textViewUserName)).setText(
                String.format(
                        "%s %s",
                        sp.getString(Commons.SHARED_PREFERENCE_FIRST_NAME, ""),
                        sp.getString(Commons.SHARED_PREFERENCE_LAST_NAME, "")
                )
        );

        view.findViewById(R.id.buttonProfile).setOnClickListener(this);
        view.findViewById(R.id.buttonInfo).setOnClickListener(this);
        view.findViewById(R.id.buttonEmergencies).setOnClickListener(this);
        view.findViewById(R.id.buttonLogout).setOnClickListener(this);
        view.findViewById(R.id.buttonPanic).setOnClickListener(this);

        return view;
    }

    boolean showCreatedEmergency() {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        if (sp.contains(Commons.SHARED_PREFERENCE_EMERGENCY_CREATED) &&
                !sp.getString(Commons.SHARED_PREFERENCE_EMERGENCY_CREATED, "").equals("")) {
            EmergencyNotification emergencyNotification = new EmergencyNotification(sp.getString(Commons.SHARED_PREFERENCE_EMERGENCY_CREATED, ""));
            return Commons.isEmergencyActive(emergencyNotification.getDateTime());
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        mAccountController.registerForEvents(this);

        BaseActivity baseActivity = (BaseActivity) getActivity();
        ActionBar actionBar = baseActivity.getBaseActivityActionBar();
        if (actionBar != null) {
            baseActivity.setActionBarTitle(getString(R.string.app_name));
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        if (PermissionsUtil.checkForLocationPermissions(this)) {
            getActivity().startService(new Intent(getContext(), SendLocationService.class));
        }


        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        mButtonVolunteer.setText(
                (sp.getBoolean(Commons.SHARED_PREFERENCE_IS_VOLUNTEER, false))
                        ? getResources().getString(R.string.perfil_de_voluntario)
                        : getResources().getString(R.string.to_be_volunteer)
        );
        mButtonAskForHelp.setText(
                showCreatedEmergency()
                        ? getResources().getString(R.string.ver_emergencia)
                        : getResources().getString(R.string.ask_for_help)
        );
    }

    @Override
    public void onPause() {
        super.onPause();
        mAccountController.unregisterForEvents(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivityListener = (MainActivityListener) context;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonAskForHelp: {
                if (showCreatedEmergency()) {

                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
                    getFragmentManager().beginTransaction()
                            .replace(
                                    R.id.container,
                                    AskingForHelpMapFragment.newInstance(
                                            new EmergencyNotification(sp.getString(Commons.SHARED_PREFERENCE_EMERGENCY_CREATED, ""))
                                    ),
                                    AskingForHelpMapFragment.TAG
                            ).addToBackStack(null)
                            .commit();
                } else {
                    getFragmentManager().beginTransaction()
                            .replace(R.id.container, AskingForHelpFragment.newInstance(), AskingForHelpFragment.TAG
                            ).addToBackStack(null)
                            .commit();
                }
                break;
            }
            case R.id.buttonToBeVolunteer: {
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, ToBeVolunteerFragment.newInstance(), ToBeVolunteerFragment.TAG
                        ).addToBackStack(null)
                        .commit();
                break;
            }
            case R.id.buttonInfo: {
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, InfoFragment.newInstance(), InfoFragment.TAG
                        ).addToBackStack(null)
                        .commit();
                break;
            }
            case R.id.buttonEmergencies: {
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, AskingForHelpMapFragment.newInstance(true), AskingForHelpMapFragment.TAG
                        ).addToBackStack(null)
                        .commit();
                break;
            }
            case R.id.buttonLogout: {
                mAccountController.logout();
                break;
            }
            case R.id.buttonProfile: {
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, ProfileFragment.newInstance(), ProfileFragment.TAG
                        ).addToBackStack(null)
                        .commit();
                break;
            }
            case R.id.buttonPanic: {
                Location location = ((BaseActivity) getActivity()).mLocationService.getCurrentLocation();
                if (location != null) {
                    SmartLocation.with(getContext()).geocoding()
                            .reverse(
                                    location,
                                    new OnReverseGeocodingListener() {
                                        @Override
                                        public void onAddressResolved(Location location, List<Address> list) {
                                            mAccountController.askForHelp(
                                                    location.getLatitude(),
                                                    location.getLongitude(),
                                                    Commons.EMERGENCY_PANIC,
                                                    (list != null && list.size() > 0 && list.get(0).getMaxAddressLineIndex() > 0) ? list.get(0).getAddressLine(0) : "");

                                        }
                                    }
                            );
                }else{
                    Toast.makeText(getContext(), R.string.Encienda_gps, Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    // Called in the same thread (default)
    public void onEvent(Integer eventMessage) {

        switch (eventMessage) {
            case Commons.LOGOUT_START: {
                (new LoadingFragmentDialog()).show(getFragmentManager(), LoadingFragmentDialog.TAG);
                break;
            }
            case Commons.LOGOUT_FINISH: {
                ((LoadingFragmentDialog) getFragmentManager().findFragmentByTag(LoadingFragmentDialog.TAG)).dismiss();
                Intent i = new Intent(getActivity(), LoginActivity.class);
                startActivity(i);
                mainActivityListener.finishActivity();
                break;
            }
            case Commons.LOGOUT_ERROR: {
                ((LoadingFragmentDialog) getFragmentManager().findFragmentByTag(LoadingFragmentDialog.TAG)).dismiss();
                break;
            }
            case Commons.ASK_FOR_HELP_START: {
                (new LoadingFragmentDialog()).show(getFragmentManager(), LoadingFragmentDialog.TAG);
                break;
            }
            case Commons.ASK_FOR_HELP_FINISH: {
                ((LoadingFragmentDialog) getFragmentManager().findFragmentByTag(LoadingFragmentDialog.TAG)).dismiss();
                OneButtonDialogFragment oneButtonsDialogFragment =
                        OneButtonDialogFragment.getInstance(
                                getString(R.string.emergencia_panico_enviada)
                                , getString(R.string.aceptar) );
                oneButtonsDialogFragment.setTargetFragment(this, 0);
                oneButtonsDialogFragment.show(getFragmentManager(), TwoButtonsDialogFragment.TAG);

                break;
            }
            case Commons.ASK_FOR_HELP_ERROR: {
                Toast.makeText(getContext(), R.string.error_alerta_panico, Toast.LENGTH_SHORT).show();
                ((LoadingFragmentDialog) getFragmentManager().findFragmentByTag(LoadingFragmentDialog.TAG)).dismiss();
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

    public interface MainActivityListener {
        public void finishActivity(); // not shown in impl above
    }
}
