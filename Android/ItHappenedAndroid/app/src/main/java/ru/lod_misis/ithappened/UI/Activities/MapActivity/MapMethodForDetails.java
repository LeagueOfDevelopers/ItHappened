package ru.lod_misis.ithappened.UI.Activities.MapActivity;

import android.app.Activity;

import com.google.android.gms.maps.model.LatLng;

public class MapMethodForDetails extends CommonMethodForMapAlgorithm {
    Double longitude;
    Double latitude;

    public MapMethodForDetails (Double latitude , Double longitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @Override
    public void returnData (Activity activity) {
        activity.setResult(Activity.RESULT_OK);
        activity.finish();
    }

    @Override
    public LatLng initStartedLocation () {
        return new LatLng(latitude , longitude);
    }

    public Boolean isAddGeopositionOrNot () {
        return false;
    }
}
