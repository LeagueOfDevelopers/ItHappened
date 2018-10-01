package ru.lod_misis.ithappened.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.lod_misis.ithappened.MyGeopositionService;
import ru.lod_misis.ithappened.R;

public class MapActivity extends AppCompatActivity {
    SupportMapFragment supportMapFragment;
    GoogleMap map;
    LocationManager locationManager;
    Double longitude;
    Double latitude;
    Marker marker;
    Context context;
    Activity activity;

    Integer flag;
    String trackingId;
    @BindView(R.id.addGeoposition)
    Button addGeoposition;

    public static void toMapActivity(Activity activity, ArrayList<String> intents) {
        Intent intent = new Intent(activity, MapActivity.class);
        intent.putExtra("isCreateOrShow", intents.get(0));
        intent.putExtra("trackingId", intents.get(1));
        if (intents.get(0).equals("2")) {
            intent.putExtra("eventId", intents.get(2));
            intent.putExtra("latitude", intents.get(3));
            intent.putExtra("longitude", intents.get(4));
        }
        activity.startActivity(intent);
        activity.finish();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);
        context = this;
        activity = this;
        trackingId = getIntent().getStringExtra("trackingId");
        flag = Integer.valueOf(getIntent().getStringExtra("isCreateOrShow"));
        if (flag != 0) {
            createAndInitMap();
        } else {
            new Exception();
        }
        addGeoposition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag == 1)
                    addGeoposition();
                if (flag == 2)
                    backToEventDetails();
                finish();
            }
        });

    }

    private void addGeoposition() {
        Intent intent = new Intent(activity, AddNewEventActivity.class);
        intent.putExtra("trackingId", trackingId);
        intent.putExtra("latitude", latitude.toString());
        intent.putExtra("longitude", longitude.toString());
        activity.startActivity(intent);
    }

    private void backToEventDetails() {
        Intent intent = new Intent(activity, EventDetailsActivity.class);
        intent.putExtra("trackingId", trackingId);
        intent.putExtra("eventId", getIntent().getStringExtra("eventId"));
        activity.startActivity(intent);
    }

    private void initMap() {
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                CameraUpdate cameraUpdate;
                map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                } else {
                    if (flag == 1) {
                        Location location = MyGeopositionService.myLocation;
                        if (location == null) {
                            location = getLastKnownLocation();
                            marker = map.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())));
                        }
                    } else {
                        longitude = Double.valueOf(getIntent().getStringExtra("longitude"));
                        latitude = Double.valueOf(getIntent().getStringExtra("latitude"));
                        marker = map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)));
                    }

                    latitude = marker.getPosition().latitude;
                    longitude = marker.getPosition().longitude;

                    cameraUpdate = CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(new LatLng(latitude, longitude))
                                    .zoom(15)
                                    .build()
                    );
                    map.moveCamera(cameraUpdate);
                    if (flag == 1) {

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
                                latitude = marker.getPosition().latitude;
                                longitude = marker.getPosition().longitude;
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
                                latitude = latLng.latitude;
                                longitude = latLng.longitude;
                            }
                        });
                    }
                }

            }
        });

    }

    private void createAndInitMap() {

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        initMap();

    }

    private Location getLastKnownLocation() {
        LocationManager mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

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
