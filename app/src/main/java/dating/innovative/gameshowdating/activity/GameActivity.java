package dating.innovative.gameshowdating.activity;

import android.app.Activity;
import android.os.Bundle;
import dating.innovative.gameshowdating.R;
import dating.innovative.gameshowdating.data.WebSocketHandler;
import dating.innovative.gameshowdating.model.Game;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class GameActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        String gameId = getIntent().getExtras().getString("gameId");
        WebSocketHandler ws = WebSocketHandler.getInstance();
        ws.confirmGame(true, gameId, new Function1<Game, Unit>() {
            @Override
            public Unit invoke(Game game) {
                
                return null;
            }
        });

    }
}
