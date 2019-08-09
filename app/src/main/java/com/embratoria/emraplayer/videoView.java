package com.embratoria.emraplayer;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.devbrackets.android.exomedia.core.video.scale.ScaleType;
import com.devbrackets.android.exomedia.ui.widget.VideoControlsCore;
import com.devbrackets.android.exomedia.ui.widget.VideoView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;



public class videoView extends AppCompatActivity {


    private static final String TAG = "VIDEOVIEW";
    String hls_url = "https://aja-hd-web-hls-live.secure.footprint.net/egress/chandler/aljazeera2/arabichd/index5000.m3u8";

    //String hls_url = "https://youtu.be/xbxDuKqV61Y";

    VideoView videoView;
    ImageView play, fullscreen;
    Boolean playPause = true;
    Boolean fullScreen = false;
    boolean showhide = false;
    RelativeLayout control;

    private InterstitialAd mInterstitialAd;

    private void showHide() {
        if (showhide)
            control.setVisibility(View.GONE);
        else {
            control.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showHide();
                }
            }, 5000);
        }
        showhide = !showhide;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.full_screen);
        MobileAds.initialize(this,
                "ca-app-pub-3940256099942544~3347511713");

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener(){

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                mInterstitialAd.loadAd(new AdRequest.Builder().build());

            }
        });
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_VIEW.equals(action) && type != null) {
            if (type.indexOf("video/*") != -1)
            {
                //Log.d(TAG, "onCreate: "+);
                hls_url = intent.getData().toString();
                init(true);
            }
        }
        else if (type != null && type.equals("localfile"))
        {
            hls_url = intent.getExtras().getString("filepath");
            init(true);
        }
        else if (action.equals("play_stream_from_ea"))
        {
            hls_url = intent.getStringExtra("streamurl");
            init(false);
        }
        else {
                init(false);
        }






    }
    private  void init(final boolean extern)
    {

        videoView = findViewById(R.id.video_view);
        play = findViewById(R.id.play);
        control = findViewById(R.id.control);
        fullscreen = findViewById(R.id.fullscreen);
        showHide();
        fullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fullScreen) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    videoView.setMeasureBasedOnAspectRatioEnabled(true);
                    videoView.setScaleType(ScaleType.NONE);
                    fullscreen.setImageResource(R.drawable.ic_fullscreen_black_24dp);
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                    if (!extern){
                        videoView.setMeasureBasedOnAspectRatioEnabled(false);
                        videoView.setScaleType(ScaleType.CENTER_CROP);
                    }
                    fullscreen.setImageResource(R.drawable.ic_fullscreen_exit_black_24dp);
                }
                fullScreen = !fullScreen;

            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (playPause) {
                    videoView.pause();
                    play.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    }
                } else {
                    videoView.setVideoURI(Uri.parse(hls_url));
                    play.setImageResource(R.drawable.ic_pause_black_24dp);
                }
                playPause = !playPause;
            }
        });
        videoView.setOnPreparedListener(new com.devbrackets.android.exomedia.listener.OnPreparedListener() {

            @Override
            public void onPrepared() {
                videoView.start();
            }
        });
        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHide();
            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        if (videoView != null) {
            try {
                videoView.stopPlayback();

            } catch (Exception e) {
                return;
            }

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoView != null) {
            try {
                videoView.stopPlayback();
                videoView.release();
            } catch (Exception e) {
                return;
            }

        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (videoView != null)
            videoView.setVideoURI(Uri.parse(hls_url));
    }
}
