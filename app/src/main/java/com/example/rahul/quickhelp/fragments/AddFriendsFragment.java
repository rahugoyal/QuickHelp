package com.example.rahul.quickhelp.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.rahul.quickhelp.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Rahul on 2/22/2017.
 */

public class AddFriendsFragment extends Fragment {
    private EditText mEtFriend1, mEtFriend2, mEtFriend3;
    private Button mBtAdd;
    private SharedPreferences sharedPreferences;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_friends_fragments, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeView(view);
    }

    private void initializeView(View view) {
        mEtFriend1 = (EditText) view.findViewById(R.id.et_friend1);
        mEtFriend2 = (EditText) view.findViewById(R.id.et_friend2);
        mEtFriend3 = (EditText) view.findViewById(R.id.et_friend3);
        mBtAdd = (Button) view.findViewById(R.id.bt_Add);


        mBtAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFriendsToSharedPreferences();

            }
        });

    }

    private void addFriendsToSharedPreferences() {
        sharedPreferences = getActivity().getSharedPreferences("data", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (validateData()) {
            editor.putString("friend1", mEtFriend1.getText().toString());
            editor.putString("friend2", mEtFriend2.getText().toString());
            editor.putString("friend3", mEtFriend3.getText().toString());
            editor.putBoolean("isFirstTime", false);

            editor.commit();

            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, new HomeFragment()).commit();
        }
    }

    private boolean validateData() {
        boolean status = false;

        if (mEtFriend1.getText().toString().isEmpty()) {
            mEtFriend1.setError("mobile number of first friend can not be empty");
        } else if (mEtFriend1.getText().toString().length() != 10) {
            mEtFriend1.setError("invalid mobile number");
        } else if (mEtFriend2.getText().toString().isEmpty()) {
            mEtFriend2.setError("mobile number of second friend can not be empty");
        } else if (mEtFriend2.getText().toString().length() != 10) {
            mEtFriend2.setError("invalid mobile number");
        } else if (mEtFriend3.getText().toString().isEmpty()) {
            mEtFriend3.setError("mobile number of third friend can not be empty");
        } else if (mEtFriend3.getText().toString().length() != 10) {
            mEtFriend3.setError("invalid mobile number");
        } else status = true;
        return status;
    }
}
