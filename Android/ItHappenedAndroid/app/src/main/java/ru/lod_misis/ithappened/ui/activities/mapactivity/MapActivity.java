package ru.lod_misis.ithappened.ui.activities.mapactivity;

import android.Manifest;
import android.app.Activity;
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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.lod_misis.ithappened.R;

public class MapActivity extends AppCompatActivity {
    public static int MAP_ACTIVITY_REQUEST_CODE = 555;
    static Location location;
    @BindView(R.id.addGeoposition)
    Button addGeoposition;
    private SupportMapFragment supportMapFragment;
    private Integer flag;
    private CommonMethodForMapAlgorithm algorithm;

    public static void toMapActivity (Activity activity , int code , double latitude , double longitude) {
        Intent intent = new Intent(activity , MapActivity.class);
        intent.putExtra("code" , code);
        intent.putExtra("latitude" , latitude);
        intent.putExtra("longitude" , longitude);
        activity.startActivityForResult(intent , MAP_ACTIVITY_REQUEST_CODE);
    }

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);
        createAndInitMap();
        addGeoposition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                algorithm.returnData(MapActivity.this);
            }
        });

    }

    private void initMap () {
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady (GoogleMap googleMap) {
                int flag = getIntent().getIntExtra("code" , -1);
                if ( ActivityCompat.checkSelfPermission(MapActivity.this , Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapActivity.this , Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {

                } else {
                    switch ( flag ) {
                        case 1: {
                            algorithm = new MapMethodForAddGeoposition();
                            break;
                        }
                        case 2: {
                            algorithm = new MapMethodForDetails(getIntent().getDoubleExtra("latitude" , 0) , getIntent().getDoubleExtra("longitude" , 0));
                        }
                        case 3: {
                            algorithm = new MapMethodForEditGeoposition(getIntent().getDoubleExtra("latitude" , 0) , getIntent().getDoubleExtra("longitude" , 0));
                        }
                        default:
                            new Exception();
                    }
                    algorithm.commonAbstractMethodForMap(googleMap);
                }
            }
        });

    }

    private void createAndInitMap () {

        if ( ActivityCompat.checkSelfPermission(this , Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this , Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            return;
        }
        supportMapFragment = ( SupportMapFragment ) getSupportFragmentManager().findFragmentById(R.id.map);
        initMap();

    }

    private Location getLastKnownLocation () {
        LocationManager mLocationManager = ( LocationManager ) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if ( ActivityCompat.checkSelfPermission(this , Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this , Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {

            }
            Location l = mLocationManager.getLastKnownLocation(provider);
            if ( l == null ) {
                continue;
            }
            if ( bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy() ) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }
}
