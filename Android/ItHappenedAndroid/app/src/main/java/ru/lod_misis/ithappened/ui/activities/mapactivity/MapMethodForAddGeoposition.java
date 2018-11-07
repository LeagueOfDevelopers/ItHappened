package ru.lod_misis.ithappened.ui.activities.mapactivity;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import ru.lod_misis.ithappened.ui.background.MyGeopositionService;

public class MapMethodForAddGeoposition extends CommonMethodForMapAlgorithm {
    @Override
    public LatLng initStartedLocation() {
        Location location = MyGeopositionService.myLocation;
        if (location == null) {
            location = MapActivity.location;
        }
        return new LatLng(location.getLatitude(),location.getLongitude());
    }
}
