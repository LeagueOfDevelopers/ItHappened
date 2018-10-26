package ru.lod_misis.ithappened.WorkWithFiles;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


import javax.inject.Inject;

import static android.content.Context.MODE_PRIVATE;
import static com.github.mikephil.charting.charts.Chart.LOG_TAG;

public class WorkWithFiles implements IWorkWithFIles {
    final int TYPE_PHOTO = 1;
    final int TYPE_VIDEO = 2;
    Application application;
    Context context;
    String uriPhotoFromCamera;

    @Inject
    public WorkWithFiles(Application application, Context context){
        this.application=application;
        this.context=context;
    }

    @Override
    public String saveBitmap(Uri uri) {
        try {
            Bitmap bitmap= MediaStore.Images.Media.getBitmap(application.getContentResolver(),uri);
            File file = new File(application.getFilesDir(),"/" + "photo" + System.currentTimeMillis() + ".jpg");
            FileOutputStream fOut = null;
            fOut = application.openFileOutput(file.getName(),Application.MODE_PRIVATE);
            ByteArrayOutputStream stream=new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            fOut.write(stream.toByteArray());
            fOut.close();
            return file.getPath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
         catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Bitmap loadImage(String path) {
        Bitmap bitmap;
        return bitmap=BitmapFactory.decodeFile(path);
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
        uriPhotoFromCamera=FileProvider.getUriForFile(context, "ru.lod_misis.ithappened.fileprovider", file).toString();
        return FileProvider.getUriForFile(context, "ru.lod_misis.ithappened.fileprovider", file);
    }
    @Override
    public String getUriPhotoFromCamera() {
        return uriPhotoFromCamera;
    }

}
