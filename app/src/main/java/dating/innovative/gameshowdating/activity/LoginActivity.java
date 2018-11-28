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

        System.out.println("what?");
        final WebSocketHandler ws = new WebSocketHandler();
        /*User user = new User();
        user.username = "super human";
        user.age = 123;
        user.sex = "female";
        user.password = "123";
        ws.createUser(user, new Function1<Boolean, Unit>() {
                    @Override
                    public Unit invoke(Boolean aBoolean) {
                        System.out.println("success " + aBoolean);
                        return null;
                    }
                }
        );*/

        ws.logon("super human", "123", new Function1<Boolean, Unit>() {
            @Override
            public Unit invoke(Boolean aBoolean) {
                System.out.println("logon " + aBoolean);

              /*  ws.updateBiography("I am Unit", new Function1<Boolean, Unit>() {
                    @Override
                    public Unit invoke(Boolean aBoolean) {
                        System.out.println("updated " + aBoolean);
                        return null;
                    }
                });*/

                ws.getUser("super human", new Function1<User, Unit>() {
                    @Override
                    public Unit invoke(User user) {
                        System.out.println(user);
                        return null;
                    }
                });
                return null;
            }
        });


        dbHelper = SQLiteHelper.getSqLiteHelperInstance(getApplicationContext());

        if (!PreferenceManagerClass.getUsername(getApplicationContext()).isEmpty() &&
                dbHelper.getUserByUsername(PreferenceManagerClass.getUsername(getApplicationContext())) != null) {

            /* WebSocketHandler.getInstance().logon(PreferenceManagerClass.getUsername(this), dbHelper.getUserByUsername(PreferenceManagerClass.getUsername(this)).password, new Function1<Boolean, Unit>() {
                @Override
                public Unit invoke(Boolean aBoolean) {

                    return null;
                }
            });*/
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
