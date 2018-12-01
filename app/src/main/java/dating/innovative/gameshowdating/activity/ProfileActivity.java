package dating.innovative.gameshowdating.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
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
import dating.innovative.gameshowdating.util.BaseActivity;
import dating.innovative.gameshowdating.util.PreferenceManagerClass;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static dating.innovative.gameshowdating.activity.ImageSettingActivity.photoPath;

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

        if(!dbHelper.getUserByUsername(PreferenceManagerClass.getUsername(getApplicationContext())).profileImage.isEmpty()){
            Uri profilePicUri = Uri.parse(dbHelper.getUserByUsername(PreferenceManagerClass.getUsername(getApplicationContext())).profileImage);
            InputStream imageStream = null;
            try {
                imageStream = getContentResolver().openInputStream(profilePicUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            if(photoPath != null) {
                ExifInterface ei = null;
                try {
                    ei = new ExifInterface(photoPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED);
                Bitmap rotatedBitmap = null;
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotatedBitmap = ImageSettingActivity.rotateImage(selectedImage, 90);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotatedBitmap = ImageSettingActivity.rotateImage(selectedImage, 180);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotatedBitmap = ImageSettingActivity.rotateImage(selectedImage, 270);
                        break;

                    case ExifInterface.ORIENTATION_NORMAL:
                    default:
                        rotatedBitmap = selectedImage;
                }
                profileImage.setImageBitmap(rotatedBitmap);
            } else {
                profileImage.setImageBitmap(selectedImage);
            }

            //There might be some slight memory allocation things here, could just be my phone being lazy though


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

    public void checkVideoContent(String videoURL, int videoId){
        if(!videoURL.isEmpty()){
            Intent i = new Intent(getApplicationContext(), VideoPlayerActivity.class);
            i.putExtra("videoId", videoId);
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
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
                break;
            case R.id.profileFeedbackMenuItem:
                Intent feedbackIntent = new Intent(getApplicationContext(), ViewFeedbackActivity.class);
                startActivity(feedbackIntent);
                break;
        }
        return false;
    }

    @Override
    public void onBackPressed(){
        Intent backToMenu = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(backToMenu);
        super.onBackPressed();
    }
}
