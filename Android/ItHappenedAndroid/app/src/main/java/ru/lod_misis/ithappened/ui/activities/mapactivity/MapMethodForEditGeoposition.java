package ru.lod_misis.ithappened.ui.activities.mapactivity;

import com.google.android.gms.maps.model.LatLng;

public class MapMethodForEditGeoposition extends CommonMethodForMapAlgorithm {
    private Double longitude;
    private Double latitude;

    public MapMethodForEditGeoposition (Double latitude , Double longitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @Override
    public LatLng initStartedLocation () {
        return new LatLng(latitude , longitude);
    }

    public Boolean isAddGeopositionOrNot () {
        return true;
    }
}
