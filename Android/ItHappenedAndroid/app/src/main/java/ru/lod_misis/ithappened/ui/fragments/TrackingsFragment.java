package ru.lod_misis.ithappened.ui.fragments;

import android.app.Fragment;
import android.content.Intent;
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

import javax.inject.Inject;

import ru.lod_misis.ithappened.ui.activities.AddNewTrackingActivity;
import ru.lod_misis.ithappened.domain.models.TrackingV1;
import ru.lod_misis.ithappened.ui.presenters.TrackingsContract;
import ru.lod_misis.ithappened.R;
import ru.lod_misis.ithappened.ui.recyclers.TrackingsAdapter;
import ru.lod_misis.ithappened.ui.ItHappenedApplication;

public class TrackingsFragment extends Fragment implements TrackingsContract.TrackingsView {

    TextView hintForTrackings;
    RecyclerView trackingsRecycler;
    TrackingsAdapter trackAdpt;
    FloatingActionButton addTracking;
    @Inject
    TrackingsContract.TrackingsPresenter trackingsPresenter;

    @Nullable
    @Override
    public View onCreateView ( LayoutInflater inflater , @Nullable ViewGroup container , Bundle savedInstanceState ) {
        return inflater.inflate ( R.layout.fragment_trackings , null );
    }

    @Override
    public void onActivityCreated ( @Nullable Bundle savedInstanceState ) {
        super.onActivityCreated ( savedInstanceState );
    }

    @Override
    public void onResume () {
        super.onResume ();
        ItHappenedApplication.getAppComponent ().inject ( this );
        getActivity ().setTitle ( "Что произошло?" );
        hintForTrackings = ( TextView ) getActivity ().findViewById ( R.id.hintForTrackingsFragment );
        trackingsPresenter.onViewAttach ( this );
        trackingsRecycler = ( RecyclerView ) getActivity ().findViewById ( R.id.tracingsRV );
        trackingsRecycler.setLayoutManager ( new StaggeredGridLayoutManager ( 2 , StaggeredGridLayoutManager.VERTICAL ) );

        trackingsPresenter.loadTrackings ();

        addTracking = ( FloatingActionButton ) getActivity ().findViewById ( R.id.addNewTracking );
        addTracking.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick ( View view ) {

                Intent intent = new Intent ( getActivity () , AddNewTrackingActivity.class );
                startActivity ( intent );

            }
        } );
    }

    @Override
    public void showTrackings ( List <TrackingV1> visibleTrackingV1s ) {

        if ( visibleTrackingV1s.size () != 0 ) {
            hintForTrackings.setVisibility ( View.INVISIBLE );
        }
        trackAdpt = new TrackingsAdapter ( visibleTrackingV1s , getActivity () , trackingsPresenter );
        trackingsRecycler.setAdapter ( trackAdpt );

    }

    @Override
    public void showMessage ( String message ) {
        Toast.makeText ( getActivity ().getApplicationContext () , message , Toast.LENGTH_SHORT );
    }

    @Override
    public void onViewCreated ( View view , @Nullable Bundle savedInstanceState ) {
        super.onViewCreated ( view , savedInstanceState );


    }
}