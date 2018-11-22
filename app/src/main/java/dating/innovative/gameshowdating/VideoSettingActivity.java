package dating.innovative.gameshowdating;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;

import dating.innovative.gameshowdating.data.WebSocketHandler;

public class VideoSettingActivity extends BaseActivity {


    VideoView videoView;
    Button uploadVideoButton;
    Button recordVideoButton;
    Button returnButton;
    TextView confirmationLabel;
    MediaController mediaController;
    Uri videoUri;

    @NotNull
    @Override
    public Toolbar getToolBar() {
        return findViewById(R.id.toolbar_video_setting);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_video_setting;
    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_setting);
        final Intent lastScreenIntent = getIntent();
        System.out.println(lastScreenIntent.getIntExtra("videoId", 0));
        uploadVideoButton = (Button) findViewById(R.id.videoSettingUploadButton);
        recordVideoButton = (Button) findViewById(R.id.videoSettingRecordButton);
        returnButton = (Button) findViewById(R.id.videoSettingReturnButton);
        confirmationLabel = (TextView) findViewById(R.id.videoSettingLabel);
        videoView = (VideoView) findViewById(R.id.videoSettingView);
        videoView.setVisibility(View.INVISIBLE);
        mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);

        uploadVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent uploadVideo = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(uploadVideo, 0);
            }
        });

        recordVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent recordVideo = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                if(recordVideo.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(recordVideo, 1);
                }
            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home: {
                final Intent lastScreenIntent = getIntent();
                String videoUriString = videoUri.toString();
                if (!videoUriString.isEmpty()) {
                    int videoId = lastScreenIntent.getIntExtra("videoId", 0);
                    if (videoId > 3 || videoId < 1) throw new RuntimeException("invalid videoID");
                    if (videoId == 1) {
                        PreferenceManagerClass.setPreferenceVideo1(getApplicationContext(), videoUriString);
                    } else if (videoId == 2) {
                        PreferenceManagerClass.setPreferenceVideo2(getApplicationContext(), videoUriString);
                    } else {
                        PreferenceManagerClass.setPreferenceVideo3(getApplicationContext(), videoUriString);
                    }
                    WebSocketHandler ws = new WebSocketHandler();
                    ws.sendOrUpdateVideo(videoUri, PreferenceManagerClass.getUsername(this), videoId);
                }

            }
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            videoView.setVisibility(View.VISIBLE);
            videoView.setMediaController(mediaController);
            if (requestCode == 0 || requestCode == 1) {
                videoUri = data.getData();
                confirmationLabel.setText(videoUri.toString());
                videoView.setVideoURI(videoUri);
                videoView.requestFocus();
                videoView.start();

            }
        }


    }


}
