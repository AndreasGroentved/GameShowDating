package dating.innovative.gameshowdating.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;
import dating.innovative.gameshowdating.R;
import dating.innovative.gameshowdating.data.WebSocketHandler;
import dating.innovative.gameshowdating.model.Game;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

import java.io.File;

public class GameJudgingActivity extends Activity {

    TextView userBio;
    TextView remainingUsers;
    Button outButton;
    VideoView userVideos;
    int usersRemaining;
    String gameId;
    WebSocketHandler ws;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_judging);
        gameId = getIntent().getStringExtra("gameId");

        ws = WebSocketHandler.getInstance();
        ws.confirmGame(true, gameId, new Function1<Game, Unit>() {
            @Override
            public Unit invoke(Game game) {
                return null;
            }
        });

        userBio = (TextView) findViewById(R.id.gameJudgingUserBio);
        remainingUsers = (TextView) findViewById(R.id.gameJudgingRemainingUsers);
        outButton = (Button) findViewById(R.id.gameJudgingOutButton);
        userVideos = (VideoView) findViewById(R.id.gameJudgingVideoView);
        usersRemaining = 5;

        remainingUsers.setText(remainingUsers.getText().toString() + " " + usersRemaining);

        outButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent outIntent = new Intent(getApplicationContext(), ProvideFeedbackActivity.class);
                outIntent.putExtra("gameId", gameId);
                startActivity(outIntent);
            }
        });

        //get video1 however the fuck that works xD
        //ws.getVideo();

    }
}
