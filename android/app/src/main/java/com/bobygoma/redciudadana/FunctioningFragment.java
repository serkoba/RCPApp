package com.bobygoma.redciudadana;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FunctioningFragment extends Fragment {
    public static final String TAG = "FunctioningFragment";

    public FunctioningFragment() {
        // Required empty public constructor
    }
    public static FunctioningFragment newInstance() {
        FunctioningFragment fragment = new FunctioningFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_functioning, container, false);
    }

}
