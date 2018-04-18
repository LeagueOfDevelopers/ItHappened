package ru.lod_misis.ithappened.Fragments;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.DigitsKeyListener;
import android.text.method.KeyListener;
import android.util.Log;
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
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import ru.lod_misis.ithappened.Application.TrackingService;
import ru.lod_misis.ithappened.Domain.Comparison;
import ru.lod_misis.ithappened.Domain.Event;
import ru.lod_misis.ithappened.Domain.Rating;
import ru.lod_misis.ithappened.Domain.Tracking;
import ru.lod_misis.ithappened.Gui.MultiSpinner;
import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;
import ru.lod_misis.ithappened.R;
import ru.lod_misis.ithappened.Recyclers.EventsAdapter;
import ru.lod_misis.ithappened.StaticInMemoryRepository;

public class EventsFragment extends Fragment  {

    static RecyclerView eventsRecycler;
    static EventsAdapter eventsAdpt;
    int myYear;
    int myMonth;
    int myDay;

    List<Boolean> flags;

    TextView hintForEventsHistory;
    TextView hintForSpinner;

    TextView filtersHintText;

    RelativeLayout filtersScreen;
    RelativeLayout filtersHint;

    FloatingActionButton filtersCancel;

    int stateForHint;

    Button dateFrom;
    Button dateTo;

    Button addFilters;

    EditText scaleFilter;

    RatingBar ratingFilter;

    MultiSpinner trackingsSpinner;
    Spinner hintsForScaleSpinner;
    Spinner hintsForRatingSpinner;
    TrackingService trackingService;

