package ru.lod_misis.ithappened.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import ru.lod_misis.ithappened.R;

public class IntroFragment extends Fragment {

    public static IntroFragment newInstanse(@NonNull int image) {
        IntroFragment introFragment = new IntroFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("image", image);
        introFragment.setArguments(bundle);

        return introFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.intro_fragment, container, false);
        ImageView imageView = v.findViewById(R.id.image);
        if (getArguments() != null) {
            imageView.setImageResource(getArguments().getInt("image"));
        } else
            throw new NullPointerException("Вы не передали аргументы");
        return v;
    }
}
