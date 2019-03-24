package ru.lod_misis.ithappened.domain.photointeractor;

import android.graphics.Bitmap;
import android.net.Uri;

import rx.Observable;
import rx.Single;

public interface PhotoInteractor {
    String saveBitmap (Uri uri);

    Observable<Bitmap> loadImage (String path);

    Uri generateFileUri (int type);

    String getUriPhotoFromCamera ();
}
