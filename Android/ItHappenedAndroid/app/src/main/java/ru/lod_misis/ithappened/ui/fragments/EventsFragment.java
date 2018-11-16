package ru.lod_misis.ithappened.ui.fragments;

import android.app.Activity;
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

import ru.lod_misis.ithappened.R;
import ru.lod_misis.ithappened.data.repository.TrackingDataSource;
import ru.lod_misis.ithappened.domain.models.Comparison;
import ru.lod_misis.ithappened.domain.models.EventV1;
import ru.lod_misis.ithappened.domain.models.Rating;
import ru.lod_misis.ithappened.ui.ItHappenedApplication;
import ru.lod_misis.ithappened.ui.presenters.EventsHistoryContract;
import ru.lod_misis.ithappened.ui.recyclers.EventsAdapter;

public class EventsFragment extends Fragment implements EventsHistoryContract.EventsHistoryView {

    RecyclerView eventsRecycler;
    EventsAdapter eventsAdpt;

    @Inject
    EventsHistoryContract.EventsHistoryPresenter eventsHistoryPresenter;
    @Inject
    TrackingDataSource collection;

    List<EventV1> eventsForAdapter = new ArrayList<>();
    List<Boolean> selectedItems = new ArrayList<>();
    List<Integer> selectedPositionItems = new ArrayList<>();

    List<UUID> idCollection = new ArrayList<>();
    List<String> strings = new ArrayList<>();
    List<UUID> allTrackingsId;
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
    List<UUID> filteredTrackingsUuids = new ArrayList<>();

    int stateForHint;

