package ru.lod_misis.ithappened.ui.presenters;

import android.app.Activity;
import android.content.Context;

import com.anjlab.android.iab.v3.BillingProcessor;

import ru.lod_misis.ithappened.domain.BillingInteractor.BillingInteractor;
import ru.lod_misis.ithappened.domain.BillingInteractor.IBilling;

public class BillingPresenter {
    private IBilling billingInteractor;
    private BillingView billingView;

    public BillingPresenter(Activity activity) {
        billingInteractor = BillingInteractor.getInstance(activity, this);
    }

    public void attachView(BillingView billingView) {
        this.billingView = billingView;
    }

    public void detachView() {
        billingView = null;
    }

    public void buyFullVersion() {
        billingInteractor.startPurchase();
    }

    public void checkPurchase() {
        if (billingView != null) {
            billingView.getPurchase(billingInteractor.checkPurchase());
        }
    }

    public BillingProcessor getBillingProcessor() {
        return billingInteractor.getBillingProcessor();
    }

    public void purchaseSuccess() {
        if (billingView != null) {
            billingView.getPurchase(true);
        }
    }
}
