package ru.lod_misis.ithappened.Presenters;

import android.content.Context;
import android.util.Log;

import com.yandex.metrica.YandexMetrica;

import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;
import ru.lod_misis.ithappened.Infrastructure.InMemoryFactRepository;
import ru.lod_misis.ithappened.R;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Пользователь on 10.07.2018.
 */

public class StatisticsInteractorImpl implements StatisticsContract.StatisticsInteractor {

    InMemoryFactRepository factRepository;
    StatisticsContract.StatisticsView statisticsView;
    Context context;

    public StatisticsInteractorImpl(Context context, InMemoryFactRepository factRepository) {
        this.context = context;
        this.factRepository = factRepository;
    }

    @Override
    public void loadingFacts(final ITrackingRepository trackingCollection) {
//        statisticsView.showLoading();
        YandexMetrica.reportEvent(context.getString(R.string.metrica_recount_statistics));
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

//                                        statisticsView.fragmentRefresh();
//                                        statisticsView.hideLoading();
                                    }
                                });
                    }
                });
    }

    public void setStatisticsView(StatisticsContract.StatisticsView statisticsView) {
        this.statisticsView = statisticsView;
    }
}
