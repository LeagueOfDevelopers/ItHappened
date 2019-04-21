package ru.lod_misis.ithappened.domain.photointeractor;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class PhotoInteractorImpl implements PhotoInteractor {
    private final int TYPE_PHOTO = 1;
    private final int TYPE_VIDEO = 2;
    private Context context;
    private String uriPhotoFromCamera;
    private Map<String, Bitmap> cache;


    @Inject
    public PhotoInteractorImpl(Context context) {
        this.context = context;
        cache = new HashMap<>();
    }

    @Override
    public String saveBitmap(Uri uri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            File file = new File(context.getFilesDir(), "/" + "photo" + System.currentTimeMillis() + ".jpg");
            FileOutputStream fOut = null;
            fOut = context.openFileOutput(file.getName(), Application.MODE_PRIVATE);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            fOut.write(stream.toByteArray());
            fOut.close();
            return file.getPath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Uri generateFileUri(int type) {
        File file = null;
        switch (type) {
            case TYPE_PHOTO: {
                file = new File(context.getFilesDir() + "/" + "photo" + System.currentTimeMillis() + ".jpg");
                break;
            }
            case TYPE_VIDEO: {
                break;
            }
        }
        uriPhotoFromCamera = FileProvider.getUriForFile(context, "ru.lod_misis.ithappened.fileprovider", file).toString();
        return FileProvider.getUriForFile(context, "ru.lod_misis.ithappened.fileprovider", file);
    }

    @Override
    public String getUriPhotoFromCamera() {
        return uriPhotoFromCamera;
    }

}
