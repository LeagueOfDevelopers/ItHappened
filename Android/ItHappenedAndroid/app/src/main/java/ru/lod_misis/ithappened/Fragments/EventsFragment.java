package ru.lod_misis.ithappened.Fragments;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.DigitsKeyListener;
import android.text.method.KeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
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

import javax.inject.Inject;

import ru.lod_misis.ithappened.Application.TrackingService;
import ru.lod_misis.ithappened.Domain.Comparison;
import ru.lod_misis.ithappened.Domain.EventV1;
import ru.lod_misis.ithappened.Domain.Rating;
import ru.lod_misis.ithappened.Domain.TrackingV1;
import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;
import ru.lod_misis.ithappened.Presenters.EventsHistoryContract;
import ru.lod_misis.ithappened.R;
import ru.lod_misis.ithappened.Recyclers.EventsAdapter;
import ru.lod_misis.ithappened.Recyclers.PagonationScrollListener;
import ru.lod_misis.ithappened.Retrofit.ItHappenedApplication;

public class EventsFragment extends Fragment implements EventsHistoryContract.EventsHistoryView {

    RecyclerView eventsRecycler;
    EventsAdapter eventsAdpt;

    @Inject
    EventsHistoryContract.EventsHistoryPresenter eventsHistoryPresenter;

    List<EventV1> eventsForAdapter = new ArrayList<>();
    List<Boolean> selectedItems;
    ArrayList<Integer> selectedPositionItems = new ArrayList<>();

    List<String> filteredTrackingsTitles;
    ArrayList<UUID> idCollection;
    List<String> strings;
    ArrayList<UUID> allTrackingsId;
    int startPosition = 0;
    int endPosition = 10;

    boolean isScrolling = false;
    boolean isLastPage = false;
    boolean isFilteredAdded = false;

    Date dateF = null;
    Date dateT = null;
    Comparison scaleComparison = null;
    Double scale = null;
    Comparison ratingComparison = null;
    Rating rating = null;
    List<UUID> filteredTrackingsUuids;

    int stateForHint;

    Button dateFrom;
    Button dateTo;
    Button addFilters;
    EditText scaleFilter;
    RatingBar ratingFilter;
    Spinner hintsForScaleSpinner;
    Spinner hintsForRatingSpinner;
    CardView trackingsPickerBtn;
    TrackingService trackingService;
    TextView hintForEventsHistory;
    TextView hintForSpinner;
    TextView filtersHintText;
    RelativeLayout filtersScreen;
    RelativeLayout filtersHint;
    FloatingActionButton filtersCancel;
    TextView trackingsPickerText;
    ProgressBar progressBar;

    @Inject
    ITrackingRepository collection;
    private LinearLayoutManager manager;
    private boolean isFilteredCancel = false;
    List<TrackingV1> trackings;

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

        ItHappenedApplication.getAppComponent().inject(this);

        dateFrom = (Button) view.findViewById(R.id.dateFromButton);
        dateTo = (Button) view.findViewById(R.id.dateToButton);
        trackingsPickerText = getActivity().findViewById(R.id.trackingsPickerText);
        trackingsPickerBtn = getActivity().findViewById(R.id.trackingsFiltersCard);
        hintForEventsHistory = (TextView) getActivity().findViewById(R.id.hintForEventsHistoryFragment);
        filtersCancel = (FloatingActionButton) getActivity().findViewById(R.id.filtersCancel);
        eventsRecycler = (RecyclerView) view.findViewById(R.id.evetsRec);
        progressBar = (ProgressBar) view.findViewById(R.id.eventsHistoryProgressBar);

        startPosition = 0;
        endPosition = 10;

        Bundle bundle = getArguments();
        if(bundle!=null){
            dateF = new Date(bundle.getLong("dateFrom"));
            dateT = new Date(bundle.getLong("dateTo"));
            Locale locale = new Locale("ru");
            SimpleDateFormat simpleDateFormat = new
                    SimpleDateFormat("dd.MM.yyyy HH:mm", locale);
            dateFrom.setText(simpleDateFormat.format(dateF));
            dateTo.setText(simpleDateFormat.format(dateT));
        }

        YandexMetrica.reportEvent(getString(R.string.metrica_enter_events_histroy));
        eventsHistoryPresenter.onViewAttach(this);
        eventsHistoryPresenter.filterEvents(filteredTrackingsUuids,
                dateF,
                dateT,
                scaleComparison,
                scale,
                ratingComparison,
                rating,
                startPosition,
                endPosition
        );

        filtersScreen = (RelativeLayout) getActivity().findViewById(R.id.bottom_sheet);
        final BottomSheetBehavior behavior = BottomSheetBehavior.from(filtersScreen);
        behavior.setHideable(false);

