package com.embratoria.emraplayer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;

public class Launcher extends AppCompatActivity {
    String res[];
    private boolean perm = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_launcher);


    }

    @Override
    protected void onStart() {
        super.onStart();

        if (isReadStoragePermissionGranted())
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    MainActivity.array = getAllVideoPath(getApplicationContext());
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            }, 1000);

    }

    private String[] getAllVideoPath(Context context) {
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Video.VideoColumns.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, MediaStore.Video.VideoColumns.DATE_TAKEN);
        ArrayList<String> pathArrList = new ArrayList<String>();
        //int vidsCount = 0;
        if (cursor != null) {
            //vidsCount = cursor.getCount();
            //Log.d(TAG, "Total count of videos: " + vidsCount);
            while (cursor.moveToNext()) {
                pathArrList.add(cursor.getString(0));
                //Log.d(TAG, cursor.getString(0));
                if (pathArrList.size() >= 20) break;
            }
            cursor.close();
        }

        return pathArrList.toArray(new String[pathArrList.size()]);
    }

    public boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                // Log.v(TAG,"Permission is granted1");
                return true;
            } else {

                //Log.v(TAG,"Permission is revoked1");
                perm = true;
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            //Log.v(TAG,"Permission is granted1");
            return true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!perm)
            finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 3:
                // Log.d(TAG, "External storage1");
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
                    //resume tasks needing this permission
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            perm = false;
                            MainActivity.array = getAllVideoPath(getApplicationContext());
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                    });
                } else {
                    // progress.dismiss();
                }
                break;
        }

    }

}
