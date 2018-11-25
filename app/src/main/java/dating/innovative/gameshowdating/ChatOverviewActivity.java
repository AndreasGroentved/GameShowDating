package dating.innovative.gameshowdating;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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

        ArrayList<Match> tempMatches = new ArrayList<>();
        ArrayList<User> matches = new ArrayList<>();
        //testdata
        for(int i = 0; i < dbHelper.getAllMatchesForUser(PreferenceManagerClass.getUsername(getApplicationContext())).size(); i++){
            matches.add(dbHelper.getUserByUsername(dbHelper.getAllMatchesForUser(PreferenceManagerClass.getUsername(getApplicationContext())).get(i).nameTwo));
        }

        recyclerView = (RecyclerView) findViewById(R.id.chatoverviewRecyclerView);
        recyclerView.setHasFixedSize(true);

        recyclerManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerManager);

        recyclerAdapter = new ChatAdapter(matches);
        recyclerView.setAdapter(recyclerAdapter);


    }
}
