package dating.innovative.gameshowdating.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import dating.innovative.gameshowdating.R;
import dating.innovative.gameshowdating.data.WebSocketHandler;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class InQueueActivity extends Activity {

    TextView sexPreferenceQueueTextView;
    TextView rolePreferenceQueueTextView;
    TextView timeElapsedQueueTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_queue);
        final long start = System.currentTimeMillis();
        Intent lastActivityIntent = getIntent();

        WebSocketHandler ws = WebSocketHandler.getInstance();


        sexPreferenceQueueTextView = findViewById(R.id.inQueueSexTextField);
        rolePreferenceQueueTextView = findViewById(R.id.inQueueRoleTextField);
        timeElapsedQueueTextView = findViewById(R.id.inQueueTimePassedTextField2);

        boolean maleCheck = lastActivityIntent.getBooleanExtra("maleCheckBox", false);
        boolean femaleCheck = lastActivityIntent.getBooleanExtra("femaleCheckBox", false);
        final boolean beJudgedCheck = lastActivityIntent.getBooleanExtra("beJudged", false);
        final boolean judgeCheck = lastActivityIntent.getBooleanExtra("judge", false);


        final Activity a = this;
        ws.match(judgeCheck, new Function1<String, Unit>() {
            @Override
            public Unit invoke(String aString) {
                System.out.println("in queueu " + aString);
                return null;
            }
        }, new Function1<String, Unit>() {
            @Override
            public Unit invoke(String gameID) {
                if (beJudgedCheck) {
                    Intent i = new Intent(a, GameBeingJudgedActivity.class);
                    i.putExtra("gameId", gameID);
                    a.startActivity(i);
                } else if (judgeCheck) {
                    Intent i = new Intent(a, GameJudgingActivity.class);
                    i.putExtra("gameId", gameID);
                    a.startActivity(i);
                }
                return null;

            }
        });

        if (maleCheck && femaleCheck) {
            sexPreferenceQueueTextView.setText("Male and Female users");
        } else if (maleCheck) {
            sexPreferenceQueueTextView.setText("Male users");
        } else if (femaleCheck) {
            sexPreferenceQueueTextView.setText("Female users");
        }

        if (beJudgedCheck) {
            rolePreferenceQueueTextView.setText("Getting judged");
        } else if (judgeCheck) {
            rolePreferenceQueueTextView.setText("Judging a user");
        }

        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!interrupted()) {
                        Thread.sleep(1000);
                        final long now = System.currentTimeMillis();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                timeElapsedQueueTextView.setText((now - start) / 1000 + " seconds");
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }
}
