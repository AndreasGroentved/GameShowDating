package dating.innovative.gameshowdating.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import dating.innovative.gameshowdating.R;
import dating.innovative.gameshowdating.data.Util;
import dating.innovative.gameshowdating.data.WebSocketHandler;
import dating.innovative.gameshowdating.model.RemoteUser;
import dating.innovative.gameshowdating.util.BaseActivity;
import dating.innovative.gameshowdating.util.PreferenceManagerClass;
import dating.innovative.gameshowdating.util.SQLiteHelper;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import org.jetbrains.annotations.NotNull;

public class LoginActivity extends BaseActivity {

    EditText usernameTextField;
    EditText passwordTextField;
    Button loginButton;
    Button registerButton;
    TextView errorLabel;
    SQLiteHelper dbHelper;
    WebSocketHandler ws = WebSocketHandler.getInstance();
    //final File storageDirImage = getExternalFilesDir(Environment.DIRECTORY_PICTURES);


    @NotNull
    @Override
    public Toolbar getToolBar() {
        return findViewById(R.id.toolbar_login);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void setUpToolBar() {
        setSupportActionBar(getToolBar());
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = SQLiteHelper.getSqLiteHelperInstance(getApplicationContext());

        if (!PreferenceManagerClass.getUsername(getApplicationContext()).isEmpty()) {
            loginUser(dbHelper.getUserByUsername(PreferenceManagerClass.getUsername(getApplicationContext())).username,
                    dbHelper.getUserByUsername(PreferenceManagerClass.getUsername(getApplicationContext())).password);
        }

        usernameTextField = findViewById(R.id.usernameEditText);
        passwordTextField = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        errorLabel = findViewById(R.id.errorLoginTextView);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser(usernameTextField.getText().toString(),
                        passwordTextField.getText().toString());
            }
        });
        checkForPermissions();

    }

    private void loginUser(final String username, String password) {
        System.out.println("logon " + username + ", " + password);
        ws.logon(username, password, new Function1<Boolean, Unit>() {
            @Override
            public Unit invoke(final Boolean aBoolean) {
                if (!aBoolean) {
                    LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            errorLabel.setText("Incorrect password");
                        }
                    });
                    return null;
                }
                ws.getUser(username, new Function1<RemoteUser, Unit>() {
                    @Override
                    public Unit invoke(RemoteUser user) {
                        System.out.println("user");
                        setUserData(user);
                        Intent i = new Intent(getApplicationContext(), MenuActivity.class);
                        startActivity(i);
                        LoginActivity.this.finish();
                        //Util.scheduleJob(LoginActivity.this);
                        return null;
                    }
                });
                return null;
            }
        });
    }

    private void setUserData(RemoteUser user) {

/*

        try {
            File.createTempFile("profilePictureUpload_" + user.get_id(), ".jpg", storageDirImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        File file = Util.createProfilePicture(this);

        if (file != null) {
            Intent snapPictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri uri = FileProvider.getUriForFile(getApplicationContext(), "dating.innovative.gameshowdating", file);
            snapPictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(snapPictureIntent, 1);
        }
*/

   /*     dbHelper.updateUserProfileImage(dbHelper.getUserByUsername(PreferenceManagerClass.getUsername(getApplicationContext())),
                PreferenceManagerClass.getProfilePictureUpdated(getApplicationContext()));*/
        PreferenceManagerClass.setUsername(getApplicationContext(), user.get_id());
        if (dbHelper.getUserByUsername(PreferenceManagerClass.getUsername(getApplicationContext())) == null) {
            dbHelper.addUser(Util.remoteUserToUser(user));
        }


    }

    private void checkForPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    123);
        }
    }

}
