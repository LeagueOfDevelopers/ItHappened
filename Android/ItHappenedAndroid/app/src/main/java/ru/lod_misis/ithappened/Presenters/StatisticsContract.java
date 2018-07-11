package ru.lod_misis.ithappened.Presenters;

import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;

/**
 * Created by Пользователь on 10.07.2018.
 */

public interface StatisticsContract {
    interface StatisticsView{
        void fragmentRefresh();
        void showLoading();
        void hideLoading();
    }
    interface StatisticsPresenter{

    }
    interface StatisticsInteractor{
        void loadingFacts(ITrackingRepository trackingCollection);
    }
}
