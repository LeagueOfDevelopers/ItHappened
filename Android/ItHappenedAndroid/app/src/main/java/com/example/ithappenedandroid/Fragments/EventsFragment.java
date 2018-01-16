package com.example.ithappenedandroid.Fragments;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ithappenedandroid.Application.TrackingService;
import com.example.ithappenedandroid.Domain.Comparison;
import com.example.ithappenedandroid.Domain.Event;
import com.example.ithappenedandroid.Domain.Rating;
import com.example.ithappenedandroid.Domain.Tracking;
import com.example.ithappenedandroid.Infrastructure.ITrackingRepository;
import com.example.ithappenedandroid.R;
import com.example.ithappenedandroid.Recyclers.EventsAdapter;
import com.example.ithappenedandroid.StaticInMemoryRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class EventsFragment extends Fragment  {

    RecyclerView eventsRecycler;
    EventsAdapter eventsAdpt;
    int myYear;
    int myMonth;
    int myDay;

    TextView hintForEventsHistory;

    RelativeLayout filtersScreen;

    Button dateFrom;
    Button dateTo;

    Button addFilters;

    TextView dateFromText;
    TextView dateToText;
    EditText scaleFilter;

    RatingBar ratingFilter;

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

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("История событий");
        hintForEventsHistory = (TextView) getActivity().findViewById(R.id.hintForEventsHistoryFragment);
        if(collection.FilterEvents(null, null, null, null, null, null, null).size()!=0){
            hintForEventsHistory.setVisibility(View.INVISIBLE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        filtersScreen = (RelativeLayout) getActivity().findViewById(R.id.bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(filtersScreen);
        behavior.setHideable(false);

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

        dateFromText = (TextView) view.findViewById(R.id.dateFrom);
        dateToText = (TextView) view.findViewById(R.id.dateTo);



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
        Comparison[] comparisons = new Comparison[] {Comparison.More, Comparison.Less, Comparison.Equal};

        hintsForScaleSpinner = (Spinner) view.findViewById(R.id.hintsForScale);
        hintsForRatingSpinner = (Spinner) view.findViewById(R.id.hintsForRating);

        ArrayAdapter<String> hintsForScaleAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, hints);
        hintsForScaleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hintsForScaleSpinner.setAdapter(hintsForScaleAdapter);

        ArrayAdapter<String> hintsForRatingAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, hints);
        hintsForRatingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hintsForRatingSpinner.setAdapter(hintsForRatingAdapter);

        scaleFilter = (EditText) getActivity().findViewById(R.id.scaleFilter);
        ratingFilter = (RatingBar) getActivity().findViewById(R.id.ratingFilter);

        addFilters = (Button) getActivity().findViewById(R.id.addFiltersButton);

        addFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UUID tracingId = null;
                Date dateFrom = null;
                Date dateTo = null;
                Comparison scaleComparison = null;
                Double scale = null;
                Comparison ratingComparison = null;
                Rating rating = null;

                if(trackingService.GetTrackingCollection().size()!=0) {
                    int position = trackingsSpinner.getSelectedItemPosition();
                    tracingId = idCollection.get(position);
                }

                if(!dateFromText.getText().toString().isEmpty() && !dateToText.getText().toString().isEmpty()){
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy 'г.' HH:mm:ss a");
                    try {
                        dateFrom = simpleDateFormat.parse(dateFromText.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    try {
                        dateTo = simpleDateFormat.parse(dateToText.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                if(!scaleFilter.getText().toString().isEmpty()){
                    int positionForScale = hintsForScaleSpinner.getSelectedItemPosition();
                    scaleComparison = comparisons[positionForScale];
                    scale = Double.parseDouble(scaleFilter.getText().toString());
                }

                if(ratingFilter.getRating()!=0){
                    int positionForRating = hintsForRatingSpinner.getSelectedItemPosition();
                    ratingComparison = comparisons[positionForRating];
                    rating = new Rating((int)ratingFilter.getRating()*2);
                }

                List<Event> filteredEvents = trackingService.FilterEventCollection(tracingId,dateFrom, dateTo, scaleComparison, scale, ratingComparison, rating);
                eventsAdpt = new EventsAdapter(filteredEvents, getActivity(), 0);
                eventsRecycler.setAdapter(eventsAdpt);

                BottomSheetBehavior behavior = BottomSheetBehavior.from(filtersScreen);
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

            }
        });



    }
}
