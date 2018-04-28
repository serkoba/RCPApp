package com.pixeldifusiones.redciudadana.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.pixeldifusiones.redciudadana.R;

/**
 * Created by daniel.streitenberger on 19/04/2017.
 */
public class Utils {

    public static Bitmap getMarkerBitmapFromViewForHelp(String emergencyType, double lat, double lng, int count, String address, Context context) {

        View customMarkerView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.map_marker, null);
        ((TextView) customMarkerView.findViewById(R.id.textViewEmergencyType)).setText(emergencyType);
        ((TextView) customMarkerView.findViewById(R.id.textViewEmergencyType)).setText(emergencyType);
        if (address != null) {
            ((TextView) customMarkerView.findViewById(R.id.textViewLocation)).setText(address);
        } else {
            ((TextView) customMarkerView.findViewById(R.id.textViewLocation)).setText(String.format("%s/%s", lat, lng));
        }
        ((TextView) customMarkerView.findViewById(R.id.textViewAmountOfUsers)).setText(String.format("%d %s", count, context.getString(R.string.users)));
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }

    public static Bitmap getMarkerBitmapFromViewForEmergenciesList(String emergencyType, double lat, double lng, String userName, String address, Context context) {

        View customMarkerView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.map_marker, null);
        ((TextView) customMarkerView.findViewById(R.id.textViewEmergencyType)).setText(emergencyType);
        if (address != null) {
            ((TextView) customMarkerView.findViewById(R.id.textViewLocation)).setText(address);
        } else {
            ((TextView) customMarkerView.findViewById(R.id.textViewLocation)).setText(String.format("%s/%s", lat, lng));
        }
        ((TextView) customMarkerView.findViewById(R.id.textViewEmergencySentTo)).setText(context.getString(R.string.person_who_needs_help));
        ((TextView) customMarkerView.findViewById(R.id.textViewAmountOfUsers)).setText(userName);

        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }
}
