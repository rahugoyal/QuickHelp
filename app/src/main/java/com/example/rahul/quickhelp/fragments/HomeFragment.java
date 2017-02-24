package com.example.rahul.quickhelp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.rahul.quickhelp.R;

/**
 * Created by Rahul on 2/22/2017.
 */

public class HomeFragment extends android.support.v4.app.Fragment {
    private ImageButton mBtHelp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_layout, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeViews(view);
    }

    private void initializeViews(View view) {
        mBtHelp = (ImageButton) view.findViewById(R.id.bt_help);

        mBtHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.frag_container, new HelpFragment()).addToBackStack("Help Fragment").commit();

            }
        });
    }
}
