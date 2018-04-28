package com.pixeldifusiones.redciudadana;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.pixeldifusiones.redciudadana.controllers.AccountController;
import com.pixeldifusiones.redciudadana.custom_views.LoadingFragmentDialog;
import com.pixeldifusiones.redciudadana.utils.Commons;
import com.google.firebase.iid.FirebaseInstanceId;


public class LoginActivity extends BaseActivity implements View.OnClickListener {

    EditText mEditTextEmail;
    EditText mEditTextPassword;

    AccountController mAccountController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEditTextEmail = (EditText) findViewById(R.id.editTextEmail);
        mEditTextPassword = (EditText) findViewById(R.id.editTextPassword);

        findViewById(R.id.buttonLogin).setOnClickListener(this);
        findViewById(R.id.buttonSignUp).setOnClickListener(this);

        mAccountController = AccountController.getInstance(getApplicationContext());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonLogin: {
                mAccountController.login(mEditTextEmail.getText().toString(), mEditTextPassword.getText().toString());
                break;
            }
            case R.id.buttonSignUp: {
                Intent i = new Intent(this, CreateAccountActivity.class);
                startActivity(i);
                //finish();
                break;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAccountController.unregisterForEvents(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAccountController.registerForEvents(this);
    }

    // Called in the same thread (default)
    public void onEvent(Integer eventMessage) {

        switch (eventMessage) {
            case Commons.LOGIN_START:{
                (new LoadingFragmentDialog()).show(getSupportFragmentManager(), LoadingFragmentDialog.TAG);
                break;
            }
            case Commons.LOGIN_FINISH:{
                mAccountController.sendFirebaseToken(FirebaseInstanceId.getInstance().getToken());
                break;
            }
            case Commons.FIREBASE_TOKEN_FINISH:{
                mAccountController.getUser();
                break;
            }
            case Commons.GET_USER_FINISH:{
                ((LoadingFragmentDialog) getSupportFragmentManager().findFragmentByTag(LoadingFragmentDialog.TAG)).dismiss();
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                finish();
                mAccountController.sendFirebaseToken(FirebaseInstanceId.getInstance().getToken());
                break;
            }
            case Commons.GET_USER_ERROR:
            case Commons.FIREBASE_TOKEN_ERROR:{
                LoadingFragmentDialog lfd = ((LoadingFragmentDialog) getSupportFragmentManager().findFragmentByTag(LoadingFragmentDialog.TAG));
                if(lfd!=null) {
                    lfd.dismiss();
                }
                break;
            }
            case Commons.LOGIN_ERROR:{
                LoadingFragmentDialog lfd = ((LoadingFragmentDialog) getSupportFragmentManager().findFragmentByTag(LoadingFragmentDialog.TAG));
                if(lfd!=null) {
                    lfd.dismiss();
                }
                Toast.makeText(this, R.string.contrasenia_invalida, Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

}
