package ru.lod_misis.ithappened.ui.activities.mapactivity;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import ru.lod_misis.ithappened.ui.background.GeopositionService;

import static android.content.Context.LOCATION_SERVICE;

public class MapMethodForAddGeoposition extends CommonMethodForMapAlgorithm {
    private Context context;
    private Location location;

    public MapMethodForAddGeoposition(Context context) {
        super();
        this.context = context;
    }

    @Override
    public LatLng initStartedLocation() throws Exception {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MAIN_KEYS", Context.MODE_PRIVATE);
        float longitude = sharedPreferences.getFloat("LongitudeCoordinate", 0);
        float latitude = sharedPreferences.getFloat("LatitudeCoordinate", 0);
        if (longitude == 0 && latitude == 0) {
            LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                throw new Exception();

            }
            location = locationManager.getLastKnownLocation(locationManager.getAllProviders().get(0));
            return new LatLng(location.getLatitude(), location.getLongitude());
        } else {
            return new LatLng(latitude, longitude);
        }


    }

}
