package ru.lod_misis.ithappened.Statistics;

import android.util.Log;

import java.util.UUID;

import javax.inject.Inject;

import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;
import ru.lod_misis.ithappened.Infrastructure.InMemoryFactRepository;
import ru.lod_misis.ithappened.Infrastructure.StaticFactRepository;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Пользователь on 23.05.2018.
 */

public class FactCalculator {

    private ITrackingRepository trackingRepository;
    InMemoryFactRepository factRepository;

    public FactCalculator(ITrackingRepository trackingRepository) {
        this.trackingRepository = trackingRepository;
        factRepository = StaticFactRepository.getInstance();
    }

    public void calculateFacts(){
        factRepository.calculateAllTrackingsFacts(trackingRepository.GetTrackingCollection())
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Fact>() {
                    @Override
                    public void call(Fact fact) {
                        Log.d("Statistics", "calculate");
                    }
                });

        factRepository.calculateOneTrackingFacts(trackingRepository.GetTrackingCollection())
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Fact>() {
                    @Override
                    public void call(Fact fact) {
                        Log.d("Statistics", "calculateOneTrackingFact");
                    }
                });
    }
    public void calculateFactsForAddNewEventActivity(UUID trackingId){
        factRepository.onChangeCalculateOneTrackingFacts(trackingRepository.GetTrackingCollection(), trackingId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Fact>() {
                    @Override
                    public void call(Fact fact) {
                        Log.d("Statistics", "calculate");
                    }
                });
        factRepository.calculateAllTrackingsFacts(trackingRepository.GetTrackingCollection())
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Fact>() {
                    @Override
                    public void call(Fact fact) {
                        Log.d("Statistics", "calculate");
                    }
                });
    }
}
