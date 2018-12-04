package dating.innovative.gameshowdating.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import dating.innovative.gameshowdating.R;

public class MenuActivity extends Activity {

    private Button profileButton;
    private Button gameButton;
    private Button chatButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        profileButton = findViewById(R.id.mainMenuProfileButton);
        gameButton = findViewById(R.id.mainMenuGameButton);
        chatButton = findViewById(R.id.mainMenuChatButton);

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
