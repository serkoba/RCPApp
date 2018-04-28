package com.pixeldifusiones.redciudadana;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.pixeldifusiones.redciudadana.services.SendLocationService;

/**
 * Created by daniel.streitenberger on 06/03/2017.
 */
public class BaseActivity extends AppCompatActivity implements ServiceConnection {
    SendLocationService  mLocationService;


    ActionBar mActionBar;
    View mViewActionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Customize the ActionBar
        mActionBar = getSupportActionBar();

        mViewActionBar = getLayoutInflater().inflate(R.layout.action_bar_text, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        TextView textViewTitle = (TextView) mViewActionBar.findViewById(R.id.actionbar_textview);
        textViewTitle.setText(getString(R.string.app_name));
        if (mActionBar != null) {
            mActionBar.setCustomView(mViewActionBar, params);
            mActionBar.setDisplayShowCustomEnabled(true);
            mActionBar.setDisplayShowTitleEnabled(false);
        }
    }

    public ActionBar getBaseActivityActionBar(){
        return mActionBar;
    }

    public void setActionBarTitle(String title){
        TextView textViewTitle = (TextView) mViewActionBar.findViewById(R.id.actionbar_textview);
        textViewTitle.setText(title);
    }

    // ServiceConnection *************************
    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

        SendLocationService.LocalBinder b = (SendLocationService.LocalBinder) iBinder;
        mLocationService = b.getService();
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }
    // ********************************************


    @Override
    protected void onResume() {
        super.onResume();
        Intent intent= new Intent(this, SendLocationService.class);
        bindService(intent, this, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(this);
    }
}
