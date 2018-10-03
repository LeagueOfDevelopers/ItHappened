package ru.lod_misis.ithappened.Activities.MapActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import ru.lod_misis.ithappened.MyGeopositionService;

import static android.content.Context.LOCATION_SERVICE;

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
