package ru.lod_misis.ithappened.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ru.lod_misis.ithappened.Activities.AddNewTrackingActivity;
import ru.lod_misis.ithappened.Application.TrackingService;
import ru.lod_misis.ithappened.Domain.Tracking;
import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;
import ru.lod_misis.ithappened.Presenters.TrackingsContract;
import ru.lod_misis.ithappened.Presenters.TrackingsPresenterImpl;
import ru.lod_misis.ithappened.R;
import ru.lod_misis.ithappened.Recyclers.TrackingsAdapter;
import ru.lod_misis.ithappened.StaticInMemoryRepository;

public class TrackingsFragment extends Fragment implements TrackingsContract.TrackingsView {

    TextView hintForTrackings;
    RecyclerView trackingsRecycler;
    TrackingsAdapter trackAdpt;
    FloatingActionButton addTracking;
    ITrackingRepository trackingCollection;
    TrackingService trackingService;
    TrackingsContract.TrackingsPresenter trackingsPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trackings, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Что произошло?");
        hintForTrackings = (TextView) getActivity().findViewById(R.id.hintForTrackingsFragment);


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MAIN_KEYS", Context.MODE_PRIVATE);
        if(sharedPreferences.getString("LastId","").isEmpty()) {
            trackingCollection = StaticInMemoryRepository.getInstance();
        }else{
            trackingCollection = StaticInMemoryRepository.getInstance();
        }
        trackingService = new TrackingService(sharedPreferences.getString("UserId", ""), trackingCollection);

        trackingsPresenter = new TrackingsPresenterImpl(
                trackingService,
                getActivity(),
                this,
                trackingCollection,
                sharedPreferences);

        trackingsRecycler = (RecyclerView)getActivity().findViewById(R.id.tracingsRV);
        trackingsRecycler.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        trackingsPresenter.loadTrackings();

        addTracking = (FloatingActionButton) getActivity().findViewById(R.id.addNewTracking);
        addTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), AddNewTrackingActivity.class);
                startActivity(intent);

            }
        });
    }

    @Override
    public void showTrackings(List<Tracking> visibleTrackings) {

        if(visibleTrackings.size()!=0){
            hintForTrackings.setVisibility(View.INVISIBLE);
        }
        trackAdpt = new TrackingsAdapter(visibleTrackings,getActivity(), trackingsPresenter);
        trackingsRecycler.setAdapter(trackAdpt);

    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}