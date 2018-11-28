package dating.innovative.gameshowdating.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import dating.innovative.gameshowdating.R;
import dating.innovative.gameshowdating.model.User;
import dating.innovative.gameshowdating.util.BaseActivity;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class RegisterActivity extends BaseActivity {

    EditText registerUsername;
    EditText registerPassword;
    EditText registerConfirmPassword;
    TextView errorLabel;
    Button registerUserButton;
    RadioButton radioRegisterButtonMale;
    RadioButton radioRegisterButtonFemale;
    EditText registerAge;
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
        radioRegisterButtonMale = (RadioButton) findViewById(R.id.radio_male);
        radioRegisterButtonFemale = (RadioButton) findViewById(R.id.radio_female);
        registerAge = (EditText) findViewById(R.id.ageEditText);

        registerUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!registerUsername.getText().toString().isEmpty() &&
                        !registerPassword.getText().toString().isEmpty() &&
                        !registerAge.getText().toString().isEmpty() &&
                        registerConfirmPassword.getText().toString().equals(registerPassword.getText().toString())) {
                    if (dbHelper.getUserByUsername(registerUsername.getText().toString()) == null) {
                        User newUser = null;
                        if(radioRegisterButtonFemale.isChecked()){
                            newUser = createNewUser(registerUsername.getText().toString(), registerPassword.getText().toString(), radioRegisterButtonFemale.getText().toString(),  Integer.parseInt(registerAge.getText().toString()));
                            createRemoteUser(newUser);
                        } else if(radioRegisterButtonMale.isChecked()){
                            newUser = createNewUser(registerUsername.getText().toString(),registerPassword.getText().toString(),radioRegisterButtonMale.getText().toString(), Integer.parseInt(registerAge.getText().toString()));
                            createRemoteUser(newUser);
                        } else {
                            errorLabel.setText("A sex must be chosen");
                        }
                        if(newUser != null){
                            dbHelper.addUser(newUser);
                            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(i);
                        }

                    } else {
                        errorLabel.setText("A user with that username already exists");
                    }
                } else {
                    if (registerUsername.getText().toString().isEmpty()) {
                        errorLabel.setText("Username can not be empty");
                    } else if (registerPassword.getText().toString().isEmpty()) {
                        errorLabel.setText("Password can not be empty");
                    } else if(registerAge.getText().toString().isEmpty()){
                        errorLabel.setText("An age must be set");
                    } else if (!registerConfirmPassword.getText().toString().equals(registerPassword.getText().toString())) {
                        errorLabel.setText("Passwords do not match");
                    }
                }
            }
        });
    }

    public void onRadioButtonClicked(View view){
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()){
            case R.id.radio_male:
                if(checked){
                    radioRegisterButtonMale.setChecked(true);
                    radioRegisterButtonFemale.setChecked(false);
                }
                break;
            case R.id.radio_female:
                if(checked){
                    radioRegisterButtonFemale.setChecked(true);
                    radioRegisterButtonMale.setChecked(false);
                }
        }
    }

    public User createNewUser(String username, String password, String sex, int age) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setSex(sex);
        user.setAge(age);
        user.setBiography("No biography was set by the user");
        user.setProfileImage("");
        user.setVideo1("");
        user.setVideo2("");
        user.setVideo3("");
        return user;
    }

    public void createRemoteUser(User user){
        LoginActivity.ws.createUser(user, new Function1<Boolean, Unit>() {
            @Override
            public Unit invoke(Boolean aBoolean) {
                System.out.println("user created" + aBoolean);
                return null;
            }
        });
    }
}
