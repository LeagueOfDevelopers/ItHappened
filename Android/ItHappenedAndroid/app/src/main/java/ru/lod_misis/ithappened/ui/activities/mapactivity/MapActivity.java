package ru.lod_misis.ithappened.ui.activities.mapactivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.yandex.metrica.YandexMetrica;

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
        YandexMetrica.reportEvent(getString(R.string.metrica_user_use_address_button));
    }

    private void initMap () {
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady (GoogleMap googleMap) {
                int flag = getIntent().getIntExtra("code" , -1);
                if (ActivityCompat.checkSelfPermission(MapActivity.this , Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapActivity.this , Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                } else {
                    switch ( flag ) {
                        case 1: {
                            algorithm = new MapMethodForAddGeoposition(getApplicationContext());
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
                    try {
                        algorithm.commonAbstractMethodForMap(googleMap);
                    }catch (Exception exeption){
                        Intent data = new Intent();
                        setResult(Activity.RESULT_CANCELED , data);
                        finish();
                    }

                }
            }
        });

    }

    private void createAndInitMap () {
        if (ActivityCompat.checkSelfPermission(this , Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this , Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        supportMapFragment = ( SupportMapFragment ) getSupportFragmentManager().findFragmentById(R.id.map);
        initMap();

    }

    @Override
    public void onBackPressed () {
        setResult(Activity.RESULT_OK);
        finish();
        super.onBackPressed();

    }
}
