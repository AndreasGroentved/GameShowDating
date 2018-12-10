package dating.innovative.gameshowdating.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import dating.innovative.gameshowdating.R;
import dating.innovative.gameshowdating.data.WebSocketHandler;
import dating.innovative.gameshowdating.model.Message;
import dating.innovative.gameshowdating.model.RemoteUser;
import dating.innovative.gameshowdating.util.ChatOverviewAdapter;
import dating.innovative.gameshowdating.util.PreferenceManagerClass;
import dating.innovative.gameshowdating.util.SQLiteHelper;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*  Andreas Jeppesen - ajepp09@student.sdu.dk
    Emil Jensen - emije14@student.sdu.dk
    Nicolai Jensen - nije214@student.sdu.dk */

public class ChatOverviewActivity extends Activity {

    RecyclerView recyclerView;
    ChatOverviewAdapter recyclerAdapter;
    RecyclerView.LayoutManager recyclerManager;
    SQLiteHelper dbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatoverview);

        dbHelper = SQLiteHelper.getSqLiteHelperInstance(getApplicationContext());

        //dbHelper.addMatch(PreferenceManagerClass.getUsername(getApplicationContext()), "test");

        List<RemoteUser> matches = new ArrayList<>();
       /* if(dbHelper.getAllMatchesForUser(PreferenceManagerClass.getUsername(getApplicationContext())) != null){
            for(int i = 0; i < dbHelper.getAllMatchesForUser(PreferenceManagerClass.getUsername(getApplicationContext())).size(); i++){
                matches.add(dbHelper.getUserByUsername(dbHelper.getAllMatchesForUser(PreferenceManagerClass.getUsername(getApplicationContext())).get(i).nameTwo));
            }
        }*/


        recyclerView = (RecyclerView) findViewById(R.id.chatoverviewRecyclerView);
        recyclerView.setHasFixedSize(true);

        recyclerManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerManager);

        recyclerAdapter = new ChatOverviewAdapter(new ArrayList<RemoteUser>());
        recyclerView.setAdapter(recyclerAdapter);
        getData();

    }

    private void getData() {
        String username = PreferenceManagerClass.getUsername(this);
        final WebSocketHandler ws = WebSocketHandler.getInstance();
        ws.getMessages(username, new Function1<Map<String, ? extends List<? extends Message>>, Unit>() {
            @Override
            public Unit invoke(Map<String, ? extends List<? extends Message>> stringMap) {
                for (Map.Entry<String, ? extends List<? extends Message>> entry : stringMap.entrySet()) {
                    System.out.println("user " + entry.getKey());
                    ws.getUser(entry.getKey(), new Function1<RemoteUser, Unit>() {
                        @Override
                        public Unit invoke(RemoteUser remoteUser) {
                            recyclerAdapter.userDataSet.add(remoteUser);
                            ChatOverviewActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    recyclerAdapter.notifyDataSetChanged();
                                }
                            });
                            return null;
                        }
                    });
                }
                return null;
            }
        });
    }
}
