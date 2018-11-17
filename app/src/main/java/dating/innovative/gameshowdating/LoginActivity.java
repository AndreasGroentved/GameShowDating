package dating.innovative.gameshowdating;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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


    public void onCreate(Bundle savedInstanceState) {
        System.out.println("yo");
        super.onCreate(savedInstanceState);
        System.out.println("yo2");

        dbHelper = SQLiteHelper.getSqLiteHelperInstance(getApplicationContext());

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
                    System.out.println(user.toString());
                    if (user.password.equals(passwordTextField.getText().toString())) {
                        Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
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
