package ru.lod_misis.ithappened.ui.activities.mapactivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import ru.lod_misis.ithappened.ui.background.MyGeopositionService;

import static android.content.Context.LOCATION_SERVICE;

public class MapMethodForAddGeoposition extends CommonMethodForMapAlgorithm {
    Context context;

    public MapMethodForAddGeoposition (Context context) {
        super();
        this.context = context;
    }

    @Override
    public LatLng initStartedLocation () {
        Location location = MyGeopositionService.myLocation;
        if (location == null) {
            location = getLastKnownLocation(context);
        }
        return new LatLng(location.getLatitude() , location.getLongitude());
    }

    private Location getLastKnownLocation (Context context) {
        LocationManager mLocationManager = ( LocationManager ) context.getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(context , Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context , Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            }
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }
}
