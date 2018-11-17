package dating.innovative.gameshowdating;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity {

    EditText usernameTextField;
    EditText passwordTextField;
    Button loginButton;
    Button registerButton;
    TextView errorLabel;
    SQLiteHelper dbHelper;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = SQLiteHelper.getSqLiteHelperInstance(getApplicationContext());

        usernameTextField = (EditText) findViewById(R.id.usernameEditText);
        passwordTextField = (EditText) findViewById(R.id.passwordEditText);
        loginButton = (Button) findViewById(R.id.loginButton);
        registerButton = (Button) findViewById(R.id.registerButton);
        errorLabel = (TextView) findViewById(R.id.errorLoginTextView);

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
                if(user != null){
                    System.out.println(user.toString());
                    if(user.password.equals(passwordTextField.getText().toString())) {
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
