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

import ru.lod_misis.ithappened.Activities.UserActionsActivity;
import ru.lod_misis.ithappened.Application.TrackingService;
import ru.lod_misis.ithappened.Domain.NewTracking;
import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;
import ru.lod_misis.ithappened.Infrastructure.InMemoryFactRepository;
import ru.lod_misis.ithappened.Infrastructure.StaticFactRepository;
import ru.lod_misis.ithappened.R;
import ru.lod_misis.ithappened.Recyclers.StatisticsAdapter;
import ru.lod_misis.ithappened.StaticInMemoryRepository;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class StatisticsFragment extends Fragment {

    RecyclerView trackingsRecycler;
    StatisticsAdapter trackAdpt;

    ProgressBar loading;

    RecyclerView allTrackingsRecycler;

    InMemoryFactRepository factRepository;

    TextView hint;
    List<Fact> facts = new ArrayList<>();

    CarouselView carousel;
    AppCompatSpinner s;

    FloatingActionButton recountBtn;

    List<String> titles = new ArrayList<>();

    ITrackingRepository trackingCollection;
    TrackingService service;
    List<NewTracking> allNewTrackings = new ArrayList<>();

    ArrayAdapter<String> spinneradapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        YandexMetrica.reportEvent("Пользователь вошел в статистику");
        return inflater.inflate(ru.lod_misis.ithappened.R.layout.fragment_statistics, null);

    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Статистика");

        allNewTrackings = new ArrayList<>();
        titles = new ArrayList<>();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MAIN_KEYS", Context.MODE_PRIVATE);

        if(sharedPreferences.getString("LastId","").isEmpty()) {
            StaticInMemoryRepository.setUserId(sharedPreferences.getString("UserId", ""));
            trackingCollection = StaticInMemoryRepository.getInstance();
        }else{
            StaticInMemoryRepository.setUserId(sharedPreferences.getString("LastId", ""));
            trackingCollection = StaticInMemoryRepository.getInstance();
        }
        service = new TrackingService(getActivity().getSharedPreferences("MAIN_KEYS", Context.MODE_PRIVATE).getString("UserId", ""), trackingCollection);
        titles.add("Общая статистика");
        for(NewTracking newTracking : service.GetTrackingCollection()){
            if(!newTracking.GetStatus()){
                titles.add(newTracking.GetTrackingName());
                allNewTrackings.add(newTracking);
            }
        }

        carousel = (CarouselView) getActivity().findViewById(R.id.mainCarousel);
        carousel.setViewListener(viewListener);
        carousel.setPageCount(titles.size());
        loading = getActivity().findViewById(R.id.statisticsProgressBar);
        carousel.setIndicatorVisibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS);
        getActivity().setTitle(titles.get(0));
        ((UserActionsActivity)getActivity()).getSupportActionBar().setDisplayShowCustomEnabled(true);
        ((UserActionsActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        LayoutInflater inflator = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vi = inflator.inflate(R.layout.statistics_action_bar_spinner, null);

        s = (AppCompatSpinner) vi.findViewById(R.id.statisticsSpinner);

        recountBtn = (FloatingActionButton) getActivity().findViewById(R.id.recountStatistics);

        spinneradapter = new ArrayAdapter<String>(getActivity(),R.layout.statistics_spinner_item, titles);
        spinneradapter.setDropDownViewResource(R.layout.dropdown_spinner_item);

        s.setAdapter(spinneradapter);

        ((UserActionsActivity)getActivity()).getSupportActionBar().setCustomView(vi);

        spinneradapter.notifyDataSetChanged();

        recountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoading();
                YandexMetrica.reportEvent("Пользователь пересчитывает статистику");
                factRepository.calculateAllTrackingsFacts(trackingCollection.GetTrackingCollection())
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Fact>() {
                            @Override
                            public void call(Fact fact) {
                                Log.d("Statistics", "calculate");
                                factRepository.calculateOneTrackingFacts(trackingCollection.GetTrackingCollection())
                                        .subscribeOn(Schedulers.computation())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Action1<Fact>() {
                                            @Override
                                            public void call(Fact fact) {
                                                Log.d("Statistics", "calculateOneTrackingFact");
                                                StaticInMemoryRepository.setUserId(getActivity().getSharedPreferences("MAIN_KEYS", Context.MODE_PRIVATE).getString("UserId", ""));
                                                trackingCollection = StaticInMemoryRepository.getInstance();
                                                service = new TrackingService(getActivity().getSharedPreferences("MAIN_KEYS", Context.MODE_PRIVATE).getString("UserId", ""), trackingCollection);
                                                titles = new ArrayList<>();
                                                allNewTrackings = new ArrayList<>();
                                                titles.add("Общая статистика");
                                                for(NewTracking newTracking : service.GetTrackingCollection()){
                                                    if(!newTracking.GetStatus()){
                                                        titles.add(newTracking.GetTrackingName());
                                                        allNewTrackings.add(newTracking);
                                                    }
                                                }
                                                fragmentRefresh();
                                                hideLoading();
                                            }
                                        });
                            }
                        });
            }
        });

        /*s.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(MaterialSpinner materialSpinner, int i, long l, Object o) {

            }
        });*/

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
                //s.setSelectedIndex(i);
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

    @Override
    public void onPause() {
        super.onPause();
        YandexMetrica.reportEvent("Пользователь перестал смотреть статистику");
    }

    ViewListener viewListener = new ViewListener() {
        @Override
        public View setViewForPosition(int position) {
            View customView = getActivity().getLayoutInflater().inflate(R.layout.view_statistics, null);
            factRepository = StaticFactRepository.getInstance();
            if(position==0) {
                facts = new ArrayList<>();
                customView = getActivity().getLayoutInflater().inflate(R.layout.all_statistics_layout, null);
                StaticInMemoryRepository.setUserId(getActivity().getSharedPreferences(
                        "MAIN_KEYS", Context.MODE_PRIVATE).getString("UserId", ""));
                ITrackingRepository trackingCollection = StaticInMemoryRepository.getInstance();
                facts = factRepository.getAllTrackingsFactCollection();

                if(facts.size()!=0) {
                    hint = (TextView) customView.findViewById(R.id.hintAllTrackingsFacts);
                    hint.setVisibility(View.INVISIBLE);
                    allTrackingsRecycler = (RecyclerView) customView.findViewById(R.id.allStatisticsRecycler);
                    StatisticsAdapter adapter = new StatisticsAdapter(facts, getActivity().getApplicationContext());
                    allTrackingsRecycler.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                    allTrackingsRecycler.setAdapter(adapter);
                }
            }else{

                facts = new ArrayList<>();
                customView = getActivity().getLayoutInflater().inflate(R.layout.one_tracking_statistics_layout, null);
                StaticInMemoryRepository.setUserId(getActivity().getSharedPreferences(
                                "MAIN_KEYS",
                                Context.MODE_PRIVATE).getString("UserId", ""));
                ITrackingRepository trackingCollection = StaticInMemoryRepository.getInstance();

                facts = factRepository.getOneTrackingFactCollection(allNewTrackings.get(position-1).GetTrackingID());


                Log.e("Size", facts.size()+"");
                if(facts.size()!=0) {
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


    private void showLoading(){
        carousel.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
    }

    private void hideLoading(){
        carousel.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
    }


    public void fragmentRefresh(){



        FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
        fragmentTransaction.detach(this).attach(this).commit();
    }

}
