package ru.lod_misis.ithappened.UI.Activities.MapActivity;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public abstract class CommonMethodForMapAlgorithm {
    GoogleMap map;
    LatLng location;
    Marker marker;

    public void commonAbstractMethodForMap(GoogleMap googleMap) {
        this.map = googleMap;
        location = initStartedLocation();
        initMap();
        if (isAddGeopositionOrNot())
            setAllListenersIfNeedIt();
    }

    abstract public LatLng initStartedLocation();

    public void setAllListenersIfNeedIt() {
        marker.setDraggable(true);
        map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                location = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
            }
        });
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (marker == null) {
                    marker = map.addMarker(new MarkerOptions().position(latLng));
                    marker.setDraggable(true);
                } else {
                    marker.setPosition(latLng);
                }
                location = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
            }
        });
    }

    ;

    private void initMap() {
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

    public Boolean isAddGeopositionOrNot() {
        return true;
    }

    public LatLng getLocation() {
        return location;
    }
}
