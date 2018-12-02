package dating.innovative.gameshowdating.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import dating.innovative.gameshowdating.R;
import dating.innovative.gameshowdating.data.WebSocketHandler;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class MenuActivity extends Activity {

    Button profileButton;
    Button gameButton;
    Button chatButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        profileButton = findViewById(R.id.mainMenuProfileButton);
        gameButton = findViewById(R.id.mainMenuGameButton);
        chatButton = findViewById(R.id.mainMenuChatButton);

        WebSocketHandler.getInstance().getVideo("test1", 1, new Function1<byte[], Unit>() {
            @Override
            public Unit invoke(byte[] bytes) {

                return null;
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profileIntent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(profileIntent);
            }
        });

        gameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gameIntent = new Intent(getApplicationContext(), FindGameActivity.class);
                startActivity(gameIntent);
            }
        });

        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chatIntent = new Intent(getApplicationContext(), ChatOverviewActivity.class);
                startActivity(chatIntent);
            }
        });
    }


}
