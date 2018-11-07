package ru.lod_misis.ithappened.domain.statistics;

import android.util.Log;

import java.util.UUID;

import ru.lod_misis.ithappened.data.repository.TrackingDataSource;
import ru.lod_misis.ithappened.data.repository.InMemoryFactRepository;
import ru.lod_misis.ithappened.data.repository.StaticFactRepository;
import ru.lod_misis.ithappened.domain.statistics.facts.Fact;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Пользователь on 23.05.2018.
 */

public class FactCalculator {

    private TrackingDataSource trackingRepository;
    InMemoryFactRepository factRepository;

    public FactCalculator(TrackingDataSource trackingRepository) {
        this.trackingRepository = trackingRepository;
        factRepository = StaticFactRepository.getInstance();
    }

    public void calculateFacts(){
        factRepository.calculateAllTrackingsFacts(trackingRepository.getTrackingCollection())
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Fact>() {
                    @Override
                    public void call(Fact fact) {
                        Log.d("statistics", "calculate");
                    }
                });

        factRepository.calculateOneTrackingFacts(trackingRepository.getTrackingCollection())
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Fact>() {
                    @Override
                    public void call(Fact fact) {
                        Log.d("statistics", "calculateOneTrackingFact");
                    }
                });
    }
    public void calculateFactsForAddNewEventActivity(UUID trackingId){
        factRepository.onChangeCalculateOneTrackingFacts(trackingRepository.getTrackingCollection(), trackingId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Fact>() {
                    @Override
                    public void call(Fact fact) {
                        Log.d("statistics", "calculate");
                    }
                });
        factRepository.calculateAllTrackingsFacts(trackingRepository.getTrackingCollection())
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Fact>() {
                    @Override
                    public void call(Fact fact) {
                        Log.d("statistics", "calculate");
                    }
                });
    }
}
