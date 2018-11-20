package dating.innovative.gameshowdating;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class VideoPlayerActivity extends AppCompatActivity {
    //This class should not have the options bar since it should be a full screen video player!
    //pls do not fix it to extend BaseActivity

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoplayer);
    }


}
