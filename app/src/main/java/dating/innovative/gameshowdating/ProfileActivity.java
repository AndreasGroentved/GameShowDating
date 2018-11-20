package dating.innovative.gameshowdating;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends BaseActivity {

    TextView profileUsername;
    TextView profileBiography;
    ImageView profileImage;
    Button video1;
    Button video2;
    Button video3;
    SQLiteHelper dbHelper;


    public void onCreate(Bundle onSavedState) {
        super.onCreate(onSavedState);
        profileUsername = findViewById(R.id.profileName);
        profileBiography = findViewById(R.id.profileBiography);
        profileImage = findViewById(R.id.profileImage);
        video1 = findViewById(R.id.profileVideo1Button);
        video2 = findViewById(R.id.profileVideo2Button);
        video3 = findViewById(R.id.profileVideo3Button);
        dbHelper = SQLiteHelper.getSqLiteHelperInstance(getApplicationContext());

        profileUsername.setText(dbHelper.getUserByUsername(PreferenceManagerClass.getUsername(getApplicationContext())).username + ", " +
        dbHelper.getUserByUsername(PreferenceManagerClass.getUsername(getApplicationContext())).sex + ", " +
        dbHelper.getUserByUsername(PreferenceManagerClass.getUsername(getApplicationContext())).age);
        profileBiography.setText(dbHelper.getUserByUsername(PreferenceManagerClass.getUsername(getApplicationContext())).biography);

        video1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkVideoContent(dbHelper.getUserByUsername(PreferenceManagerClass.getUsername(getApplicationContext())).video1);
            }
        });
        video2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkVideoContent(dbHelper.getUserByUsername(PreferenceManagerClass.getUsername(getApplicationContext())).video2);
            }
        });
        video3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkVideoContent(dbHelper.getUserByUsername(PreferenceManagerClass.getUsername(getApplicationContext())).video3);
            }
        });
    }

    public void checkVideoContent(String videoURL){
        if(!videoURL.isEmpty()){
            Intent i = new Intent(getApplicationContext(), VideoPlayerActivity.class);
            i.putExtra("videoId", R.raw.video);
            startActivity(i);
        } else {
            Toast.makeText(getApplicationContext(),"There is no available video", Toast.LENGTH_LONG).show();
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.profileLogOut:
                PreferenceManagerClass.clearPreferences(getApplicationContext());
                Intent logoutIntent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(logoutIntent);
                break;
            case R.id.profileCustomize:
                Intent customizeIntent = new Intent(getApplicationContext(), CustomizeProfileActivity.class);
                startActivity(customizeIntent);
        }
        return false;
    }

}
