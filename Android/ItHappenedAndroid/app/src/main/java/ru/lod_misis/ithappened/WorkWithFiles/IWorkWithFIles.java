package ru.lod_misis.ithappened.WorkWithFiles;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import java.io.File;

public interface IWorkWithFIles {
    String saveBitmap(Uri uri);
    Bitmap loadImage(String path);
    Uri generateFileUri(int type);
    String getUriPhotoFromCamera();
}
