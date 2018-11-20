package dating.innovative.gameshowdating;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;
import org.jetbrains.annotations.NotNull;

public class VideoSettingActivity extends Activity {

    final Intent lastScreenIntent = getIntent();
    Button uploadVideoButton;
    Button recordVideoButton;
    Button returnButton;
    TextView confirmationLabel;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_setting);

        uploadVideoButton = (Button) findViewById(R.id.videoSettingUploadButton);
        recordVideoButton = (Button) findViewById(R.id.videoSettingRecordButton);
        returnButton = (Button) findViewById(R.id.videoSettingReturnButton);
        confirmationLabel = (TextView) findViewById(R.id.videoSettingLabel);

    }

}
