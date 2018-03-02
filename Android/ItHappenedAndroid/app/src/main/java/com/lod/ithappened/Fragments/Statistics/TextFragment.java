package com.lod.ithappened.Fragments.Statistics;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lod.ithappened.Domain.Event;
import com.lod.ithappened.Domain.Tracking;
import com.lod.ithappened.Infrastructure.ITrackingRepository;
import com.lod.ithappened.R;
import com.lod.ithappened.StaticInMemoryRepository;
import com.lod.ithappened.StatisticsHelpers.TextStatisticsHelper;

import java.util.UUID;

/**
 * Created by Пользователь on 20.02.2018.
 */

public class TextFragment extends Fragment {

    ITrackingRepository collection;
    TextStatisticsHelper helper;
    UUID trackingId;

    TextView eventsCount;
    TextView avrgScale;
    TextView avrgRating;
    TextView sumScale;
    TextView sumRating;

    TextView hint;
    RelativeLayout visibility;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_text, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        //initWidgets();
        eventsCount = (TextView) getActivity().findViewById(R.id.eventsCount);
        avrgScale = (TextView) getActivity().findViewById(R.id.avrgScale);
        avrgRating = (TextView) getActivity().findViewById(R.id.avrgRating);
        sumScale = (TextView) getActivity().findViewById(R.id.sumScale);
        sumRating = (TextView) getActivity().findViewById(R.id.sumRating);
        hint = (TextView) getActivity().findViewById(R.id.hintForTextFragment);
        visibility = (RelativeLayout) getActivity().findViewById(R.id.visibilityText);

        visibility.setVisibility(View.INVISIBLE);
        hint.setVisibility(View.VISIBLE);

        collection = new StaticInMemoryRepository(getActivity().getApplicationContext()).getInstance();
        trackingId = UUID.fromString(getActivity().getIntent().getStringExtra("id"));
        Tracking tracking = collection.GetTracking(trackingId);

        for(Event event : tracking.GetEventCollection()){
            if(!event.GetStatus()){
                hint.setVisibility(View.INVISIBLE);
                visibility.setVisibility(View.VISIBLE);
            }
        }

        helper = new TextStatisticsHelper(tracking);
        eventsCount.setText(helper.getEventsCount().toString());

        if(helper.getAvrgScale()==null){
            avrgScale.setText("отсутствует");
        }else{
            avrgScale.setText(helper.getAvrgScale().toString());
        }

        if(helper.getAvrgRating()==null){
            avrgRating.setText("отсутствует");
        }else{
            avrgRating.setText(helper.getAvrgRating().toString());
        }

        if(helper.getRatingSum()==null){
            sumRating.setText("отсутствует");
        }else{
            sumRating.setText(helper.getRatingSum().toString());
        }

        if(helper.getScaleSum()==null){
            sumScale.setText("отсутствует");
        }else{
            sumScale.setText(helper.getScaleSum().toString());
        }
    }

    private void initWidgets(){
        eventsCount = (TextView) getActivity().findViewById(R.id.eventsCount);
        avrgScale = (TextView) getActivity().findViewById(R.id.avrgScale);
        avrgRating = (TextView) getActivity().findViewById(R.id.avrgRating);
        sumScale = (TextView) getActivity().findViewById(R.id.sumScale);
        sumRating = (TextView) getActivity().findViewById(R.id.sumRating);
    }
}
