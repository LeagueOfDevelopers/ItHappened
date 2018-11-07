package ru.lod_misis.ithappened.ui.presenters;

import android.content.Context;
import android.util.Log;

import com.yandex.metrica.YandexMetrica;

import javax.inject.Inject;

import ru.lod_misis.ithappened.domain.TrackingService;
import ru.lod_misis.ithappened.data.repository.InMemoryFactRepository;
import ru.lod_misis.ithappened.R;
import ru.lod_misis.ithappened.domain.statistics.facts.Fact;
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

    @Inject
    public StatisticsInteractorImpl(Context context, InMemoryFactRepository factRepository) {
        this.context = context;
        this.factRepository = factRepository;
    }

    @Override
    public void loadingFacts(final TrackingService service) {
//        statisticsView.showLoading();
        YandexMetrica.reportEvent(context.getString(R.string.metrica_recount_statistics));
        factRepository.calculateAllTrackingsFacts(service.GetTrackingCollection())
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Fact>() {
                    @Override
                    public void call(Fact fact) {
                        Log.d("statistics", "calculate");
                        factRepository.calculateOneTrackingFacts(service.GetTrackingCollection())
                                .subscribeOn(Schedulers.computation())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Action1<Fact>() {
                                    @Override
                                    public void call(Fact fact) {
                                        Log.d("statistics", "calculateOneTrackingFact");

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
