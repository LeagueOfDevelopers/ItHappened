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

import com.yandex.metrica.YandexMetrica;

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
import ru.lod_misis.ithappened.Presenters.EventsHistoryContract;
import ru.lod_misis.ithappened.Presenters.EventsHistoryPresenterImpl;
import ru.lod_misis.ithappened.R;
import ru.lod_misis.ithappened.Recyclers.EventsAdapter;
import ru.lod_misis.ithappened.StaticInMemoryRepository;

public class EventsFragment extends Fragment implements EventsHistoryContract.EventsHistoryView {

    RecyclerView eventsRecycler;
    EventsAdapter eventsAdpt;

    EventsHistoryContract.EventsHistoryPresenter eventsHistoryPresenter;

    List<Event> eventsForAdapter = new ArrayList<>();
    List<Boolean> flags;

    List<String> filteredTrackingsTitles;
    List<UUID> filteredTrackingsUuids;
    ArrayList<UUID> idCollection;
    List<String> strings;

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
    TextView hintForEventsHistory;
    TextView hintForSpinner;
    TextView filtersHintText;
    RelativeLayout filtersScreen;
    RelativeLayout filtersHint;
    FloatingActionButton filtersCancel;

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

        YandexMetrica.reportEvent("Пользователь зашел в историю событий");
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MAIN_KEYS", Context.MODE_PRIVATE);

        if(sharedPreferences.getString("LastId","").isEmpty()) {
            StaticInMemoryRepository.setUserId(sharedPreferences.getString("UserId", ""));
            collection = StaticInMemoryRepository.getInstance();
        }else{
            StaticInMemoryRepository.setUserId(sharedPreferences.getString("LastId", ""));
            collection = StaticInMemoryRepository.getInstance();
        }

        trackingService = new TrackingService(sharedPreferences.getString("UserId", ""), collection);
        eventsHistoryPresenter = new EventsHistoryPresenterImpl(collection, trackingService, getActivity(), this);

        hintForEventsHistory = (TextView) getActivity().findViewById(R.id.hintForEventsHistoryFragment);
        filtersCancel = (FloatingActionButton) getActivity().findViewById(R.id.filtersCancel);
        eventsHistoryPresenter.loadEvents();

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

        idCollection = new ArrayList<UUID>();
        strings = new ArrayList<String>();
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

        filteredTrackingsTitles = new ArrayList<>();
        filteredTrackingsUuids = new ArrayList<>();

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

                final List<Event> allEvents = new ArrayList<>();
                YandexMetrica.reportEvent("Пользователь отменил фильтры");
                eventsHistoryPresenter.loadEvents();
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

                if (!dateFrom.getText().toString().isEmpty() && !dateTo.getText().toString().isEmpty()) {
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
                        double ratingVal = ratingFilter.getRating() * 2;
                        rating = new Rating((int) ratingVal);
                    }
                    YandexMetrica.reportEvent("Пользователь добавил фильтры");
                    eventsHistoryPresenter.filterEvents(filteredTrackingsUuids,
                            dateF,
                            dateT,
                            scaleComparison,
                            scale,
                            ratingComparison,
                            rating);
                } catch (NumberFormatException e) {
                    Toast.makeText(getActivity().getApplicationContext(), "Введите нормальные данные шкалы!", Toast.LENGTH_SHORT).show();
                }
            }});
    }

    @Override
    public void showEvents(List<Event> events) {
        if(events.size()==0){
            hintForEventsHistory.setVisibility(View.VISIBLE);
            eventsAdpt = new EventsAdapter(events, getActivity(), 1);
        }else{
            hintForEventsHistory.setVisibility(View.GONE);
            eventsAdpt = new EventsAdapter(events, getActivity(), 1);
        }
        if(eventsAdpt!=null) {
            List<Event> adapterEvents = eventsAdpt.getEvents();
            ArrayList<Event> refreshedEvents = new ArrayList<>();
            if (adapterEvents != null)
                for (Event event : adapterEvents) {
                    collection.GetTracking(event.GetTrackingId());
                    Event addAbleEvent = collection.GetTracking(event.GetTrackingId()).GetEvent(event.GetEventId());
                    if (!addAbleEvent.GetStatus())
                        refreshedEvents.add(addAbleEvent);
                }
            if (refreshedEvents.size() == 0) {
                hintForEventsHistory.setVisibility(View.VISIBLE);
            }
            eventsRecycler.setAdapter(new EventsAdapter(refreshedEvents, getActivity().getApplicationContext(), 1));
        }
        BottomSheetBehavior behavior = BottomSheetBehavior.from(filtersScreen);
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(eventsAdpt!=null) {
            List<Event> adapterEvents = eventsAdpt.getEvents();
            ArrayList<Event> refreshedEvents = new ArrayList<>();
            if (adapterEvents != null)
                for (Event event : adapterEvents) {
                    collection.GetTracking(event.GetTrackingId());
                    Event addAbleEvent = collection.GetTracking(event.GetTrackingId()).GetEvent(event.GetEventId());
                    if (!addAbleEvent.GetStatus())
                        refreshedEvents.add(addAbleEvent);
                }
            if (refreshedEvents.size() == 0) {
                hintForEventsHistory.setVisibility(View.VISIBLE);
            }
            eventsRecycler.setAdapter(new EventsAdapter(refreshedEvents, getActivity().getApplicationContext(), 1));
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        YandexMetrica.reportEvent("Пользователь вышел из истории");
    }

    @Override
    public void cancelFilters() {
        ratingFilter.setRating(0);
        scaleFilter.setText("");
        dateFrom.setText("После");
        dateFrom.setTextSize(15);
        dateTo.setText("До");
        dateTo.setTextSize(15);
        hintsForRatingSpinner.setSelection(0);
        hintsForScaleSpinner.setSelection(0);

        String allText = "";
        for (int i = 0; i < strings.size(); i++) {
            if (i != strings.size()) {
                allText += strings.get(i) + ", ";
            }
        }

        if (strings.size() != 0) {
            trackingsSpinner.setItems(strings, allText.substring(0, allText.length() - 2),
                    new MultiSpinner.MultiSpinnerListener() {

                        @Override
                        public void onItemsSelected(boolean[] selected) {
                            for (int i = 0; i < selected.length; i++) {

                                Log.e("FILTER", selected[i] + "");
                                if (selected[i]) {
                                    filteredTrackingsTitles.add(strings.get(i));
                                    if (flags.get(i)) {
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
            filtersHintText.setVisibility(View.GONE);
        } else {
            trackingsSpinner.setVisibility(View.INVISIBLE);
            hintForSpinner.setVisibility(View.VISIBLE);
            filtersHintText.setVisibility(View.GONE);
        }
    }

    private void setUuidsCollection(List<UUID> uuids){
        for(int i =0; i<trackingService.GetTrackingCollection().size();i++){
            if(!trackingService.GetTrackingCollection().get(i).GetStatus()){
                uuids.add(trackingService.GetTrackingCollection().get(i).GetTrackingID());
            }
        }
    }
    }


