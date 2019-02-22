package ru.lod_misis.ithappened.ui.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import javax.inject.Inject;

import ru.lod_misis.ithappened.R;
import ru.lod_misis.ithappened.ui.ItHappenedApplication;
import ru.lod_misis.ithappened.ui.activities.UserActionsActivity;

public class StartIntroFragment extends Fragment {

    @Inject
    SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.start_intro_screen, container, false);
        ItHappenedApplication.getAppComponent().inject(this);
        Button button = view.findViewById(R.id.miss_tutorial);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences.edit().putBoolean("Tutorial", true).apply();
                Intent intent = new Intent(getContext(), UserActionsActivity.class);
                getActivity().startActivity(intent);
            }
        });

        return view;
    }
}
