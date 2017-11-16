package com.bobygoma.redciudadana.utils;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.bobygoma.redciudadana.*;
import com.bobygoma.redciudadana.R;

/**
 * Created by daniel.streitenberger on 18/03/2017.
 */
public class PermissionsUtil {

    public static final int PERMISSION_LOCATION = 0;

    /**
     * Check and ask user for permission (SDK > 23) for activities
     */

    public static boolean checkForLocationPermissions(final Fragment fragment) {

        if (ContextCompat.checkSelfPermission(fragment.getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(fragment.getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            if (fragment.shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_COARSE_LOCATION) ||
                    fragment.shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_COARSE_LOCATION)) {

                //Explain user
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(fragment.getActivity());
                builder.setMessage(com.bobygoma.redciudadana.R.string.grant_location_access_dialog_message)
                        .setTitle(R.string.grant_location_access_dialog_title);

                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {

                        if (fragment.isAdded()) {
                            fragment.requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_LOCATION);
                        }
                    }

                });
                builder.create().show();

            } else {

                fragment.requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_LOCATION);
            }

            return false;
        }

        return true;
    }

    public static boolean checkForAppCompatLocationPermissions(final Fragment fragment) {

        if (ActivityCompat.checkSelfPermission(fragment.getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(fragment.getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (fragment.shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_COARSE_LOCATION) ||
                    fragment.shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_COARSE_LOCATION)) {

                //Explain user
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(fragment.getActivity());
                builder.setMessage(com.bobygoma.redciudadana.R.string.grant_location_access_dialog_message)
                        .setTitle(R.string.grant_location_access_dialog_title);

                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {

                        if (fragment.isAdded()) {
                            fragment.requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_LOCATION);
                        }
                    }

                });
                builder.create().show();

            } else {

                fragment.requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_LOCATION);
            }

            return false;
        }

        return true;
    }

    public static boolean hasGrantedPermission(int[] grantResults) {

        if (grantResults.length == 0) return false;

        return grantResults[0] == PackageManager.PERMISSION_GRANTED;

    }
}
