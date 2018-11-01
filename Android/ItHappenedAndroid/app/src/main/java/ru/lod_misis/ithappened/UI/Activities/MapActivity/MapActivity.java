package ru.lod_misis.ithappened.UI.Activities.MapActivity;

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

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.lod_misis.ithappened.UI.Activities.AddNewEventActivity;
import ru.lod_misis.ithappened.UI.Activities.EventDetailsActivity;
import ru.lod_misis.ithappened.R;

public class MapActivity extends AppCompatActivity {
    static Location location;
    SupportMapFragment supportMapFragment;
    LocationManager locationManager;
    Context context;
    Activity activity;

    Integer flag;
    String trackingId;
    @BindView(R.id.addGeoposition)
    Button addGeoposition;
    CommonMethodForMapAlgorithm algorithm;

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
            location=getLastKnownLocation();
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
        intent.putExtra("latitude", algorithm.getLocation().latitude + "");
        intent.putExtra("longitude", algorithm.getLocation().longitude + "");
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

                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                } else {
                    if (flag == 1) {
                        algorithm = new MapMethodForAddGeoposition();
                    } else {
                        algorithm = new MapMethodForDetails(Double.valueOf(getIntent().getStringExtra("latitude")), Double.valueOf(getIntent().getStringExtra("longitude")));
                    }
                    algorithm.commonAbstractMethodForMap(googleMap);
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