    Button dateFrom;
    Button dateTo;
    Button addFilters;
    EditText scaleFilter;
    RatingBar ratingFilter;
    Spinner hintsForScaleSpinner;
    Spinner hintsForRatingSpinner;
    CardView trackingsPickerBtn;
    TextView hintForEventsHistory;
    TextView hintForSpinner;
    TextView filtersHintText;
    RelativeLayout filtersScreen;
    RelativeLayout filtersHint;
    FloatingActionButton filtersCancel;
    TextView trackingsPickerText;
    ProgressBar progressBar;
    private boolean isFilteredCancel = false;

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater , @Nullable ViewGroup container , Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_events_history , null);
    }

    @Override
    public void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated (View view , @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view , savedInstanceState);
        getActivity().setTitle("История событий");

        ItHappenedApplication.getAppComponent().inject(this);

        initViews(view , getActivity());

        startPosition = 0;
        endPosition = 10;

        getDataFromBundle();

        YandexMetrica.reportEvent(getString(R.string.metrica_enter_events_histroy));
        eventsHistoryPresenter.onViewAttach(this);
        eventsHistoryPresenter.filterEvents(filteredTrackingsUuids ,
                dateF ,
                dateT ,
                scaleComparison ,
                scale ,
                ratingComparison ,
                rating ,
                startPosition ,
                endPosition
        );


        final BottomSheetBehavior behavior = BottomSheetBehavior.from(filtersScreen);
        behavior.setHideable(false);

        stateForHint = 0;

        filtersHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                switch ( stateForHint ) {
                    case 0:
                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        stateForHint = 1;
                        break;
                    case 1:
                        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        stateForHint = 0;
                        break;
                    default:
                        Toast.makeText(getActivity() , "Потяните вверх!" , Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        filtersHintText = getActivity().findViewById(R.id.hintForEventsHistoryFragmentFilters);

        filtersHintText.setVisibility(View.GONE);


        LinearLayoutManager manager = new LinearLayoutManager(getActivity().getApplicationContext());
        eventsRecycler.setLayoutManager(manager);


        String allText = eventsHistoryPresenter.prepareDataForDialog(strings , idCollection , selectedItems);
        filteredTrackingsUuids = new ArrayList<>();
        allTrackingsId = eventsHistoryPresenter.setUuidsCollection();


        if (allText.length() != 0)
            trackingsPickerText.setText(allText.substring(0 , allText.length() - 2));

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
                public void onClick (View view) {
                    filteredTrackingsUuids.clear();
                    final AlertDialog trackingsPickerDiaolg;
                    final AlertDialog.Builder trackingsPicker = new AlertDialog.Builder(getActivity());
                    trackingsPicker.setTitle("Выберите отслеживания");
                    trackingsPicker.setMultiChoiceItems(trackingsTitles , selectedArray , new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick (DialogInterface dialogInterface , int position , boolean isChecked) {

                            if (isChecked) {
                                selectedPositionItems.add(position);
                            } else {
                                selectedPositionItems.remove((Integer.valueOf(position)));
                            }
                        }
                    });
                    trackingsPicker.setPositiveButton("Применить" , new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick (DialogInterface dialogInterface , int k) {
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
                    trackingsPicker.setNegativeButton("Снять все" , null);
                    trackingsPicker.setNeutralButton("Выбрать все" , null);

                    trackingsPickerDiaolg = trackingsPicker.show();

                    Button selectAll = trackingsPickerDiaolg.getButton(AlertDialog.BUTTON_NEUTRAL);
                    Button unselectAll = trackingsPickerDiaolg.getButton(DialogInterface.BUTTON_NEGATIVE);

                    selectAll.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick (View view) {
                            selectedPositionItems.clear();
                            for (int i = 0; i < selectedArray.length; i++) {
                                selectedArray[i] = true;
                                selectedPositionItems.add(i);
                                trackingsPickerText.setText("Выбраны все отслеживания");
                            }
                            ListView curList = trackingsPickerDiaolg.getListView();
                            for (int i = 0; i < trackingsTitles.length; ++i)
                                curList.setItemChecked(i , true);

                        }
                    });

                    unselectAll.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick (View view) {
                            for (int i = 0; i < selectedArray.length; i++) {
                                selectedArray[i] = false;
                                selectedPositionItems.clear();
                            }

                            ListView curList = trackingsPickerDiaolg.getListView();
                            for (int i = 0; i < trackingsTitles.length; ++i)
                                curList.setItemChecked(i , false);
                        }
                    });
                }
            });
        } else {
            trackingsPickerText.setText("Отслеживания отсутствуют");
            hintForSpinner.setVisibility(View.VISIBLE);
        }


        dateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                FragmentManager fragmentManager = getFragmentManager();
                DialogFragment picker = new DatePickerFragment(dateFrom);
                picker.show(fragmentManager , "from");
            }
        });

        dateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                FragmentManager fragmentManager = getFragmentManager();
                DialogFragment picker = new DatePickerFragment(dateTo);
                picker.show(fragmentManager , "to");
            }
        });

        String[] hints = new String[]{"Больше" , "Меньше" , "Равно"};
        final Comparison[] comparisons = new Comparison[]{Comparison.More , Comparison.Less , Comparison.Equal};

        ArrayAdapter<String> hintsForScaleAdapter = new ArrayAdapter<>(getActivity() , android.R.layout.simple_spinner_item , hints);
        hintsForScaleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hintsForScaleSpinner.setAdapter(hintsForScaleAdapter);

        ArrayAdapter<String> hintsForRatingAdapter = new ArrayAdapter<>(getActivity() , android.R.layout.simple_spinner_item , hints);
        hintsForRatingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hintsForRatingSpinner.setAdapter(hintsForRatingAdapter);

        KeyListener keyListener = DigitsKeyListener.getInstance("-1234567890.");
        scaleFilter.setKeyListener(keyListener);

        filtersCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                final List<EventV1> allEvents = new ArrayList<>();
                YandexMetrica.reportEvent(getString(R.string.metrica_cancel_filters));
                isFilteredCancel = true;
                eventsHistoryPresenter.filterEvents(null ,
                        null ,
                        null ,
                        null ,
                        null ,
                        null ,
                        null ,
                        startPosition , endPosition);
            }
        });

        /*Это надо полюбому переделать!
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
        });*/

        addFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                isFilteredAdded = true;
                for (int i = 0; i < selectedPositionItems.size(); i++) {
                    filteredTrackingsUuids.add
                            (allTrackingsId.get(selectedPositionItems.get(i)));
                }

                if (!dateFrom.getText().toString().isEmpty() && !dateTo.getText().toString().isEmpty()) {
                    Locale locale = new Locale("ru");
                    SimpleDateFormat simpleDateFormat = new
                            SimpleDateFormat("dd.MM.yyyy HH:mm" , locale);
                    try {
                        dateF = simpleDateFormat.parse(dateFrom.getText().toString());
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
                        rating = new Rating(( int ) ratingVal);
                    }
                    YandexMetrica.reportEvent(getString(R.string.metrica_add_filters));
                    eventsHistoryPresenter.filterEvents(filteredTrackingsUuids ,
                            dateF ,
                            dateT ,
                            scaleComparison ,
                            scale ,
                            ratingComparison ,
                            rating , startPosition , endPosition);
                } catch (NumberFormatException e) {
                    Toast.makeText(getActivity().getApplicationContext() , "Введите нормальные данные шкалы!" , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getDataFromBundle () {
        Bundle bundle = getArguments();
        if (bundle != null) {
            dateF = new Date(bundle.getLong("dateFrom"));
            dateT = new Date(bundle.getLong("dateTo"));
            Locale locale = new Locale("ru");
            SimpleDateFormat simpleDateFormat = new
                    SimpleDateFormat("dd.MM.yyyy HH:mm" , locale);
            dateFrom.setText(simpleDateFormat.format(dateF));
            dateTo.setText(simpleDateFormat.format(dateT));
        }
    }

    private void initViews (View view , Activity activity) {
        filtersScreen = activity.findViewById(R.id.bottom_sheet);
        filtersHint = activity.findViewById(R.id.filtersText);
        dateFrom = view.findViewById(R.id.dateFromButton);
        dateTo = view.findViewById(R.id.dateToButton);
        trackingsPickerText = activity.findViewById(R.id.trackingsPickerText);
        trackingsPickerBtn = activity.findViewById(R.id.trackingsFiltersCard);
        hintForEventsHistory = activity.findViewById(R.id.hintForEventsHistoryFragment);
        filtersCancel = activity.findViewById(R.id.filtersCancel);
        eventsRecycler = view.findViewById(R.id.evetsRec);
        progressBar = view.findViewById(R.id.eventsHistoryProgressBar);
        hintsForScaleSpinner = view.findViewById(R.id.hintsForScale);
        hintsForRatingSpinner = view.findViewById(R.id.hintsForRating);
        scaleFilter = activity.findViewById(R.id.scaleFilter);
        ratingFilter = activity.findViewById(R.id.ratingFilter);
        addFilters = activity.findViewById(R.id.addFiltersButton);
    }

    //я пытался понять ,честно,но не смог,сус, я вызываю тебя!
    @Override
    public void showEvents (List<EventV1> events) {
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
            eventsAdpt = new EventsAdapter(eventsForAdapter , getActivity() , 1 , collection);
        } else {
            hintForEventsHistory.setVisibility(View.GONE);
            eventsAdpt = new EventsAdapter(eventsForAdapter , getActivity() , 1 , collection);
            eventsAdpt.notifyDataSetChanged();
        }
        if (eventsAdpt != null) {
            List<EventV1> adapterEvents = eventsAdpt.getEventV1s();
            ArrayList<EventV1> refreshedEvents = new ArrayList<>();
            if (adapterEvents != null)
                for (EventV1 event : adapterEvents) {
                    collection.getTracking(event.getTrackingId());
                    EventV1 addAbleEvent = collection.getTracking(event.getTrackingId()).getEvent(event.getEventId());
                    if (!addAbleEvent.isDeleted())
                        refreshedEvents.add(addAbleEvent);
                }
            if (refreshedEvents.size() == 0) {
                hintForEventsHistory.setVisibility(View.VISIBLE);
            }
            eventsRecycler.setAdapter(new EventsAdapter(refreshedEvents , getActivity().getApplicationContext() , 1 , collection));
        }
        BottomSheetBehavior behavior = BottomSheetBehavior.from(filtersScreen);
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }


    @Override
    public void onPause () {
        super.onPause();
        YandexMetrica.reportEvent(getString(R.string.metrica_exit_event_history));
    }

    @Override
    public void showLoading (boolean isLoading) {
        if (isLoading) {
            eventsRecycler.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            eventsRecycler.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void cancelFilters () {
        ratingFilter.setRating(0);
        scaleFilter.setText("");
        dateFrom.setText("После");
        dateFrom.setTextSize(15);
        dateTo.setText("До");
        dateTo.setTextSize(15);
        hintsForRatingSpinner.setSelection(0);
        hintsForScaleSpinner.setSelection(0);

        StringBuilder allText = new StringBuilder();
        for (int i = 0; i < strings.size(); i++) {
            strings.size();
            allText.append(strings.get(i)).append(", ");
        }

        if (strings.size() != 0) {
            trackingsPickerText.setText(allText.toString());
            filteredTrackingsUuids.clear();
            selectedPositionItems.clear();
            filtersHintText.setVisibility(View.GONE);
        } else {
            hintForSpinner.setVisibility(View.VISIBLE);
            filtersHintText.setVisibility(View.GONE);
        }
    }

}