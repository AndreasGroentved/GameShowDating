package dating.innovative.gameshowdating.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import dating.innovative.gameshowdating.R;
import dating.innovative.gameshowdating.data.WebSocketHandler;
import dating.innovative.gameshowdating.model.User;
import dating.innovative.gameshowdating.util.BaseActivity;
import dating.innovative.gameshowdating.util.PreferenceManagerClass;
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
    final static WebSocketHandler ws = WebSocketHandler.getInstance();


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

            ws.logon(dbHelper.getUserByUsername(PreferenceManagerClass.getUsername(getApplicationContext())).username,
                    dbHelper.getUserByUsername(PreferenceManagerClass.getUsername(getApplicationContext())).password,
                    new Function1<Boolean, Unit>() {
                        @Override
                        public Unit invoke(Boolean aBoolean) {
                            ws.getUser(dbHelper.getUserByUsername(PreferenceManagerClass.getUsername(getApplicationContext())).username, new Function1<User, Unit>() {
                                @Override
                                public Unit invoke(User user) {
                                    System.out.println(user);
                                    return null;
                                }
                            });
                            return null;
                        }
                    });

            Intent i = new Intent(getApplicationContext(), MenuActivity.class);
            startActivity(i);
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
                User user = dbHelper.getUserByUsername(usernameTextField.getText().toString());
                if (user != null) {
                    if (user.password.equals(passwordTextField.getText().toString())) {
                        PreferenceManagerClass.setUsername(getApplicationContext(), user.username);

                        ws.logon(dbHelper.getUserByUsername(PreferenceManagerClass.getUsername(getApplicationContext())).username,
                                dbHelper.getUserByUsername(PreferenceManagerClass.getUsername(getApplicationContext())).password,
                                new Function1<Boolean, Unit>() {
                                    @Override
                                    public Unit invoke(Boolean aBoolean) {
                                        ws.getUser(dbHelper.getUserByUsername(PreferenceManagerClass.getUsername(getApplicationContext())).username, new Function1<User, Unit>() {
                                            @Override
                                            public Unit invoke(User user) {
                                                System.out.println(user);
                                                return null;
                                            }
                                        });
                                        return null;
                                    }
                                });

                        Intent i = new Intent(getApplicationContext(), MenuActivity.class);
                        startActivity(i);
                    } else {
                        errorLabel.setText("Incorrect password");
                    }
                } else {
                    errorLabel.setText("No user with that name exists");
                }
            }
        });
    }


}
