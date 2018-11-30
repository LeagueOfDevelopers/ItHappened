package ru.lod_misis.ithappened.ui.presenters;

import android.content.Context;

import com.anjlab.android.iab.v3.BillingProcessor;

import ru.lod_misis.ithappened.domain.BillingInteractor.BillingInteractor;

public class BillingPresenter {
    private static BillingPresenter billingPresenter;
    private BillingInteractor billingInteractor;
    private BillingView billingView;

    private BillingPresenter (Context context) {
        billingInteractor = new BillingInteractor(context);
    }

    public synchronized static BillingPresenter getInstance (Context context) {
        if (billingPresenter == null) {
            billingPresenter = new BillingPresenter(context);
        }
        return billingPresenter;
    }

    public void atachView (BillingView billingView) {
        this.billingView = billingView;
    }

    public void detachView () {
        billingView = null;
    }

    public void buyFullVersion () {
        billingInteractor.startPurchase();
    }

    public void checkPurchase () {
        if (billingView != null) {
            billingView.getPurchase(billingInteractor.alreadyBuy());
        }
    }

    public BillingProcessor getBillingProcessor () {
        return billingInteractor.getBillingProcessor();
    }
}
