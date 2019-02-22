package ru.lod_misis.ithappened.ui.presenters;

import android.view.View;

import java.util.UUID;

public interface EventsFragmnetCallBack {
    void showPopupMenu(View v, final UUID trackingId, final UUID eventId);
}
