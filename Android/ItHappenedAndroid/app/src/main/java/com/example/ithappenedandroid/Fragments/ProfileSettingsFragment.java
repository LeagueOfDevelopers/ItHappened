package com.example.ithappenedandroid.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.ithappenedandroid.R;

public class ProfileSettingsFragment extends Fragment {

    TextView userMail;
    TextView userNickName;
    Button logOut;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_settings, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Настройки профиля");

        userMail =(TextView) getActivity().findViewById(R.id.mail);
        userNickName = (TextView) getActivity().findViewById(R.id.nickname);
        logOut = (Button) getActivity().findViewById(R.id.logout);

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogOutDailogFragment logout = new LogOutDailogFragment();
                logout.show(getFragmentManager(), "Logout");
            }
        });

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MAIN_KEYS", Context.MODE_PRIVATE);
        userMail.setText(sharedPreferences.getString("UserId", ""));
        userNickName.setText(sharedPreferences.getString("Nick", ""));

    }
}
