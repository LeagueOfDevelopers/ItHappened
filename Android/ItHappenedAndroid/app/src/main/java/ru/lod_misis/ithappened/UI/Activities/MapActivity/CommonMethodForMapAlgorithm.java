package ru.lod_misis.ithappened.UI.Activities.MapActivity;

import android.app.Activity;
import android.content.Intent;
import android.location.Geocoder;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Locale;

public abstract class CommonMethodForMapAlgorithm {
    GoogleMap map;
    LatLng location;
    Marker marker;

    public void commonAbstractMethodForMap (GoogleMap googleMap) {
        this.map = googleMap;
        location = initStartedLocation();
        initMap();
        if ( isAddGeopositionOrNot() )
            setAllListenersIfNeedIt();
    }

    abstract public LatLng initStartedLocation ();

    public void setAllListenersIfNeedIt () {
        marker.setDraggable(true);
        map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart (Marker marker) {

            }

            @Override
            public void onMarkerDrag (Marker marker) {

            }

            @Override
            public void onMarkerDragEnd (Marker marker) {
                location = new LatLng(marker.getPosition().latitude , marker.getPosition().longitude);
            }
        });
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick (LatLng latLng) {
                if ( marker == null ) {
                    marker = map.addMarker(new MarkerOptions().position(latLng));
                    marker.setDraggable(true);
                } else {
                    marker.setPosition(latLng);
                }
                location = new LatLng(marker.getPosition().latitude , marker.getPosition().longitude);
            }
        });
    }

    public void returnData (Activity activity) {
        try {
            Intent data = new Intent();
            data.putExtra("location" , getAddress(location , activity));
            activity.setResult(Activity.RESULT_OK , data);
            activity.finish();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initMap () {
        CameraUpdate cameraUpdate;
        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        marker = map.addMarker(new MarkerOptions().position(location));

        cameraUpdate = CameraUpdateFactory.newCameraPosition(
                new CameraPosition.Builder()
                        .target(location)
                        .zoom(15)
                        .build()
        );
        map.moveCamera(cameraUpdate);
    }

    public Boolean isAddGeopositionOrNot () {
        return true;
    }

    public LatLng getLocation () {
        return location;
    }

    private String getAddress (LatLng location , Activity activity) throws IOException {
        Geocoder geocoder = new Geocoder(activity , Locale.getDefault());
        return geocoder.getFromLocation(location.latitude , location.longitude , 1).get(0).getAddressLine(0);
    }
}
