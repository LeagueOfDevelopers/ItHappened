package ru.lod_misis.ithappened.domain.photointeractor;

import android.graphics.Bitmap;
import android.net.Uri;

public interface PhotoInteractor {
    String saveBitmap (Uri uri);

    Bitmap loadImage (String path);

    Uri generateFileUri (int type);

    String getUriPhotoFromCamera ();
}
