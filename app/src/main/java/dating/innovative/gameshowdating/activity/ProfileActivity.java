package dating.innovative.gameshowdating.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import dating.innovative.gameshowdating.R;
import dating.innovative.gameshowdating.data.WebSocketHandler;
import dating.innovative.gameshowdating.util.BaseActivity;
import dating.innovative.gameshowdating.util.PreferenceManagerClass;
import dating.innovative.gameshowdating.util.SQLiteHelper;

import java.io.*;

/*  Andreas Jeppesen - ajepp09@student.sdu.dk
    Emil Jensen - emije14@student.sdu.dk
    Nicolai Jensen - nije214@student.sdu.dk */

public class ProfileActivity extends BaseActivity {

    TextView profileUsername;
    TextView profileBiography;
    ImageView profileImage;
    Button video1;
    Button video2;
    Button video3;
    SQLiteHelper dbHelper;
    File profileImageFile;
    BufferedOutputStream bos;

    public void onCreate(Bundle onSavedState) {
        super.onCreate(onSavedState);
        profileUsername = findViewById(R.id.profileName);
        profileBiography = findViewById(R.id.profileBiography);
        profileImage = findViewById(R.id.profileImage);
        video1 = findViewById(R.id.profileVideo1Button);
        video2 = findViewById(R.id.profileVideo2Button);
        video3 = findViewById(R.id.profileVideo3Button);
        dbHelper = SQLiteHelper.getSqLiteHelperInstance(getApplicationContext());
        WebSocketHandler ws = WebSocketHandler.getInstance();

        if (!dbHelper.getUserByUsername(PreferenceManagerClass.getUsername(getApplicationContext())).profileImage.isEmpty()) {
            Bitmap bitmap = BitmapFactory.decodeFile(dbHelper.getUserByUsername(PreferenceManagerClass.getUsername(getApplicationContext())).profileImage);
            profileImage.setImageBitmap(bitmap);
        }

        profileUsername.setText(dbHelper.getUserByUsername(PreferenceManagerClass.getUsername(getApplicationContext())).username + ", " +
                dbHelper.getUserByUsername(PreferenceManagerClass.getUsername(getApplicationContext())).sex + ", " +
                dbHelper.getUserByUsername(PreferenceManagerClass.getUsername(getApplicationContext())).age);
        profileBiography.setText(dbHelper.getUserByUsername(PreferenceManagerClass.getUsername(getApplicationContext())).biography);

        video1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkVideoContent(dbHelper.getUserByUsername(PreferenceManagerClass.getUsername(getApplicationContext())).video1, 1);
            }
        });
        video2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkVideoContent(dbHelper.getUserByUsername(PreferenceManagerClass.getUsername(getApplicationContext())).video2, 2);
            }
        });
        video3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkVideoContent(dbHelper.getUserByUsername(PreferenceManagerClass.getUsername(getApplicationContext())).video3, 3);
            }
        });
    }

    public void checkVideoContent(String videoURL, int videoId) {
        if (!videoURL.isEmpty()) {
            Intent i = new Intent(getApplicationContext(), VideoPlayerActivity.class);
            i.putExtra("videoId", videoId);
            startActivity(i);
        } else {
            Toast.makeText(getApplicationContext(), "There is no available video", Toast.LENGTH_LONG).show();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.profileLogOut:
                PreferenceManagerClass.clearPreferences(getApplicationContext());
                Intent logoutIntent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(logoutIntent);
                break;
            case R.id.profileCustomize:
                Intent customizeIntent = new Intent(getApplicationContext(), CustomizeProfileActivity.class);
                startActivity(customizeIntent);
                break;
            case R.id.profileFeedbackMenuItem:
                Intent feedbackIntent = new Intent(getApplicationContext(), ViewFeedbackActivity.class);
                startActivity(feedbackIntent);
                break;
        }
        return false;
    }

}
