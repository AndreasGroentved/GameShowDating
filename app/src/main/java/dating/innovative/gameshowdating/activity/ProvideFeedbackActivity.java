package dating.innovative.gameshowdating.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import dating.innovative.gameshowdating.R;
import dating.innovative.gameshowdating.data.WebSocketHandler;
import dating.innovative.gameshowdating.util.SQLiteHelper;

/*  Andreas Jeppesen - ajepp09@student.sdu.dk
    Emil Jensen - emije14@student.sdu.dk
    Nicolai Jensen - nije214@student.sdu.dk */

public class ProvideFeedbackActivity extends Activity {

    EditText writeFeedbackEditText;
    Button sendFeedbackButton;
    Button skipButton;
    WebSocketHandler ws;
    SQLiteHelper dbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provide_feedback);

        writeFeedbackEditText = findViewById(R.id.provideFeedbackEditText);
        sendFeedbackButton = findViewById(R.id.provideFeedbackSendButton);
        skipButton = findViewById(R.id.provideFeedbackDontSendFeedbackButton);
        ws = WebSocketHandler.getInstance();
        dbHelper = SQLiteHelper.getSqLiteHelperInstance(getApplicationContext());

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(i);
            }
        });

        sendFeedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!writeFeedbackEditText.getText().toString().isEmpty()) {
                    ws.comment(getIntent().getStringExtra("username"), writeFeedbackEditText.getText().toString(), getIntent().getLongExtra("timeStamp", 0), getIntent().getIntExtra("roundnumber", -1111));
                    dbHelper.addFeedback(getIntent().getStringExtra("username"), writeFeedbackEditText.getText().toString());
                    Intent i = new Intent(getApplicationContext(), MenuActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "Please write some text before sending", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
