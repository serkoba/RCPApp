package com.bobygoma.redciudadana;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bobygoma.redciudadana.backend.EmergencyNotification;
import com.bobygoma.redciudadana.backend.ModifyHelpBody;
import com.bobygoma.redciudadana.controllers.AccountController;
import com.bobygoma.redciudadana.custom_views.LoadingFragmentDialog;
import com.bobygoma.redciudadana.custom_views.TwoButtonsDialogFragment;
import com.bobygoma.redciudadana.utils.Commons;
import com.bobygoma.redciudadana.utils.Utils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AskingForHelpMapFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener, TwoButtonsDialogFragment.DialogClickListener {
    public static final String TAG = "AskingForHelpMapFragment";

    private static final String SHOW_EMERGENCIES = "SHOW_EMERGENCIES";
    private static final String EMERGENCY_NOTIFICATION = "EMERGENCY_NOTIFICATION";

    MapView mMapView;
    GoogleMap mMap;
    ArrayList<EmergencyNotification> emergencyNotifications;
    Map<Marker, String> markersMapToEmails;

    LinearLayout mLinearLayoutButtonsWrapper;

    AccountController mAccountController;

    int mEmergencyStatus;
    String emergencyEmail;
    private boolean showEmergencies;
    public AskingForHelpMapFragment() {
        // Required empty public constructor
    }

    public static AskingForHelpMapFragment newInstance(boolean showEmergencies) {
        AskingForHelpMapFragment fragment = new AskingForHelpMapFragment();
        Bundle args = new Bundle();
        args.putBoolean(SHOW_EMERGENCIES, showEmergencies);
        fragment.setArguments(args);
        return fragment;
    }

    public static AskingForHelpMapFragment newInstance(EmergencyNotification emergencyNotification) {
        AskingForHelpMapFragment fragment = new AskingForHelpMapFragment();
        Bundle args = new Bundle();
        args.putBoolean(SHOW_EMERGENCIES, false);
        args.putParcelable(EMERGENCY_NOTIFICATION, emergencyNotification);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        emergencyNotifications = new ArrayList<>();
        mAccountController = AccountController.getInstance(getContext());

        Bundle extras = getArguments();
        if (extras != null) {
            showEmergencies = (extras.getBoolean(SHOW_EMERGENCIES, false));
            if (showEmergencies) {
                mAccountController.getEmergencies();
                BaseActivity baseActivity = (BaseActivity) getActivity();
                ActionBar actionBar = baseActivity.getBaseActivityActionBar();
                if (actionBar != null) {
                    baseActivity.setActionBarTitle(getString(R.string.emergencies));
                    actionBar.setDisplayHomeAsUpEnabled(true);
                }
            } else {
                emergencyNotifications.add((EmergencyNotification) extras.getParcelable(EMERGENCY_NOTIFICATION));
                emergencyEmail = emergencyNotifications.get(0).getEmail();
            }
        }

        markersMapToEmails = new HashMap<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.asking_for_help_map_fragment, container, false);

        mLinearLayoutButtonsWrapper = (LinearLayout) view.findViewById(R.id.linearLayoutButtonsWrapper);
        mLinearLayoutButtonsWrapper.setVisibility((showEmergencies) ? View.GONE : View.VISIBLE);
        view.findViewById(R.id.buttonFalse).setVisibility((!showEmergencies) ? View.GONE : View.VISIBLE);

        view.findViewById(R.id.buttonCancel).setOnClickListener(this);
        view.findViewById(R.id.buttonFinish).setOnClickListener(this);
        view.findViewById(R.id.buttonFalse).setOnClickListener(this);

        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        mMapView.getMapAsync(this);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        loadMapInfo();
    }

    void loadMapInfo() {

        mMap.clear();
        markersMapToEmails.clear();

        if (showEmergencies) {
            for (int i = 0; i < emergencyNotifications.size(); ++i) {
                EmergencyNotification emergency = emergencyNotifications.get(i);
                LatLng latLng = new LatLng(emergency.getLatitude(), emergency.getLongitude());
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .icon(
                                BitmapDescriptorFactory.fromBitmap(
                                        Utils.getMarkerBitmapFromViewForEmergenciesList(
                                                Commons.getEmergencyTypeString(emergency.getEmergencyType(), getContext()),
                                                emergency.getLatitude(),
                                                emergency.getLongitude(),
                                                emergency.getFirstName() + " " + emergency.getLastName(),
                                                emergency.getAddress(),
                                                getContext()
                                        )
                                )
                        )
                );
                markersMapToEmails.put(marker, emergency.getEmail());
            }

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    mLinearLayoutButtonsWrapper.setVisibility(View.VISIBLE);
                    emergencyEmail = markersMapToEmails.get(marker);
                    return false;
                }
            });

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Commons.BAHIA_BLANCA_LAT, Commons.BAHIA_BLANCA_LNG), Commons.DEFAULT_ZOOM));
        } else {//asking for help
            EmergencyNotification emergencyNotification = null;
            if (emergencyNotifications != null && emergencyNotifications.size() == 1) {
                emergencyNotification = emergencyNotifications.get(0);
            }
            if (emergencyNotification != null) {
                LatLng latLng = new LatLng(emergencyNotification.getLatitude(), emergencyNotification.getLongitude());
                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .icon(
                                BitmapDescriptorFactory.fromBitmap(
                                        Utils.getMarkerBitmapFromViewForHelp(
                                                Commons.getEmergencyTypeString(emergencyNotification.getEmergencyType(), getContext()),
                                                emergencyNotification.getLatitude(),
                                                emergencyNotification.getLongitude(),
                                                emergencyNotification.getUsersNotified(),
                                                emergencyNotification.getAddress(),
                                                getContext()
                                        )
                                )
                        )
                );

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, Commons.DEFAULT_ZOOM));
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        mAccountController.registerForEvents(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        mAccountController.unregisterForEvents(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonCancel: {
                mEmergencyStatus = ModifyHelpBody.STATUS_CANCEL;
                TwoButtonsDialogFragment twoButtonsDialogFragment =
                        TwoButtonsDialogFragment.getInstance(
                                getString(R.string.cancelar_emergencia)
                        , getString(R.string.no), getString(R.string.si) );
                twoButtonsDialogFragment.setTargetFragment(this, 0);
                twoButtonsDialogFragment.show(getFragmentManager(), TwoButtonsDialogFragment.TAG);
                break;
            }
            case R.id.buttonFinish: {
                mEmergencyStatus =  ModifyHelpBody.STATUS_COMPLETE;
                TwoButtonsDialogFragment twoButtonsDialogFragment =
                        TwoButtonsDialogFragment.getInstance(
                                getString(R.string.finalizar_emergencia)
                                , getString(R.string.no), getString(R.string.si) );
                twoButtonsDialogFragment.setTargetFragment(this, 0);
                twoButtonsDialogFragment.show(getFragmentManager(), TwoButtonsDialogFragment.TAG);
                break;
            }
            case R.id.buttonFalse: {
                mEmergencyStatus = ModifyHelpBody.STATUS_FALSE_ALARM;
                TwoButtonsDialogFragment twoButtonsDialogFragment =
                        TwoButtonsDialogFragment.getInstance(
                                getString(R.string.falsa_emergencia)
                                , getString(R.string.no), getString(R.string.si) );
                twoButtonsDialogFragment.setTargetFragment(this, 0);
                twoButtonsDialogFragment.show(getFragmentManager(), TwoButtonsDialogFragment.TAG);
                break;
            }
        }
    }

    public void onEvent(Integer eventMessage) {

        switch (eventMessage) {
            case Commons.MODIFY_EMERGENCY_START: {
                (new LoadingFragmentDialog()).show(getFragmentManager(), LoadingFragmentDialog.TAG);
                break;
            }
            case Commons.MODIFY_EMERGENCY_FINISH: {
                LoadingFragmentDialog lfd = ((LoadingFragmentDialog) getFragmentManager().findFragmentByTag(LoadingFragmentDialog.TAG));
                if (lfd != null) {
                    lfd.dismiss();
                }
                if (!showEmergencies) {
                    String message = "";
                    switch (mEmergencyStatus){
                        case ModifyHelpBody.STATUS_CANCEL:{
                            message = getContext().getString(R.string.emergency_canceled);
                            break;
                        }
                        case ModifyHelpBody.STATUS_COMPLETE:{
                            message = getContext().getString(R.string.emergency_finished);
                            break;
                        }
                        case ModifyHelpBody.STATUS_FALSE_ALARM:{
                            message = getContext().getString(R.string.emergency_false_alarm);
                            break;
                        }
                    }

                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
                    sp.edit().putString(Commons.SHARED_PREFERENCE_EMERGENCY_CREATED, "").commit();
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                } else {
                    mAccountController.getEmergencies();
                }
                break;
            }
            case Commons.MODIFY_EMERGENCY_ERROR: {
                LoadingFragmentDialog lfd = ((LoadingFragmentDialog) getFragmentManager().findFragmentByTag(LoadingFragmentDialog.TAG));
                if (lfd != null) {
                    lfd.dismiss();
                }

                if (!showEmergencies) {
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
                    sp.edit().putString(Commons.SHARED_PREFERENCE_EMERGENCY_CREATED, "").commit();
                    Toast.makeText(getContext(), "La emergencia expiro o ya fue atendida/cancelada", Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                }
                break;
            }
            case Commons.GET_EMERGENCIES_START: {
                (new LoadingFragmentDialog()).show(getFragmentManager(), LoadingFragmentDialog.TAG);
                break;
            }
            case Commons.GET_EMERGENCIES_FINISH: {
                mLinearLayoutButtonsWrapper.setVisibility(View.GONE);
                emergencyNotifications = mAccountController.getEmergenciesList();
                LoadingFragmentDialog lfd = ((LoadingFragmentDialog) getFragmentManager().findFragmentByTag(LoadingFragmentDialog.TAG));
                if (lfd != null) {
                    lfd.dismiss();
                }
                if (mMap == null) {
                    mMapView.getMapAsync(this);
                } else {
                    loadMapInfo();
                }
                break;
            }
            case Commons.GET_EMERGENCIES_ERROR: {
                LoadingFragmentDialog lfd = ((LoadingFragmentDialog) getFragmentManager().findFragmentByTag(LoadingFragmentDialog.TAG));
                if (lfd != null) {
                    lfd.dismiss();
                }
                break;
            }
        }
    }

    @Override
    public void onDialogLeftButtonPressed() {

    }

    @Override
    public void onDialogRightButtonPressed() {
        mAccountController.modifyStatusEmergency(emergencyEmail, mEmergencyStatus);
    }
}
