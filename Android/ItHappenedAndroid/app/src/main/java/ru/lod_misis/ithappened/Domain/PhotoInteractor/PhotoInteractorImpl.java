package ru.lod_misis.ithappened.Domain.PhotoInteractor;

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

public class PhotoInteractorImpl implements PhotoInteractor {
    final int TYPE_PHOTO = 1;
    final int TYPE_VIDEO = 2;
    Context context;
    String uriPhotoFromCamera;

    public PhotoInteractorImpl (Context context) {
        this.context = context;
    }

    @Override
    public String saveBitmap (Uri uri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver() , uri);
            File file = new File(context.getFilesDir() , "/" + "photo" + System.currentTimeMillis() + ".jpg");
            FileOutputStream fOut = null;
            fOut = context.openFileOutput(file.getName() , Application.MODE_PRIVATE);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG , 100 , stream);
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
    public Bitmap loadImage (String path) {
        Bitmap bitmap;
        return bitmap = BitmapFactory.decodeFile(path);
    }

    @Override
    public Uri generateFileUri (int type) {
        File file = null;
        switch ( type ) {
            case TYPE_PHOTO: {
                file = new File(context.getFilesDir() + "/" + "photo" + System.currentTimeMillis() + ".jpg");
                break;
            }
            case TYPE_VIDEO: {
                break;
            }
        }
        uriPhotoFromCamera = FileProvider.getUriForFile(context , "ru.lod_misis.ithappened.fileprovider" , file).toString();
        return FileProvider.getUriForFile(context , "ru.lod_misis.ithappened.fileprovider" , file);
    }

    @Override
    public String getUriPhotoFromCamera () {
        return uriPhotoFromCamera;
    }

}
