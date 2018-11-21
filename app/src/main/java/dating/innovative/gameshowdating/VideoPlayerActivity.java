package dating.innovative.gameshowdating;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoPlayerActivity extends AppCompatActivity {
    VideoView videoPlayer;
    MediaController mediaController;
    Uri uri;
    SQLiteHelper dbHelper;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoplayer);
        dbHelper = SQLiteHelper.getSqLiteHelperInstance(getApplicationContext());
        final Intent videoIdIntent = getIntent();
        videoPlayer = (VideoView) findViewById(R.id.videoPlayerView);
        mediaController = new MediaController(this);
        mediaController.setAnchorView(videoPlayer);
        if(videoIdIntent.getIntExtra("videoId",0) == 1){
            uri = Uri.parse(dbHelper.getUserByUsername(PreferenceManagerClass.getUsername(getApplicationContext())).video1);
        } else if(videoIdIntent.getIntExtra("videoId",0) == 2){
            uri = Uri.parse(dbHelper.getUserByUsername(PreferenceManagerClass.getUsername(getApplicationContext())).video2);
        } else if(videoIdIntent.getIntExtra("videoId",0) == 3){
            uri = Uri.parse(dbHelper.getUserByUsername(PreferenceManagerClass.getUsername(getApplicationContext())).video3);
        }

        videoPlayer.setMediaController(mediaController);
        videoPlayer.setVideoURI(uri);
        videoPlayer.requestFocus();
        videoPlayer.start();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }


}
