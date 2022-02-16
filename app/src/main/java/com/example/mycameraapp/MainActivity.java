package com.example.mycameraapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUriExposedException;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Intent.ACTION_GET_CONTENT;
import static android.content.Intent.ACTION_PICK;
import static android.content.Intent.ACTION_PICK_ACTIVITY;
import static android.provider.MediaStore.*;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView textView;
    private static final int CAMERA_REQUEST = 1;
    private String currentPhotoPath = "";
    private Uri mImageUri;
    private Uri mGalleryUri;
    private static final int REQUEST_EXTERNAL_STORAGE_RESULT = 3;
    final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);
    }

    public void onClick(View view) {
        //classic code
        /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAMERA_REQUEST);*/
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                callCameraApp();
            } else {
                if (shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(getApplicationContext(),
                            "Требуется разрешение на запись", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_EXTERNAL_STORAGE_RESULT);
            }
        } else {
            callCameraApp();
        }*/
        callCameraApp();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                //imageView.setImageURI(mImageUri);

                textView.setText(currentPhotoPath);
            } else if (requestCode == 2) {




                textView.setText(mGalleryUri.toString());
            }
        }
    }
    private void callCameraApp() {
        Intent cameraAppIntent = new Intent(ACTION_IMAGE_CAPTURE);


        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String authorities = getApplicationContext().getPackageName() + ".fileprovider";
        mImageUri = FileProvider.getUriForFile(this, authorities, photoFile);
        Log.d("Provider", mImageUri.toString());
        //mOutputFileUri = Uri.fromFile(photoFile);
        //cameraAppIntent.putExtra(MediaStore.EXTRA_OUTPUT, mOutputFileUri);
        cameraAppIntent.putExtra(EXTRA_OUTPUT, mImageUri);
        startActivityForResult(cameraAppIntent, 1);
    }


    //this code made original photo name
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE_RESULT) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callCameraApp();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Нет разрешения на запись, фото не сохранено", Toast.LENGTH_SHORT)
                        .show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }



    public void onClick1(View view) {
        // this cod is for classic button-gallery
        /*Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media
                .EXTERNAL_CONTENT_URI);*/
        //startActivityForResult(intent, 2);
        // this code is same for classic gallery
        /*Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");*/

//code for WebView button-gallery
           Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(ACTION_GET_CONTENT);



        startActivityForResult(intent, 2);
    }

}
