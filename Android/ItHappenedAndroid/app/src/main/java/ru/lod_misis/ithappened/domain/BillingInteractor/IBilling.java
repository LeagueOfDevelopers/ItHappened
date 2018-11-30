package ru.lod_misis.ithappened.domain.BillingInteractor;

import com.anjlab.android.iab.v3.BillingProcessor;

public interface IBilling {
    void startPurchase ();

    Boolean alreadyBuy ();

    BillingProcessor getBillingProcessor();
}
