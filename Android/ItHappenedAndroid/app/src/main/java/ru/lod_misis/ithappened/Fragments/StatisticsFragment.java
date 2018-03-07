package ru.lod_misis.ithappened.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;

import java.util.ArrayList;
import java.util.List;

import ru.lod_misis.ithappened.Application.TrackingService;
import ru.lod_misis.ithappened.Domain.Tracking;
import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;
import ru.lod_misis.ithappened.R;
import ru.lod_misis.ithappened.Recyclers.StatisticsAdapter;
import ru.lod_misis.ithappened.StaticInMemoryRepository;


public class StatisticsFragment extends Fragment {

    RecyclerView trackingsRecycler;
    StatisticsAdapter trackAdpt;

    TextView hint;

    CarouselView carousel;

    List<String> titles = new ArrayList<>();

    ITrackingRepository trackingCollection;
    TrackingService service;
    List<Tracking> allTrackings = new ArrayList<>();

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


        carousel = (CarouselView) getActivity().findViewById(R.id.mainCarousel);
        carousel.setViewListener(viewListener);
        carousel.setPageCount(titles.size());
        getActivity().setTitle(titles.get(0));
        carousel.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                getActivity().setTitle(titles.get(i));
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

    ViewListener viewListener = new ViewListener() {
        @Override
        public View setViewForPosition(int position) {

            View customView = getActivity().getLayoutInflater().inflate(R.layout.view_statistics, null);
            getActivity().setTitle(titles.get(position));
            return customView;
        }
    };

}
