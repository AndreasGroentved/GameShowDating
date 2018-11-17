package dating.innovative.gameshowdating;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends BaseActivity {

    EditText registerUsername;
    EditText registerPassword;
    EditText registerConfirmPassword;
    TextView errorLabel;
    Button registerUserButton;
    SQLiteHelper dbHelper;


    @Override
    public int getLayout() {
        return R.layout.activity_register;
    }

    @NonNull
    @Override
    public Toolbar getToolBar() {
        return findViewById(R.id.toolbar_register);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        dbHelper = SQLiteHelper.getSqLiteHelperInstance(getApplicationContext());

        registerUserButton = (Button) findViewById(R.id.registerRegisterButton);
        registerUsername = (EditText) findViewById(R.id.usernameRegisterEditText);
        registerPassword = (EditText) findViewById(R.id.passwordRegisterEditText);
        registerConfirmPassword = (EditText) findViewById(R.id.confirmPasswordEditText);
        errorLabel = (TextView) findViewById(R.id.errorRegisterTextLabel);

        registerUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!registerUsername.getText().toString().isEmpty() &&
                        !registerPassword.getText().toString().isEmpty() &&
                        registerConfirmPassword.getText().toString().equals(registerPassword.getText().toString())) {
                    if (dbHelper.getUserByUsername(registerUsername.getText().toString()) == null) {
                        User newUser = createNewUser(registerUsername.getText().toString(), registerPassword.getText().toString());
                        dbHelper.addUser(newUser);
                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(i);
                    } else {
                        errorLabel.setText("A user with that username already exists");
                    }
                } else {
                    if (registerUsername.getText().toString().isEmpty()) {
                        errorLabel.setText("Username can not be empty");
                    } else if (registerPassword.getText().toString().isEmpty()) {
                        errorLabel.setText("Password can not be empty");
                    } else if (!registerConfirmPassword.getText().toString().equals(registerPassword.getText().toString())) {
                        errorLabel.setText("Passwords do not match");
                    }
                }
            }
        });
    }

    public User createNewUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setBiography("No biography was set by the user");
        user.setProfileImage("");
        user.setVideo1("");
        user.setVideo2("");
        user.setVideo3("");
        return user;
    }
}
