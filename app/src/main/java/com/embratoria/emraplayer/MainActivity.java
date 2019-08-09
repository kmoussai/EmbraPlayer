package com.embratoria.emraplayer;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {




    ListView listView;

    MyItem[] listItem;

    public static String [] array;









    private void BuildlistView() {
        String[] res =  array;
        listItem = new MyItem[res.length];
        int i = 0;
        for(String path : res)
        {
            Bitmap bitmap = getThumblineImage(path);
            String[] name = path.split("/");
            listItem[i] = new MyItem(bitmap, path, name[name.length - 1]);
            i++;
        }


        final MyAdapter<String> adapter = new MyAdapter<>();
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // TODO Auto-generated method stub

                //Toast.makeText(getApplicationContext(),"" + listItem[position],Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), videoView.class);
                i.setType("localfile");
                i.putExtra("filepath",listItem[position].getPath());
                startActivity(i);
            }
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.list_view);

       // listItem = getResources().getStringArray(R.array.array_technology);
       /* listItem[0] = new MyItem("khalid", "test");
        listItem[1] = new MyItem("khalid", "test");
        listItem[2] = new MyItem("khalid", "test");
        listItem[3] = new MyItem("khalid", "test");*/
        String[] res;
        BuildlistView();





    }
    private class MyItem {
        private String name;
        private String path;
        private Bitmap img;

        public MyItem(Bitmap bitmap, String path, String name) {
            this.img = bitmap;
            this.path = path;
            this.name = name;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public String getPath() {
            return path;
        }

        public Bitmap getBitmap() {
            return img;
        }
    }

    private class MyAdapter<MyItem> extends BaseAdapter {

        // override other abstract methods here


        @Override
        public int getCount() {
            return listItem.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }


        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item, container, false);
            }


            ((ImageView)convertView.findViewById(R.id.img)).setImageBitmap(listItem[position].getBitmap());
            ((TextView)convertView.findViewById(R.id.filename)).setText(listItem[position].getName());

            return convertView;

        }
    }
    public static Bitmap getThumblineImage(String videoPath) {
        return ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Video.Thumbnails.MINI_KIND);
    }

}

