package dating.innovative.gameshowdating.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import dating.innovative.gameshowdating.R;

public class MenuActivity extends Activity {

    Button profileButton;
    Button gameButton;
    Button chatButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        profileButton = (Button) findViewById(R.id.mainMenuProfileButton);
        gameButton = (Button) findViewById(R.id.mainMenuGameButton);
        chatButton = (Button) findViewById(R.id.mainMenuChatButton);

       /* WebSocketHandler ws = WebSocketHandler.getInstance();
        ws.createUser(new User("test1", "1", "male", 1), new Function1<Boolean, Unit>() {
            @Override
            public Unit invoke(Boolean aBoolean) {
                return null;
            }
        });

        ws.createUser(new User("test2", "1", "male", 1), new Function1<Boolean, Unit>() {
            @Override
            public Unit invoke(Boolean aBoolean) {
                return null;
            }
        });*/


       /* ws.sendChatMessage("test1", "yo dog, wanna hook up?", System.currentTimeMillis());
        ws.sendChatMessage("test2", "sup?", System.currentTimeMillis());
        */

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
