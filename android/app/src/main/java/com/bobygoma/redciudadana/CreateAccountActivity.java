package com.bobygoma.redciudadana;

import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bobygoma.redciudadana.backend.SignUpBody;
import com.bobygoma.redciudadana.controllers.AccountController;
import com.bobygoma.redciudadana.custom_views.LoadingFragmentDialog;
import com.bobygoma.redciudadana.utils.Commons;

public class CreateAccountActivity extends BaseActivity implements View.OnClickListener {

    AccountController mAccountController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        findViewById(R.id.buttonSignUp).setOnClickListener(this);

        mAccountController = AccountController.getInstance(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mAccountController.registerForEvents(this);
        ActionBar actionBar = this.getBaseActivityActionBar();
        if(actionBar != null){
            this.setActionBarTitle(getString(R.string.app_name));
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAccountController.unregisterForEvents(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonSignUp:{
                if(
                        !((EditText)findViewById(R.id.editTextFirstName)).getText().toString().equals("") &&
                !((EditText)findViewById(R.id.editTextLastName)).getText().toString().equals("") &&
                        !((EditText)findViewById(R.id.editTextEmail)).getText().toString().equals("") &&
                        !((EditText)findViewById(R.id.editTextPhoneNumber)).getText().toString().equals("") &&
                        !((EditText)findViewById(R.id.editTextAddress)).getText().toString().equals("") &&
                        !((EditText)findViewById(R.id.editTextPassword)).getText().toString().equals("") &&
                        !((EditText)findViewById(R.id.editTextPassword)).getText().toString().equals("")) {
                    mAccountController.signUp(
                            new SignUpBody(
                                    ((EditText) findViewById(R.id.editTextFirstName)).getText().toString(),
                                    ((EditText) findViewById(R.id.editTextLastName)).getText().toString(),
                                    ((EditText) findViewById(R.id.editTextEmail)).getText().toString(),
                                    ((EditText) findViewById(R.id.editTextPhoneNumber)).getText().toString(),
                                    ((EditText) findViewById(R.id.editTextAddress)).getText().toString(),
                                    ((EditText) findViewById(R.id.editTextPassword)).getText().toString(),
                                    ((EditText) findViewById(R.id.editTextPassword)).getText().toString())
                    );
                }
                break;
            }
        }
    }

    // Called in the same thread (default)
    public void onEvent(Integer eventMessage) {

        switch (eventMessage) {
            case Commons.SIGN_UP_START:{
                (new LoadingFragmentDialog()).show(getSupportFragmentManager(), LoadingFragmentDialog.TAG);
                break;
            }
            case Commons.SIGN_UP_FINISH:{
                ((LoadingFragmentDialog) getSupportFragmentManager().findFragmentByTag(LoadingFragmentDialog.TAG)).dismiss();
                finish();
                break;
            }
            case Commons.SIGN_UP_ERROR:{
                ((LoadingFragmentDialog) getSupportFragmentManager().findFragmentByTag(LoadingFragmentDialog.TAG)).dismiss();
                Toast.makeText(getApplicationContext(), "Error en el servidor, vuelva a intentarlo mas tarde", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }
}