        filtersHint = (RelativeLayout) getActivity().findViewById(R.id.filtersText);

        stateForHint = 0;

        filtersHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (stateForHint) {
                    case 0:
                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        stateForHint = 1;
                        break;
                    case 1:
                        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        stateForHint = 0;
                        break;
                    default:
                        Toast.makeText(getActivity(), "Потяните вверх!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        filtersHintText = getActivity().findViewById(R.id.hintForEventsHistoryFragmentFilters);

        filtersHintText.setVisibility(View.GONE);


        manager = new LinearLayoutManager(getActivity().getApplicationContext());
        eventsRecycler.setLayoutManager(manager);

        idCollection = new ArrayList<>();
        strings = new ArrayList<>();
        selectedItems = new ArrayList<>();

        trackings = new ArrayList<>();
        trackings = trackingService.GetTrackingCollection();


        String allText = eventsHistoryPresenter.prepareDataForDialog(trackings, strings, idCollection, selectedItems);;
        filteredTrackingsTitles = new ArrayList<>();
        filteredTrackingsUuids = new ArrayList<>();
        allTrackingsId = new ArrayList<>();

        setUuidsCollection(allTrackingsId);

        if (allText.length() != 0)
            trackingsPickerText.setText(allText.substring(0, allText.length() - 2));

        final String[] trackingsTitles = new String[strings.size()];
        final boolean[] selectedArray = new boolean[strings.size()];

        for (int i = 0; i < strings.size(); i++) {
            trackingsTitles[i] = strings.get(i);
            selectedArray[i] = selectedItems.get(i);
            selectedPositionItems.add(i);
        }

        if (strings.size() != 0) {
            trackingsPickerText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    filteredTrackingsUuids.clear();
                    final AlertDialog trackingsPickerDiaolg;
                    final AlertDialog.Builder trackingsPicker = new AlertDialog.Builder(getActivity());
                    trackingsPicker.setTitle("Выберите отслеживания");
                    trackingsPicker.setMultiChoiceItems(trackingsTitles, selectedArray, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {

                            if (isChecked) {
                                selectedPositionItems.add(position);
                            } else {
                                selectedPositionItems.remove((Integer.valueOf(position)));
                            }
                        }
                    });
                    trackingsPicker.setPositiveButton("Применить", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int k) {
                            String item = "";
                            for (int i = 0; i < selectedPositionItems.size(); i++) {
                                item = item + trackingsTitles[selectedPositionItems.get(i)];
                                if (i != selectedPositionItems.size() - 1) {
                                    item = item + ", ";
                                }
                            }
                            trackingsPickerText.setText(item);
                            if (item.isEmpty()) {
                                trackingsPickerText.setText("Не выбрано отслеживаний");
                            }
                        }
                    });
                    trackingsPicker.setNegativeButton("Снять все", null);
                    trackingsPicker.setNeutralButton("Выбрать все", null);

                    trackingsPickerDiaolg = trackingsPicker.show();

                    Button selectAll = trackingsPickerDiaolg.getButton(AlertDialog.BUTTON_NEUTRAL);
                    Button unselectAll = trackingsPickerDiaolg.getButton(DialogInterface.BUTTON_NEGATIVE);

