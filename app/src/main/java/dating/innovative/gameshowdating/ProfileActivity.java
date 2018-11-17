package dating.innovative.gameshowdating;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import org.w3c.dom.Text;

public class ProfileActivity extends Activity {

    TextView testText;

    public void onCreate(Bundle onSavedState) {
        super.onCreate(onSavedState);
        setContentView(R.layout.activity_profile);

        testText = (TextView) findViewById(R.id.testText);

    }

}