    static ITrackingRepository collection;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_events_history, null);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("История событий");

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MAIN_KEYS", Context.MODE_PRIVATE);

        collection = new StaticInMemoryRepository(getActivity().getApplicationContext(), sharedPreferences.getString("UserId", "")).getInstance();

        trackingService = new TrackingService(sharedPreferences.getString("UserId", ""), collection);

        hintForEventsHistory = (TextView) getActivity().findViewById(R.id.hintForEventsHistoryFragment);
        if(trackingService.FilterEventCollection(null, null, null, null, null, null, null).size()!=0) {
            hintForEventsHistory.setVisibility(View.INVISIBLE);
        }
        filtersCancel = (FloatingActionButton) getActivity().findViewById(R.id.filtersCancel);



        filtersScreen = (RelativeLayout) getActivity().findViewById(R.id.bottom_sheet);
        final BottomSheetBehavior behavior = BottomSheetBehavior.from(filtersScreen);
        behavior.setHideable(false);

        filtersHint = (RelativeLayout) getActivity().findViewById(R.id.filtersText);

        stateForHint = 0;

        filtersHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (stateForHint){
                    case 0:
                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        stateForHint = 1;
                        break;
                    case 1:
                        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        stateForHint = 0;
                        break;
                    default:
                        Toast.makeText(getActivity(), "Потяните вверх!",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        hintForSpinner = (TextView) getActivity().findViewById(R.id.hintsForSpinner);
        filtersHintText = getActivity().findViewById(R.id.hintForEventsHistoryFragmentFilters);

        filtersHintText.setVisibility(View.GONE);

        eventsRecycler = (RecyclerView) view.findViewById(R.id.evetsRec);
        eventsRecycler.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        eventsAdpt = new EventsAdapter(trackingService.FilterEventCollection
                (null,null,null,
                        null,null,null,
                        null), getActivity(), 1);
        eventsRecycler.setAdapter(eventsAdpt);


        final ArrayList<UUID> idCollection = new ArrayList<UUID>();
        final List<String> strings = new ArrayList<String>();
        flags = new ArrayList<>();

        List<Tracking> trackings = new ArrayList<>();
        trackings = trackingService.GetTrackingCollection();

        for(int i=0;i<trackings.size();i++){
            if(!trackings.get(i).GetStatus()) {
                strings.add(trackings.get(i).GetTrackingName());
                idCollection.add(trackings.get(i).GetTrackingID());
                flags.add(false);
            }
        }


        final List<String> filteredTrackingsTitles = new ArrayList<>();
        final List<UUID> filteredTrackingsUuids = new ArrayList<>();

        setUuidsCollection(filteredTrackingsUuids);

        trackingsSpinner = (MultiSpinner) view.findViewById(R.id.spinnerForTrackings);

        String allText = "";
        for(int i=0;i<strings.size();i++) {
            if (i != strings.size()) {
                allText += strings.get(i) + ", ";
            }
        }


        if(strings.size()!=0) {
            trackingsSpinner.setItems(strings, allText.substring(0, allText.length() - 2), new MultiSpinner.MultiSpinnerListener() {

                @Override
                public void onItemsSelected(boolean[] selected) {

                    for (int i = 0; i < selected.length; i++) {

                        Log.e("FILTER", selected[i] + "");
                        if (selected[i]) {
                            if(flags.get(i)) {
                                filteredTrackingsUuids.add(idCollection.get(i));
                            }
                        }
                        if (!selected[i]) {
                            filteredTrackingsUuids.remove(idCollection.get(i));
                            flags.set(i, true);
                        }

                    }
                }
            });
        }else{

            trackingsSpinner.setVisibility(View.INVISIBLE);
            hintForSpinner.setVisibility(View.VISIBLE);

        }

        dateFrom = (Button) view.findViewById(R.id.dateFromButton);
        dateTo = (Button) view.findViewById(R.id.dateToButton);


        dateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                DialogFragment picker = new DatePickerFragment(dateFrom);
                picker.show(fragmentManager, "from");
            }
        });



        dateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                DialogFragment picker = new DatePickerFragment(dateTo);
                picker.show(fragmentManager, "to");
            }
        });

        String[] hints = new String[]{"Больше","Меньше","Равно"};
        final Comparison[] comparisons = new Comparison[] {Comparison.More, Comparison.Less, Comparison.Equal};

        hintsForScaleSpinner = (Spinner) view.findViewById(R.id.hintsForScale);
        hintsForRatingSpinner = (Spinner) view.findViewById(R.id.hintsForRating);

        ArrayAdapter<String> hintsForScaleAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, hints);
        hintsForScaleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hintsForScaleSpinner.setAdapter(hintsForScaleAdapter);

        final ArrayAdapter<String> hintsForRatingAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, hints);
        hintsForRatingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hintsForRatingSpinner.setAdapter(hintsForRatingAdapter);


        KeyListener keyListener = DigitsKeyListener.getInstance("-1234567890.");
        scaleFilter = (EditText) getActivity().findViewById(R.id.scaleFilter);
        scaleFilter.setKeyListener(keyListener);
        ratingFilter = (RatingBar) getActivity().findViewById(R.id.ratingFilter);


        filtersCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventsAdpt = new EventsAdapter(trackingService.FilterEventCollection
                        (null,null,null,
                                null,null,null,
                                null), getActivity(), 1);
                ratingFilter.setRating(0);
                scaleFilter.setText("");
                dateFrom.setText("До");
                dateTo.setText("После");

                hintsForRatingSpinner.setSelection(0);
                hintsForScaleSpinner.setSelection(0);

                String allText = "";
                for(int i=0;i<strings.size();i++) {
                    if (i != strings.size()) {
                        allText += strings.get(i) + ", ";
                    }
                }

                if(strings.size()!=0) {
                    trackingsSpinner.setItems(strings, allText.substring(0, allText.length() - 2),
                            new MultiSpinner.MultiSpinnerListener() {

                                @Override
                                public void onItemsSelected(boolean[] selected) {

                                    for (int i = 0; i < selected.length; i++) {

                                        Log.e("FILTER", selected[i] + "");
                                        if (selected[i]) {
                                            filteredTrackingsTitles.add(strings.get(i));
                                            if(flags.get(i)) {
                                                filteredTrackingsUuids.add(idCollection.get(i));
                                            }
                                        }
                                        if (!selected[i]) {
                                            filteredTrackingsUuids.remove(idCollection.get(i));
                                            flags.set(i, true);
                                        }

                                    }
                                }
                            });
                }else{

                    trackingsSpinner.setVisibility(View.INVISIBLE);
                    hintForSpinner.setVisibility(View.VISIBLE);

                    filtersHintText.setVisibility(View.GONE);

                }

                eventsRecycler.setAdapter(eventsAdpt);
            }
        });


        addFilters = (Button) getActivity().findViewById(R.id.addFiltersButton);

        addFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Date dateF = null;
                Date dateT = null;
                Comparison scaleComparison = null;
                Double scale = null;
                Comparison ratingComparison = null;
                Rating rating = null;

                if(!dateFrom.getText().toString().isEmpty() && !dateTo.getText().toString().isEmpty()){
                    Locale locale = new Locale("ru");
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", locale);
                    try {
                        dateF = simpleDateFormat.parse(dateFrom.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    try {
                        dateT = simpleDateFormat.parse(dateTo.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                try {

                    if (!scaleFilter.getText().toString().isEmpty()) {
                        int positionForScale = hintsForScaleSpinner.getSelectedItemPosition();
                        scaleComparison = comparisons[positionForScale];
                        scale = Double.parseDouble(scaleFilter.getText().toString());
                    }

                    if (ratingFilter.getRating() != 0) {
                        int positionForRating = hintsForRatingSpinner.getSelectedItemPosition();
                        ratingComparison = comparisons[positionForRating];
                        rating = new Rating((int) ratingFilter.getRating() * 2);
                    }

                    List<Event> filteredEvents = trackingService.FilterEventCollection(filteredTrackingsUuids, dateF, dateT, scaleComparison, scale, ratingComparison, rating);
                    eventsAdpt = new EventsAdapter(filteredEvents, getActivity(), 1);
                    eventsRecycler.setAdapter(eventsAdpt);

                    if(filteredEvents.size()==0){
                        filtersHintText.setVisibility(View.VISIBLE);
                    }else{
                        filtersHintText.setVisibility(View.GONE);
                    }
                    BottomSheetBehavior behavior = BottomSheetBehavior.from(filtersScreen);
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }catch (Exception e){
                    Toast.makeText(getActivity().getApplicationContext(), "Введите нормальные данные шкалы!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        List<Event> adapterEvents = eventsAdpt.getEvents();
        List<Event> refreshedEvents = new ArrayList<>();
        for(int i=0;i<adapterEvents.size();i++){
            Tracking tracking = collection.GetTracking(adapterEvents.get(i).GetTrackingId());
            refreshedEvents.add(tracking.GetEvent(adapterEvents.get(i).GetEventId()));
        }
        eventsAdpt.refreshData(refreshedEvents);
    }





    private void setUuidsCollection(List<UUID> uuids){
        for(int i =0; i<trackingService.GetTrackingCollection().size();i++){
            if(!trackingService.GetTrackingCollection().get(i).GetStatus()){
                uuids.add(trackingService.GetTrackingCollection().get(i).GetTrackingID());
            }
        }
    }


    }


