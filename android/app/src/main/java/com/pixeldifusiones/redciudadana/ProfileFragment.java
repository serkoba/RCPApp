package com.pixeldifusiones.redciudadana;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pixeldifusiones.redciudadana.controllers.AccountController;
import com.pixeldifusiones.redciudadana.custom_views.LoadingFragmentDialog;
import com.pixeldifusiones.redciudadana.utils.Commons;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = "ProfileFragment";
    EditText mEditTextFirstName;
    EditText mEditTextLastName;
    EditText mEditTextEmail;
    EditText mEditTextPhoneNumber;
    EditText mEditTextAddress;
    Button mButtonUpdate;
    MenuItem mMenuItemEdit;

    boolean mIsEditMode = false;

    AccountController mAccountController;

    public ProfileFragment() {
        // Required empty public constructor
    }


    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());

        mEditTextFirstName = (EditText) view.findViewById(R.id.editTextFirstName);
        mEditTextFirstName.setText(sp.getString(Commons.SHARED_PREFERENCE_FIRST_NAME, ""));

        mEditTextLastName = (EditText) view.findViewById(R.id.editTextLastName);
        mEditTextLastName.setText(sp.getString(Commons.SHARED_PREFERENCE_LAST_NAME, ""));

        mEditTextEmail = (EditText) view.findViewById(R.id.editTextEmail);
        mEditTextEmail.setText(sp.getString(Commons.SHARED_PREFERENCE_EMAIL, ""));

        mEditTextPhoneNumber = (EditText) view.findViewById(R.id.editTextPhoneNumber);
        mEditTextPhoneNumber.setText(sp.getString(Commons.SHARED_PREFERENCE_PHONE, ""));

        mEditTextAddress = (EditText) view.findViewById(R.id.editTextAddress);
        mEditTextAddress.setText(sp.getString(Commons.SHARED_PREFERENCE_ADDRESS, ""));

        mButtonUpdate = (Button) view.findViewById(R.id.buttonUpdate);
        mButtonUpdate.setOnClickListener(this);

        setIsEditMode(false);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        BaseActivity baseActivity = (BaseActivity) getActivity();
        ActionBar actionBar = baseActivity.getBaseActivityActionBar();
        if (actionBar != null) {
            baseActivity.setActionBarTitle(getString(R.string.information));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mAccountController = AccountController.getInstance(getContext());
    }

    void setIsEditMode(boolean isEditMode) {

        this.mIsEditMode = isEditMode;
        mEditTextFirstName.setFocusable(isEditMode);
        mEditTextFirstName.setFocusableInTouchMode(isEditMode);
        mEditTextFirstName.setClickable(isEditMode);

        mEditTextLastName.setFocusable(isEditMode);
        mEditTextLastName.setFocusableInTouchMode(isEditMode);
        mEditTextLastName.setClickable(isEditMode);

        mEditTextEmail.setFocusable(isEditMode);
        mEditTextEmail.setFocusableInTouchMode(isEditMode);
        mEditTextEmail.setClickable(isEditMode);

        mEditTextPhoneNumber.setFocusable(isEditMode);
        mEditTextPhoneNumber.setFocusableInTouchMode(isEditMode);
        mEditTextPhoneNumber.setClickable(isEditMode);

        mEditTextAddress.setFocusable(isEditMode);
        mEditTextAddress.setFocusableInTouchMode(isEditMode);
        mEditTextAddress.setClickable(isEditMode);

        mButtonUpdate.setVisibility((isEditMode)?View.VISIBLE:View.GONE);
        if(mMenuItemEdit != null) {
            mMenuItemEdit.setVisible(!isEditMode);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflator) {
        inflator.inflate(R.menu.profile_menu, menu);
        mMenuItemEdit = menu.findItem(R.id.menu_edit);
        super.onCreateOptionsMenu(menu, inflator);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_edit:
                setIsEditMode(!mIsEditMode);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonUpdate: {
                mAccountController.updateUser(mEditTextFirstName.getText().toString(),
                        mEditTextLastName.getText().toString(),
                        mEditTextEmail.getText().toString(),
                        mEditTextPhoneNumber.getText().toString(),
                        mEditTextAddress.getText().toString()
                        );
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

    // Called in the same thread (default)
    public void onEvent(Integer eventMessage) {

        switch (eventMessage) {
            case Commons.UPDATE_USER_START: {
                (new LoadingFragmentDialog()).show(getFragmentManager(), LoadingFragmentDialog.TAG);
                break;
            }
            case Commons.UPDATE_USER_FINISH: {
                setIsEditMode(!mIsEditMode);
                Toast.makeText(getContext(), R.string.informacion_actualizada, Toast.LENGTH_SHORT).show();
                ((LoadingFragmentDialog) getFragmentManager().findFragmentByTag(LoadingFragmentDialog.TAG)).dismiss();
                break;
            }
            case Commons.UPDATE_USER_ERROR: {
                Toast.makeText(getContext(), R.string.error_al_actualizar, Toast.LENGTH_SHORT).show();
                ((LoadingFragmentDialog) getFragmentManager().findFragmentByTag(LoadingFragmentDialog.TAG)).dismiss();
                break;
            }
        }
    }
}
