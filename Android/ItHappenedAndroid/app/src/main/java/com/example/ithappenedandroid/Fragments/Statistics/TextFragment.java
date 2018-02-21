package com.example.ithappenedandroid.Fragments.Statistics;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ithappenedandroid.Infrastructure.ITrackingRepository;
import com.example.ithappenedandroid.R;
import com.example.ithappenedandroid.StaticInMemoryRepository;
import com.example.ithappenedandroid.StatisticsHelpers.TextStatisticsHelper;

import java.util.UUID;

/**
 * Created by Пользователь on 20.02.2018.
 */

public class TextFragment extends Fragment {

    ITrackingRepository collection;
    TextStatisticsHelper helper;
    UUID trackingId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_text, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        collection = new StaticInMemoryRepository(getActivity().getApplicationContext()).getInstance();
        trackingId = UUID.fromString(getActivity().getIntent().getStringExtra("id"));
        helper = new TextStatisticsHelper(collection.GetTracking(trackingId));

    }
}
