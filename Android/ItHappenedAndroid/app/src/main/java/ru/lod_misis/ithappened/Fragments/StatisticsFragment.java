package ru.lod_misis.ithappened.Fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;
import com.yandex.metrica.YandexMetrica;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ru.lod_misis.ithappened.Activities.UserActionsActivity;
import ru.lod_misis.ithappened.Application.TrackingService;
import ru.lod_misis.ithappened.Domain.TrackingV1;
import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;
import ru.lod_misis.ithappened.Infrastructure.InMemoryFactRepository;
import ru.lod_misis.ithappened.Infrastructure.StaticFactRepository;
import ru.lod_misis.ithappened.Presenters.StatisticsContract;
import ru.lod_misis.ithappened.Presenters.StatisticsInteractorImpl;
import ru.lod_misis.ithappened.R;
import ru.lod_misis.ithappened.Recyclers.StatisticsAdapter;
import ru.lod_misis.ithappened.Retrofit.ItHappenedApplication;
import ru.lod_misis.ithappened.StaticInMemoryRepository;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;


public class StatisticsFragment extends Fragment implements StatisticsContract.StatisticsView {

    RecyclerView trackingsRecycler;
    StatisticsAdapter trackAdpt;

    ProgressBar loading;

    RecyclerView allTrackingsRecycler;

    @Inject
    InMemoryFactRepository factRepository;
    @Inject
    StatisticsContract.StatisticsInteractor statisticsInteractor;

    TextView hint;
    List<Fact> facts = new ArrayList<>();

    CarouselView carousel;
    AppCompatSpinner s;

    FloatingActionButton recountBtn;

    List<String> titles = new ArrayList<>();
    @Inject
    TrackingService service;
    List<TrackingV1> allTrackingV1s = new ArrayList<>();

    ArrayAdapter<String> spinneradapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        YandexMetrica.reportEvent(getString(R.string.metrica_enter_statistics));
        ItHappenedApplication.getAppComponent().inject(this);
        return inflater.inflate(ru.lod_misis.ithappened.R.layout.fragment_statistics, null);

    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Статистика");

        allTrackingV1s = new ArrayList<>();
        titles = new ArrayList<>();
        titles.add("Общая статистика");
        for (TrackingV1 trackingV1 : service.GetTrackingCollection()) {
            if (!trackingV1.GetStatus()) {
                titles.add(trackingV1.GetTrackingName());
                allTrackingV1s.add(trackingV1);
            }
        }

        titles = new ArrayList<>();
        allTrackingV1s = new ArrayList<>();
        titles.add("Общая статистика");
        for (TrackingV1 trackingV1 : service.GetTrackingCollection()) {
            if (!trackingV1.GetStatus()) {
                titles.add(trackingV1.GetTrackingName());
                allTrackingV1s.add(trackingV1);
            }
        }

        carousel = (CarouselView) getActivity().findViewById(R.id.mainCarousel);
        carousel.setViewListener(viewListener);
        carousel.setPageCount(titles.size());
        loading = getActivity().findViewById(R.id.statisticsProgressBar);
        carousel.setIndicatorVisibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS);
        getActivity().setTitle(titles.get(0));
        ((UserActionsActivity) getActivity()).getSupportActionBar().setDisplayShowCustomEnabled(true);
        ((UserActionsActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        LayoutInflater inflator = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vi = inflator.inflate(R.layout.statistics_action_bar_spinner, null);

        s = (AppCompatSpinner) vi.findViewById(R.id.statisticsSpinner);

        recountBtn = (FloatingActionButton) getActivity().findViewById(R.id.recountStatistics);

        spinneradapter = new ArrayAdapter<String>(getActivity(), R.layout.statistics_spinner_item, titles);
        spinneradapter.setDropDownViewResource(R.layout.dropdown_spinner_item);

        s.setAdapter(spinneradapter);

        ((UserActionsActivity) getActivity()).getSupportActionBar().setCustomView(vi);

        spinneradapter.notifyDataSetChanged();

        recountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statisticsInteractor.loadingFacts(service);
            }
        });

        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                carousel.setCurrentItem(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        carousel.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                s.setSelection(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();
        ((UserActionsActivity) getActivity()).getSupportActionBar().setDisplayShowCustomEnabled(false);
        ((UserActionsActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        YandexMetrica.reportEvent(getString(R.string.metrica_exit_statistics));
    }

    ViewListener viewListener = new ViewListener() {
        @Override
        public View setViewForPosition(int position) {
            View customView = getActivity().getLayoutInflater().inflate(R.layout.view_statistics, null);
            factRepository = StaticFactRepository.getInstance();
            if (position == 0) {
                facts = new ArrayList<>();
                customView = getActivity().getLayoutInflater().inflate(R.layout.all_statistics_layout, null);
                StaticInMemoryRepository.setUserId(getActivity().getSharedPreferences(
                        "MAIN_KEYS", Context.MODE_PRIVATE).getString("UserId", ""));
                ITrackingRepository trackingCollection = StaticInMemoryRepository.getInstance();
                facts = factRepository.getAllTrackingsFactCollection();
                statisticsInteractor = new StatisticsInteractorImpl(getActivity(), factRepository);

                if (facts.size() != 0) {
                    hint = (TextView) customView.findViewById(R.id.hintAllTrackingsFacts);
                    hint.setVisibility(View.INVISIBLE);
                    allTrackingsRecycler = (RecyclerView) customView.findViewById(R.id.allStatisticsRecycler);
                    StatisticsAdapter adapter = new StatisticsAdapter(facts, getActivity().getApplicationContext());
                    allTrackingsRecycler.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                    allTrackingsRecycler.setAdapter(adapter);
                }
            } else {

                facts = new ArrayList<>();
                customView = getActivity().getLayoutInflater().inflate(R.layout.one_tracking_statistics_layout, null);
                StaticInMemoryRepository.setUserId(getActivity().getSharedPreferences(
                        "MAIN_KEYS",
                        Context.MODE_PRIVATE).getString("UserId", ""));
                ITrackingRepository trackingCollection = StaticInMemoryRepository.getInstance();

                facts = factRepository.getOneTrackingFactCollection(allTrackingV1s.get(position - 1).GetTrackingID());


                Log.e("Size", facts.size() + "");
                if (facts.size() != 0) {
                    hint = (TextView) customView.findViewById(R.id.hintOneTrackingFacts);
                    hint.setVisibility(View.INVISIBLE);
                    allTrackingsRecycler = (RecyclerView) customView.findViewById(R.id.oneTrackingStatisticsRecycler);
                    StatisticsAdapter adapter = new StatisticsAdapter(facts, getActivity().getApplicationContext());
                    allTrackingsRecycler.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                    allTrackingsRecycler.setAdapter(adapter);
                }
            }
            getActivity().setTitle(titles.get(position));
            return customView;
        }
    };


    @Override
    public void showLoading() {
        carousel.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        carousel.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
    }

    @Override
    public void fragmentRefresh() {
        FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
        fragmentTransaction.detach(this).attach(this).commit();
    }

}