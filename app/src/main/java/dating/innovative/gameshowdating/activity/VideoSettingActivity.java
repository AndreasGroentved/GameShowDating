package dating.innovative.gameshowdating.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;
import dating.innovative.gameshowdating.R;
import dating.innovative.gameshowdating.data.Util;
import dating.innovative.gameshowdating.data.WebSocketHandler;
import dating.innovative.gameshowdating.util.BaseActivity;
import dating.innovative.gameshowdating.util.PreferenceManagerClass;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/*  Andreas Jeppesen - ajepp09@student.sdu.dk
    Emil Jensen - emije14@student.sdu.dk
    Nicolai Jensen - nije214@student.sdu.dk */

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_setting);
        final Intent lastScreenIntent = getIntent();
        uploadVideoButton = findViewById(R.id.videoSettingUploadButton);
        recordVideoButton = findViewById(R.id.videoSettingRecordButton);
        returnButton = findViewById(R.id.videoSettingReturnButton);
        confirmationLabel = findViewById(R.id.videoSettingLabel);
        videoView = findViewById(R.id.videoSettingView);
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
                if (recordVideo.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(recordVideo, 1);
                }
            }
        });

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (videoUri != null && !videoUri.toString().isEmpty()) {
                    if (lastScreenIntent.getIntExtra("videoId", 0) == 1) {
                        PreferenceManagerClass.setPreferenceVideo1(getApplicationContext(), videoUri.toString());
                    } else if (lastScreenIntent.getIntExtra("videoId", 0) == 2) {
                        PreferenceManagerClass.setPreferenceVideo2(getApplicationContext(), videoUri.toString());
                    } else if (lastScreenIntent.getIntExtra("videoId", 0) == 3) {
                        PreferenceManagerClass.setPreferenceVideo3(getApplicationContext(), videoUri.toString());
                    }
                    Intent returnToCustomize = new Intent(getApplicationContext(), CustomizeProfileActivity.class);
                    startActivity(returnToCustomize);
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
                File f = new File(getPath(videoUri));
                byte[] b = Util.fileToBytes(f);
                WebSocketHandler.getInstance().sendOrUpdateVideo(b, getIntent().getIntExtra("videoId", 0), new Function1<Boolean, Unit>() {
                            @Override
                            public Unit invoke(Boolean aBoolean) {
                                return null;
                            }
                        }
                );
                videoView.requestFocus();
                videoView.start();
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s = cursor.getString(column_index);
        cursor.close();
        return s;
    }


}
