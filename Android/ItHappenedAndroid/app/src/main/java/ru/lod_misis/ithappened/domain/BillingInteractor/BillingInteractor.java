package ru.lod_misis.ithappened.domain.BillingInteractor;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;

public class BillingInteractor implements BillingProcessor.IBillingHandler, IBilling {
    public static String LICENSE_KEY = null;
    public static String PRODUCT_ID = null;
    private BillingProcessor bp;
    private Activity activity;


    public BillingInteractor (Activity activity) {
        this.activity = activity;
        bp = new BillingProcessor(activity , LICENSE_KEY , this);
        ;
    }


    @Override
    public void onProductPurchased (@NonNull String productId , @Nullable TransactionDetails details) {

    }

    @Override
    public void onPurchaseHistoryRestored () {

    }

    @Override
    public void onBillingError (int errorCode , @Nullable Throwable error) {

    }

    @Override
    public void onBillingInitialized () {

    }

    @Override
    public void startPurchase () {
        bp.initialize();
        bp.purchase(activity , PRODUCT_ID);
    }

    @Override
    public Boolean alreadyBuy () {
        return null;
    }

    @Override
    public BillingProcessor getBillingProcessor () {
        return bp;
    }
}
