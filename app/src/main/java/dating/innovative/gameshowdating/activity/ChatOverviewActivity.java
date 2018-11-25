package dating.innovative.gameshowdating.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import dating.innovative.gameshowdating.R;
import dating.innovative.gameshowdating.model.Match;
import dating.innovative.gameshowdating.model.User;
import dating.innovative.gameshowdating.util.ChatOverviewAdapter;
import dating.innovative.gameshowdating.util.PreferenceManagerClass;

import java.util.ArrayList;

public class ChatOverviewActivity extends Activity {

    RecyclerView recyclerView;
    RecyclerView.Adapter recyclerAdapter;
    RecyclerView.LayoutManager recyclerManager;
    SQLiteHelper dbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatoverview);

        dbHelper = SQLiteHelper.getSqLiteHelperInstance(getApplicationContext());

        //dbHelper.addMatch(PreferenceManagerClass.getUsername(getApplicationContext()), "test");

        ArrayList<User> matches = new ArrayList<>();
        for(int i = 0; i < dbHelper.getAllMatchesForUser(PreferenceManagerClass.getUsername(getApplicationContext())).size(); i++){
            matches.add(dbHelper.getUserByUsername(dbHelper.getAllMatchesForUser(PreferenceManagerClass.getUsername(getApplicationContext())).get(i).nameTwo));
        }

        recyclerView = (RecyclerView) findViewById(R.id.chatoverviewRecyclerView);
        recyclerView.setHasFixedSize(true);

        recyclerManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerManager);

        recyclerAdapter = new ChatOverviewAdapter(matches);
        recyclerView.setAdapter(recyclerAdapter);


    }
}
