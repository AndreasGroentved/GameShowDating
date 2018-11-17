package dating.innovative.gameshowdating;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class ProfileActivity extends BaseActivity {

    TextView testText;

    public void onCreate(Bundle onSavedState) {
        super.onCreate(onSavedState);

        testText = findViewById(R.id.testText);

    }

    @Override
    public int getLayout() {
        return R.layout.activity_profile;
    }

    @NonNull
    @Override
    public Toolbar getToolBar() {
        return findViewById(R.id.toolbar_profile);
    }
}