                    selectAll.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            selectedPositionItems.clear();
                            for (int i = 0; i < selectedArray.length; i++) {
                                selectedArray[i] = true;
                                selectedPositionItems.add(i);
                                trackingsPickerText.setText("Выбраны все отслеживания");
                            }
                            ListView curList = trackingsPickerDiaolg.getListView();
                            for (int i = 0; i < trackingsTitles.length; ++i)
                                curList.setItemChecked(i, true);

                        }
                    });

                    unselectAll.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            for (int i = 0; i < selectedArray.length; i++) {
                                selectedArray[i] = false;
                                selectedPositionItems.clear();
                            }

                            ListView curList = trackingsPickerDiaolg.getListView();
                            for (int i = 0; i < trackingsTitles.length; ++i)
                                curList.setItemChecked(i, false);
                        }
                    });
                }
            });
        } else {
            trackingsPickerText.setText("Отслеживания отсутствуют");
            //hintForSpinner.setVisibility(View.VISIBLE);
        }



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

        String[] hints = new String[]{"Больше", "Меньше", "Равно"};
        final Comparison[] comparisons = new Comparison[]{Comparison.More, Comparison.Less, Comparison.Equal};

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
                startPosition = 0;
                endPosition = 10;
                final List<EventV1> allEvents = new ArrayList<>();
                YandexMetrica.reportEvent(getString(R.string.metrica_cancel_filters));
                dateT = null;
                dateF = null;
                filteredTrackingsUuids = null;
                scaleComparison = null;
                scale = null;
                rating = null;
                ratingComparison = null;
                isFilteredCancel = true;
                eventsHistoryPresenter.filterEvents(null,
                        dateF,
                        dateT,
                        scaleComparison,
                        scale,
                        ratingComparison,
                        rating,
                        startPosition, endPosition);
            }
        });

        addFilters = (Button) getActivity().findViewById(R.id.addFiltersButton);


        eventsRecycler.addOnScrollListener(new PagonationScrollListener(manager) {
            @Override
            protected void loadMoreItems() {
                isScrolling = true;
                startPosition = endPosition + 1;
                endPosition += 10;
                if (!isFilteredAdded)
                    eventsHistoryPresenter.filterEvents(null,
                            dateF,
                            dateT,
                            scaleComparison,
                            scale,
                            ratingComparison,
                            rating,
                            startPosition, endPosition);
                else {
                    eventsHistoryPresenter.filterEvents(filteredTrackingsUuids,
                            dateF,
                            dateT,
                            scaleComparison,
                            scale,
                            ratingComparison,
                            rating,
                            startPosition, endPosition);
                }
            }

            @Override
            public boolean isLoading() {
                return isScrolling;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }
        });

        addFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPosition = 0;
                endPosition = 10;
                isFilteredAdded = true;
                for (int i = 0; i < selectedPositionItems.size(); i++) {
                    filteredTrackingsUuids.add
                            (allTrackingsId.get(selectedPositionItems.get(i)));
                }

                if (!dateFrom.getText().toString().isEmpty() && !dateTo.getText().toString().isEmpty()) {
                    Locale locale = new Locale("ru");
                    SimpleDateFormat simpleDateFormat = new
                            SimpleDateFormat("dd.MM.yyyy HH:mm", locale);
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
                    YandexMetrica.reportEvent(getString(R.string.metrica_add_filters));
                    eventsHistoryPresenter.filterEvents(filteredTrackingsUuids,
                            dateF,
                            dateT,
                            scaleComparison,
                            scale,
                            ratingComparison,
                            rating, startPosition, endPosition);
                } catch (NumberFormatException e) {
                    Toast.makeText(getActivity().getApplicationContext(), "Введите нормальные данные шкалы!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void showEvents(List<EventV1> events) {
        isScrolling = false;
        if (isFilteredAdded || isFilteredCancel) {
            eventsForAdapter.clear();
            isFilteredAdded = false;
        }
        eventsForAdapter.addAll(events);
        if (events.size() == 0) {
            isLastPage = true;
        }
        if (eventsForAdapter.size() == 0) {
            hintForEventsHistory.setVisibility(View.VISIBLE);
            eventsAdpt = new EventsAdapter(events, getActivity(), 1);
        } else {
            hintForEventsHistory.setVisibility(View.GONE);
            eventsAdpt = new EventsAdapter(eventsForAdapter, getActivity(), 1);
            eventsAdpt.notifyDataSetChanged();
        }
        if (eventsAdpt != null) {
            List<EventV1> adapterEvents = eventsAdpt.getEventV1s();
            ArrayList<EventV1> refreshedEvents = new ArrayList<>();
            if (adapterEvents != null)
                for (EventV1 event : adapterEvents) {
                    collection.GetTracking(event.GetTrackingId());
                    EventV1 addAbleEvent = collection.GetTracking(event.GetTrackingId()).GetEvent(event.GetEventId());
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
        if (eventsAdpt != null) {
            List<EventV1> adapterEvents = eventsAdpt.getEventV1s();
            ArrayList<EventV1> refreshedEvents = new ArrayList<>();
            if (adapterEvents != null)
                for (EventV1 event : adapterEvents) {
                    collection.GetTracking(event.GetTrackingId());
                    EventV1 addAbleEvent = collection.GetTracking(event.GetTrackingId()).GetEvent(event.GetEventId());
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
        YandexMetrica.reportEvent(getString(R.string.metrica_exit_event_history));
    }

    @Override
    public void showLoading(boolean isLoading) {
        if(isLoading){
            eventsRecycler.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }else{
            eventsRecycler.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
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
            trackingsPickerText.setText(allText);
            filteredTrackingsUuids.clear();
            selectedPositionItems.clear();
            filtersHintText.setVisibility(View.GONE);
        } else {
            hintForSpinner.setVisibility(View.VISIBLE);
            filtersHintText.setVisibility(View.GONE);
        }
    }

    private void setUuidsCollection(List<UUID> uuids) {
        for (int i = 0; i < trackingService.GetTrackingCollection().size(); i++) {
            if (!trackingService.GetTrackingCollection().get(i).GetStatus()) {
                uuids.add(trackingService.GetTrackingCollection().get(i).GetTrackingID());
            }
        }
    }
}