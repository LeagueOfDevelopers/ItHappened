package ru.lod_misis.ithappened.UI.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import ru.lod_misis.ithappened.R;
import ru.lod_misis.ithappened.Domain.PhotoInteractor.PhotoInteractor;

public class ChoicePhotoDialog extends Dialog {
    Button gallery;
    Button camera;
    String uriPhotoFromCamera;
    PhotoInteractor workWithFIles;
    Activity activity;
    Context context;
    public ChoicePhotoDialog(@NonNull Context context,Activity activity,PhotoInteractor workWithFIles) {
        super(context);
        this.activity=activity;
        this.context=context;
        this.workWithFIles = workWithFIles;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choice_photo_dialog);
    }

    @Override
    protected void onStart() {
        super.onStart();
        camera=findViewById(R.id.camera_button);
        gallery=findViewById(R.id.gallery_button);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickCamera();
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickGallery();
            }
        });
    }
    private void pickCamera(){

        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        uriPhotoFromCamera=workWithFIles.generateFileUri(1).toString();
        intent.putExtra(MediaStore.EXTRA_OUTPUT,workWithFIles.generateFileUri(1));
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        activity.startActivityForResult(intent,1);
    }
    private void pickGallery(){
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivityForResult(intent,2);
        }
    }

    public String getUriPhotoFromCamera() {
        return uriPhotoFromCamera;
    }
}
