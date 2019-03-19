package ru.lod_misis.ithappened.ui.presenters;

import java.util.UUID;

public interface DeleteCallback {

    void finishDeleting(UUID trackingId, UUID eventId);

    void cansel();

}
