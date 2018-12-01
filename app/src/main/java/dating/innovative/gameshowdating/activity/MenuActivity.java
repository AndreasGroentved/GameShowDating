package dating.innovative.gameshowdating.activity;

import android.app.Activity;
import android.content.Intent;
import android.icu.text.SymbolTable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import dating.innovative.gameshowdating.R;
import dating.innovative.gameshowdating.data.WebSocketHandler;
import dating.innovative.gameshowdating.model.Message;
import dating.innovative.gameshowdating.util.PreferenceManagerClass;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

import java.util.List;
import java.util.Map;

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

        WebSocketHandler ws = WebSocketHandler.getInstance();
       /* ws.sendChatMessage("hest", "yo dog, wanna hook up?", System.currentTimeMillis());
        ws.sendChatMessage("yoloMutter", "sup?", System.currentTimeMillis());
       */ ws.getMessages(PreferenceManagerClass.getUsername(getApplicationContext()), new Function1<Map<String, ? extends List<? extends Message>>, Unit>() {
            @Override
            public Unit invoke(Map<String, ? extends List<? extends Message>> stringMap) {
                System.out.println(stringMap);
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
