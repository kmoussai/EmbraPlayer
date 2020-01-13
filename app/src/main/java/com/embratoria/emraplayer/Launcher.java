package com.embratoria.emraplayer;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class Launcher extends AppCompatActivity {


    private int versionCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_launcher);
        try {
            PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
            versionCode = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        checkFromServer();


    }

    private void checkFromServer() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://dl.dropboxusercontent.com/s/vifgjblvhff5quo/embraplayer.json?dl=0";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener < String > () {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getInt("last_app_version") > versionCode) {
                                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(jsonObject.getString("app_stream_link"))));
                                Toast.makeText(getApplicationContext(), "You need update", Toast.LENGTH_LONG).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(getApplicationContext(), "Your app is up to date", Toast.LENGTH_LONG).show();
                        Intent i = new Intent("play_stream_from_ea");
                        i.putExtra("streamurl", "https://aja-hd-web-hls-live.secure.footprint.net/egress/chandler/aljazeera2/arabichd/index5000.m3u8");
                        startActivity(i);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Check your internet Access", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


    @Override
    protected void onStart() {
        super.onStart();

        //startActivity(new Intent(getApplicationContext(), .class));
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

}