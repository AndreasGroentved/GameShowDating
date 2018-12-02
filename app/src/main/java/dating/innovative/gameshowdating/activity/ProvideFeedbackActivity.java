package dating.innovative.gameshowdating.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import dating.innovative.gameshowdating.R;
import dating.innovative.gameshowdating.data.WebSocketHandler;

public class ProvideFeedbackActivity extends Activity {

    EditText writeFeedbackEditText;
    Button sendFeedbackButton;
    Button skipButton;
    WebSocketHandler ws;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provide_feedback);

        writeFeedbackEditText = (EditText) findViewById(R.id.provideFeedbackEditText);
        sendFeedbackButton = (Button) findViewById(R.id.provideFeedbackSendButton);
        skipButton = (Button) findViewById(R.id.provideFeedbackDontSendFeedbackButton);
        ws = WebSocketHandler.getInstance();

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
                if(!writeFeedbackEditText.getText().toString().isEmpty()){
                       ws.comment(getIntent().getStringExtra("gameId"), writeFeedbackEditText.getText().toString());
                       Intent i = new Intent(getApplicationContext(), MenuActivity.class);
                       startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "Please write some feedback before sending", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
