package ru.lod_misis.ithappened.ui.activities.mapactivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import ru.lod_misis.ithappened.ui.background.GeopositionService;

import static android.content.Context.LOCATION_SERVICE;

public class MapMethodForAddGeoposition extends CommonMethodForMapAlgorithm {
    Context context;

    public MapMethodForAddGeoposition (Context context) {
        super();
        this.context = context;
    }

    @Override
    public LatLng initStartedLocation () throws Exception{
        Location location = GeopositionService.myLocation;
        if (location == null) {
            throw new Exception();
        }
        return new LatLng(location.getLatitude() , location.getLongitude());
    }

}
