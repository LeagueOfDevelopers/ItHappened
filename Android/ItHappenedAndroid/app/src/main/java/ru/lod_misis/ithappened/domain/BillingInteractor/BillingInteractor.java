package ru.lod_misis.ithappened.domain.BillingInteractor;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;

import ru.lod_misis.ithappened.ui.presenters.BillingPresenter;

public class BillingInteractor implements BillingProcessor.IBillingHandler, IBilling {
    public static String LICENSE_KEY = null;
    public static String PRODUCT_ID = "ru.lod_misis.ithappened_full_version";
    private BillingProcessor bp;
    private Activity activity;
    private BillingPresenter billingPresenter;
    private static IBilling instance;

    public static synchronized IBilling getInstance(Activity activity, BillingPresenter billingPresenter) {
        if (instance == null) {
            instance= new BillingInteractor(activity,billingPresenter);
        }
        return instance;
    }


    private BillingInteractor(Activity activity, BillingPresenter billingPresenter) {
        this.billingPresenter = billingPresenter;
        this.activity = activity;
        bp = BillingProcessor.newBillingProcessor(activity, LICENSE_KEY, this);
        bp.initialize();
    }


    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
        billingPresenter.purchaseSuccess();
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {
        Log.e("BillingInteractor", errorCode + "");
    }

    @Override
    public void onBillingInitialized() {
        Log.d("BillingInteractor", "BillingCreate");
    }

    @Override
    public void startPurchase() {
        bp.purchase(activity, PRODUCT_ID);
    }

    @Override
    public boolean checkPurchase() {
        return bp.isPurchased(PRODUCT_ID);
    }

    @Override
    public BillingProcessor getBillingProcessor() {
        return bp;
    }
}
