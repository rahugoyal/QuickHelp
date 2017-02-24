package com.example.rahul.quickhelp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rahul.quickhelp.R;

/**
 * Created by Rahul on 2/22/2017.
 */

public class HelpFragment extends android.support.v4.app.Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.help_layout, container, false);
        Log.e("jk","ik");
        return view;
    }
}
