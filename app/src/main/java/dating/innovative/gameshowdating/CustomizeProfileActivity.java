package dating.innovative.gameshowdating;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.jetbrains.annotations.NotNull;

public class CustomizeProfileActivity extends BaseActivity {

    EditText biographyEdit;
    Button profileImage;
    Button video1;
    Button video2;
    Button video3;
    Button saveChanges;
    SQLiteHelper dbHelper;


    @NotNull
    @Override
    public Toolbar getToolBar() {
        return findViewById(R.id.toolbar_customizeProfile);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_customizeprofile;
    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        biographyEdit = (EditText) findViewById(R.id.customizeBiography);
        video1 = (Button) findViewById(R.id.customizeVideo1);
        video2 = (Button) findViewById(R.id.customizeVideo2);
        video3 = (Button) findViewById(R.id.customizeVideo3);
        profileImage = (Button) findViewById(R.id.customizeProfileImage);
        saveChanges = (Button) findViewById(R.id.customizeSaveButton);
        dbHelper = SQLiteHelper.getSqLiteHelperInstance(getApplicationContext());

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!biographyEdit.getText().toString().isEmpty()){
                    dbHelper.updateUserBiography(dbHelper.getUserByUsername(PreferenceManagerClass.getUsername(getApplicationContext())),biographyEdit.getText().toString());
                }
                if(!PreferenceManagerClass.getProfilePictureUpdated(getApplicationContext()).isEmpty()){
                    dbHelper.updateUserProfileImage(dbHelper.getUserByUsername(PreferenceManagerClass.getUsername(getApplicationContext())),
                            PreferenceManagerClass.getProfilePictureUpdated(getApplicationContext()));
                    PreferenceManagerClass.clearRef(getApplicationContext(),PreferenceManagerClass.PREFERENCE_PROFILE_PICTURE);
                }
                if(!PreferenceManagerClass.getPreferenceVideo1(getApplicationContext()).isEmpty()){
                    dbHelper.updateUserVideo1(dbHelper.getUserByUsername(PreferenceManagerClass.getUsername(getApplicationContext())),
                            PreferenceManagerClass.getPreferenceVideo1(getApplicationContext()));
                    PreferenceManagerClass.clearRef(getApplicationContext(),PreferenceManagerClass.PREFERENCE_VIDEO_1);
                }
                if(!PreferenceManagerClass.getPreferenceVideo2(getApplicationContext()).isEmpty()){
                    dbHelper.updateUserVideo2(dbHelper.getUserByUsername(PreferenceManagerClass.getUsername(getApplicationContext())),
                            PreferenceManagerClass.getPreferenceVideo2(getApplicationContext()));
                    PreferenceManagerClass.clearRef(getApplicationContext(),PreferenceManagerClass.PREFERENCE_VIDEO_2);
                }
                if(!PreferenceManagerClass.getPreferenceVideo3(getApplicationContext()).isEmpty()){
                    dbHelper.updateUserVideo3(dbHelper.getUserByUsername(PreferenceManagerClass.getUsername(getApplicationContext())),
                            PreferenceManagerClass.getPreferenceVideo3(getApplicationContext()));
                    PreferenceManagerClass.clearRef(getApplicationContext(),PreferenceManagerClass.PREFERENCE_VIDEO_3);
                }
                Intent backToProfile = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(backToProfile);
            }
        });
        video1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transitionToVideoSettingScreen(1);
            }
        });
        video2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transitionToVideoSettingScreen(2);
            }
        });
        video3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transitionToVideoSettingScreen(3);
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),ImageSettingActivity.class);
                startActivity(i);
            }
        });

    }

    public void transitionToVideoSettingScreen(int videoId){
        Intent i = new Intent(getApplicationContext(), VideoSettingActivity.class);
        i.putExtra("videoId", videoId);
        startActivity(i);
    }


}
