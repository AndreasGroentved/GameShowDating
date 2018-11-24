package dating.innovative.gameshowdating;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends Activity {

    Button profileButton;
    Button gameButton;
    Button chatButton;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        profileButton = (Button) findViewById(R.id.mainMenuProfileButton);
        gameButton = (Button) findViewById(R.id.mainMenuGameButton);
        chatButton = (Button) findViewById(R.id.mainMenuChatButton);

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
                Intent gameIntent = new Intent(getApplicationContext(), GamehubActivity.class);
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
