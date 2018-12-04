package dating.innovative.gameshowdating.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import dating.innovative.gameshowdating.R;
import dating.innovative.gameshowdating.data.Util;
import dating.innovative.gameshowdating.data.WebSocketHandler;
import dating.innovative.gameshowdating.model.RemoteUser;
import dating.innovative.gameshowdating.util.BaseActivity;
import dating.innovative.gameshowdating.util.PreferenceManagerClass;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CustomizeProfileActivity extends BaseActivity {

    EditText biographyEdit;
    Button profileImage;
    Button video1;
    Button video2;
    Button video3;
    Button saveChanges;
    SQLiteHelper dbHelper;
    WebSocketHandler ws;
    File video1File;
    File video2File;
    File video3File;
    File profileImageFile;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ws = WebSocketHandler.getInstance();

        biographyEdit = findViewById(R.id.customizeBiography);
        video1 = findViewById(R.id.customizeVideo1);
        video2 = findViewById(R.id.customizeVideo2);
        video3 = findViewById(R.id.customizeVideo3);
        profileImage = findViewById(R.id.customizeProfileImage);
        saveChanges = findViewById(R.id.customizeSaveButton);
        dbHelper = SQLiteHelper.getSqLiteHelperInstance(getApplicationContext());
        final File storageDirVideo = getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        final File storageDirImage = getExternalFilesDir(Environment.DIRECTORY_PICTURES);


        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!biographyEdit.getText().toString().isEmpty()) {
                    dbHelper.updateUserBiography(dbHelper.getUserByUsername(PreferenceManagerClass.getUsername(getApplicationContext())), biographyEdit.getText().toString());
                    ws.updateBiography(biographyEdit.getText().toString(), new Function1<Boolean, Unit>() {
                        @Override
                        public Unit invoke(Boolean aBoolean) {
                            return null;
                        }
                    });
                }
                if (!PreferenceManagerClass.getProfilePictureUpdated(getApplicationContext()).isEmpty()) {
                    dbHelper.updateUserProfileImage(dbHelper.getUserByUsername(PreferenceManagerClass.getUsername(getApplicationContext())),
                            PreferenceManagerClass.getProfilePictureUpdated(getApplicationContext()));
                    PreferenceManagerClass.clearRef(getApplicationContext(), PreferenceManagerClass.PREFERENCE_PROFILE_PICTURE);
                    if (ImageSettingActivity.photoPath != null) {
                        ws.getUser(PreferenceManagerClass.getUsername(getApplicationContext()), new Function1<RemoteUser, Unit>() {
                            @Override
                            public Unit invoke(RemoteUser remoteUser) {
                                if (remoteUser.getProfilePicture() != null) {
                                    profileImageFile = new File(ImageSettingActivity.photoPath);
                                    dbHelper.updateUserProfileImage(dbHelper.getUserByUsername(PreferenceManagerClass.getUsername(getApplicationContext())), profileImageFile.getAbsolutePath());
                                }
                                return null;
                            }
                        });
                    } else {
                        try {
                            profileImageFile = File.createTempFile("profilePictureImage_" + PreferenceManagerClass.getUsername(getApplicationContext()), ".jpg", getExternalFilesDir(Environment.DIRECTORY_PICTURES));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        ;

                        ws.getUser(PreferenceManagerClass.getUsername(getApplicationContext()), new Function1<RemoteUser, Unit>() {
                            @Override
                            public Unit invoke(RemoteUser remoteUser) {
                                if (remoteUser.getProfilePicture() != null) {
                                    try {
                                        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(profileImageFile));
                                        bos.write(remoteUser.getProfilePicture());
                                        dbHelper.updateUserProfileImage(dbHelper.getUserByUsername(PreferenceManagerClass.getUsername(getApplicationContext())), profileImageFile.getAbsolutePath());
                                        bos.flush();
                                        bos.close();

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                return null;
                            }
                        });
                    }
                }

                if (!PreferenceManagerClass.getPreferenceVideo1(getApplicationContext()).isEmpty()) {
                    dbHelper.updateUserVideo1(dbHelper.getUserByUsername(PreferenceManagerClass.getUsername(getApplicationContext())),
                            PreferenceManagerClass.getPreferenceVideo1(getApplicationContext()));
                    PreferenceManagerClass.clearRef(getApplicationContext(), PreferenceManagerClass.PREFERENCE_VIDEO_1);
                }
                if (!PreferenceManagerClass.getPreferenceVideo2(getApplicationContext()).isEmpty()) {
                    dbHelper.updateUserVideo2(dbHelper.getUserByUsername(PreferenceManagerClass.getUsername(getApplicationContext())),
                            PreferenceManagerClass.getPreferenceVideo2(getApplicationContext()));
                    PreferenceManagerClass.clearRef(getApplicationContext(), PreferenceManagerClass.PREFERENCE_VIDEO_2);

                }
                if (!PreferenceManagerClass.getPreferenceVideo3(getApplicationContext()).isEmpty()) {
                    dbHelper.updateUserVideo3(dbHelper.getUserByUsername(PreferenceManagerClass.getUsername(getApplicationContext())),
                            PreferenceManagerClass.getPreferenceVideo3(getApplicationContext()));
                    PreferenceManagerClass.clearRef(getApplicationContext(), PreferenceManagerClass.PREFERENCE_VIDEO_3);
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
                Intent i = new Intent(getApplicationContext(), ImageSettingActivity.class);
                startActivity(i);
            }
        });

    }

    public void transitionToVideoSettingScreen(int videoId) {
        Intent i = new Intent(getApplicationContext(), VideoSettingActivity.class);
        i.putExtra("videoId", videoId);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent backToProfile = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivity(backToProfile);
    }


}
