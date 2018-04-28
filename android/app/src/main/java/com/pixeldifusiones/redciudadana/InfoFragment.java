package com.pixeldifusiones.redciudadana;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class InfoFragment extends Fragment {
    public static final String TAG = "InfoFragment";

    public InfoFragment() {
        // Required empty public constructor
    }

    public static InfoFragment newInstance() {
        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        BaseActivity baseActivity = (BaseActivity) getActivity();
        ActionBar actionBar = baseActivity.getBaseActivityActionBar();
        if (actionBar != null) {
            baseActivity.setActionBarTitle(getString(R.string.information));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        TextView aboutTextView = (TextView) view.findViewById(R.id.info_text_view);
        aboutTextView.setText(Html.fromHtml(getString(R.string.info_text)));
        return view;
    }

}
