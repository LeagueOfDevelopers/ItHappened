package ru.lod_misis.ithappened.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;

import java.util.ArrayList;
import java.util.List;

import ru.lod_misis.ithappened.Activities.UserActionsActivity;
import ru.lod_misis.ithappened.Application.TrackingService;
import ru.lod_misis.ithappened.Domain.Tracking;
import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;
import ru.lod_misis.ithappened.Infrastructure.InMemoryFactRepository;
import ru.lod_misis.ithappened.R;
import ru.lod_misis.ithappened.Recyclers.StatisticsAdapter;
import ru.lod_misis.ithappened.StaticInMemoryRepository;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;
import rx.functions.Action1;


public class StatisticsFragment extends Fragment {

    RecyclerView trackingsRecycler;
    StatisticsAdapter trackAdpt;

    RecyclerView allTrackingsRecycler;

    TextView hint;

    CarouselView carousel;
    AppCompatSpinner s;

    List<String> titles = new ArrayList<>();

    ITrackingRepository trackingCollection;
    TrackingService service;
    List<Tracking> allTrackings = new ArrayList<>();

    ArrayAdapter<String> spinneradapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        trackingCollection = new StaticInMemoryRepository(getActivity().getApplicationContext(),
                getActivity().getSharedPreferences("MAIN_KEYS", Context.MODE_PRIVATE).getString("UserId", "")).getInstance();
        service = new TrackingService(getActivity().getSharedPreferences("MAIN_KEYS", Context.MODE_PRIVATE).getString("UserId", ""), trackingCollection);
        titles.add("Общая статистика");
        for(Tracking tracking: service.GetTrackingCollection()){
            if(!tracking.GetStatus()){
                titles.add(tracking.GetTrackingName());
                allTrackings.add(tracking);
            }
        }
        return inflater.inflate(ru.lod_misis.ithappened.R.layout.fragment_statistics, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Статистика");

        hint = (TextView) getActivity().findViewById(R.id.hintForStatisticsFragment);


        carousel = (CarouselView) getActivity().findViewById(R.id.mainCarousel);
        carousel.setViewListener(viewListener);
        carousel.setPageCount(titles.size());
        getActivity().setTitle(titles.get(0));
        ((UserActionsActivity)getActivity()).getSupportActionBar().setDisplayShowCustomEnabled(true);
        ((UserActionsActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        LayoutInflater inflator = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vi = inflator.inflate(R.layout.statistics_action_bar_spinner, null);

        s = (AppCompatSpinner) vi.findViewById(R.id.statisticsSpinner);

        spinneradapter = new ArrayAdapter<String>(getActivity(),R.layout.statistics_spinner_item, titles);

        s.setAdapter(spinneradapter);

        ((UserActionsActivity)getActivity()).getSupportActionBar().setCustomView(vi);

        spinneradapter.notifyDataSetChanged();



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
        ((UserActionsActivity)getActivity()).getSupportActionBar().setDisplayShowCustomEnabled(false);
        ((UserActionsActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    ViewListener viewListener = new ViewListener() {
        @Override
        public View setViewForPosition(int position) {
            View customView = getActivity().getLayoutInflater().inflate(R.layout.view_statistics, null);
            if(position==0) {
                customView = getActivity().getLayoutInflater().inflate(R.layout.all_statistics_layout, null);
                InMemoryFactRepository factRepository = new InMemoryFactRepository();
                final List<Fact> facts = new ArrayList<>();
                ITrackingRepository trackingCollection = new StaticInMemoryRepository(getActivity().getApplicationContext(),
                        getActivity().getSharedPreferences("MAIN_KEYS", Context.MODE_PRIVATE).getString("UserId", "")).getInstance();

                factRepository.calculateAllTrackingsFacts(trackingCollection.GetTrackingCollection())
                        .subscribe(new Action1<Fact>() {
                            @Override
                            public void call(Fact fact) {
                                fact.calculatePriority();
                                facts.add(fact);
                            }
                        });

                if(facts.size()!=0) {
                    hint.setVisibility(View.INVISIBLE);
                    allTrackingsRecycler = (RecyclerView) customView.findViewById(R.id.allStatisticsRecycler);
                    StatisticsAdapter adapter = new StatisticsAdapter(facts, getActivity().getApplicationContext());
                    allTrackingsRecycler.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                    allTrackingsRecycler.setAdapter(adapter);
                }
            }else{
                customView = getActivity().getLayoutInflater().inflate(R.layout.one_tracking_statistics_layout, null);
                InMemoryFactRepository factRepository = new InMemoryFactRepository();
                final List<Fact> facts = new ArrayList<>();
                ITrackingRepository trackingCollection = new StaticInMemoryRepository(getActivity().getApplicationContext(),
                        getActivity().getSharedPreferences("MAIN_KEYS", Context.MODE_PRIVATE).getString("UserId", "")).getInstance();

                factRepository.onChangeCalculateOneTrackingFacts(trackingCollection.GetTrackingCollection(), allTrackings.get(position-1).GetTrackingID())
                        .subscribe(new Action1<Fact>() {
                            @Override
                            public void call(Fact fact) {
                                fact.calculatePriority();
                                facts.add(fact);
                            }
                        });

                if(facts.size()!=0) {
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

}
