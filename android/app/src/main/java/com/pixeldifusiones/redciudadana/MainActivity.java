package com.pixeldifusiones.redciudadana;

import android.os.Bundle;
import android.view.MenuItem;

public class MainActivity extends BaseActivity implements MainFragment.MainActivityListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, MainFragment.newInstance(), "MainFragment"
                ).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void finishActivity() {
        finish();
    }
}
