package com.example.ithappenedandroid.Fragments;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.ithappenedandroid.Application.TrackingService;
import com.example.ithappenedandroid.Domain.Tracking;
import com.example.ithappenedandroid.Infrastructure.ITrackingRepository;
import com.example.ithappenedandroid.R;
import com.example.ithappenedandroid.Recyclers.EventsAdapter;
import com.example.ithappenedandroid.StaticInMemoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EventsFragment extends Fragment  {

    int DIALOG_DATE_FROM = 1;
    int DIALOG_DATE_TO = 2;

    RecyclerView eventsRecycler;
    EventsAdapter eventsAdpt;
    int myYear;
    int myMonth;
    int myDay;

    Button dateFrom;
    Button dateTo;

    EditText dateFromText;
    EditText dateToText;

    Spinner trackingsSpinner;
    Spinner hintsForScaleSpinner;
    Spinner hintsForRatingSpinner;
    TrackingService trackingService;

    ITrackingRepository collection = StaticInMemoryRepository.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_events_history, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        eventsRecycler = (RecyclerView) view.findViewById(R.id.evetsRec);
        eventsRecycler.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        eventsAdpt = new EventsAdapter(collection.FilterEvents(null,null,null,null,null,null,null), getActivity(), 0);
        eventsRecycler.setAdapter(eventsAdpt);

        trackingService = new TrackingService("testUser", collection);

        ArrayList<UUID> idCollection = new ArrayList<UUID>();
        ArrayList<String> strings = new ArrayList<String>();

        List<Tracking> trackings = new ArrayList<>();
        trackings = trackingService.GetTrackingCollection();

        for(int i=0;i<trackings.size();i++){
            strings.add(trackings.get(i).GetTrackingName());
            idCollection.add(trackings.get(i).GetTrackingID());
        }

        trackingsSpinner = (Spinner) view.findViewById(R.id.spinnerForTrackings);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, strings);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        trackingsSpinner.setAdapter(adapter);

        dateFrom = (Button) view.findViewById(R.id.dateFromButton);
        dateTo = (Button) view.findViewById(R.id.dateToButton);

        dateFromText = (EditText) view.findViewById(R.id.dateFrom);
        dateToText = (EditText) view.findViewById(R.id.dateTo);



        dateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                DialogFragment picker = new DatePickerFragment(dateFromText);
                picker.show(fragmentManager, "from");
            }
        });



        dateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                DialogFragment picker = new DatePickerFragment(dateToText);
                picker.show(fragmentManager, "to");
            }
        });

        String[] hints = new String[]{">","<","="};

        hintsForScaleSpinner = (Spinner) view.findViewById(R.id.hintsForScale);
        hintsForRatingSpinner = (Spinner) view.findViewById(R.id.hintsForRating);

        ArrayAdapter<String> hintsForScaleAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, hints);
        hintsForScaleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hintsForScaleSpinner.setAdapter(hintsForScaleAdapter);

        ArrayAdapter<String> hintsForRatingAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, hints);
        hintsForRatingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hintsForRatingSpinner.setAdapter(hintsForRatingAdapter);

    }
}
