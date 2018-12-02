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

import org.jetbrains.annotations.NotNull;

import dating.innovative.gameshowdating.R;
import dating.innovative.gameshowdating.data.WebSocketHandler;
import dating.innovative.gameshowdating.model.RemoteUser;
import dating.innovative.gameshowdating.util.BaseActivity;
import dating.innovative.gameshowdating.util.PreferenceManagerClass;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class LoginActivity extends BaseActivity {

    EditText usernameTextField;
    EditText passwordTextField;
    Button loginButton;
    Button registerButton;
    TextView errorLabel;
    SQLiteHelper dbHelper;
    WebSocketHandler ws = WebSocketHandler.getInstance();


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

        if (!PreferenceManagerClass.getUsername(getApplicationContext()).isEmpty() &&
                dbHelper.getUserByUsername(PreferenceManagerClass.getUsername(getApplicationContext())) != null) {
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

    private void loginUser(String username, String password) {
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
                ws.getUser(usernameTextField.getText().toString(), new Function1<RemoteUser, Unit>() {
                    @Override
                    public Unit invoke(RemoteUser user) {
                        System.out.println(user);
                        PreferenceManagerClass.setUsername(getApplicationContext(), user.get_id());
                        Intent i = new Intent(getApplicationContext(), MenuActivity.class);
                        startActivity(i);
                        LoginActivity.this.finish();
                        return null;
                    }
                });
                return null;
            }
        });
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
