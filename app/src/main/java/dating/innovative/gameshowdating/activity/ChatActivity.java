package dating.innovative.gameshowdating.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import dating.innovative.gameshowdating.R;
import dating.innovative.gameshowdating.model.Message;
import dating.innovative.gameshowdating.util.ChatAdapter;
import dating.innovative.gameshowdating.util.PreferenceManagerClass;

import java.util.ArrayList;
import java.util.Collections;

public class ChatActivity extends Activity {

    RecyclerView messagesView;
    RecyclerView.Adapter messagesAdapter;
    RecyclerView.LayoutManager messagesLayoutManager;
    EditText writeMessageEditText;
    Button sendButton;
    SQLiteHelper dbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        dbHelper = SQLiteHelper.getSqLiteHelperInstance(getApplicationContext());
        final Intent intentFromLast = getIntent();

        final ArrayList<Message> messages = new ArrayList<>();
        /*dbHelper.addMessageToConversationFromSelf(PreferenceManagerClass.getUsername(getApplicationContext()),
                intentFromLast.getStringExtra("username"), "this is a message from self");
        dbHelper.addMessageToConversationFromMatch(PreferenceManagerClass.getUsername(getApplicationContext()),
                intentFromLast.getStringExtra("username"), "this is a message from match");*/

        if(dbHelper.getMessagesForConversation(PreferenceManagerClass.getUsername(getApplicationContext()), intentFromLast.getStringExtra("username")) != null){
            for(int i = 0; i < dbHelper.getMessagesForConversation(PreferenceManagerClass.getUsername(getApplicationContext()),
                    intentFromLast.getStringExtra("username")).size(); i++){
                messages.add(dbHelper.getMessagesForConversation(PreferenceManagerClass.getUsername(getApplicationContext()),
                        intentFromLast.getStringExtra("username")).get(i));
            }
        }
        Collections.reverse(messages);

        messagesView = (RecyclerView) findViewById(R.id.chatRecyclerView);
        messagesLayoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) messagesLayoutManager).setReverseLayout(true);
        messagesView.setLayoutManager(messagesLayoutManager);
        messagesAdapter = new ChatAdapter(messages);
        messagesView.setAdapter(messagesAdapter);

        writeMessageEditText = (EditText) findViewById(R.id.chatEditText);
        sendButton = (Button) findViewById(R.id.chatSendMessageButton);
        messagesView.scrollToPosition(0);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!writeMessageEditText.getText().toString().isEmpty()){
                    dbHelper.addMessageToConversationFromSelf(PreferenceManagerClass.getUsername(getApplicationContext()),
                            intentFromLast.getStringExtra("username"), writeMessageEditText.getText().toString());
                    messages.add(0,new Message(PreferenceManagerClass.getUsername(getApplicationContext()), intentFromLast.getStringExtra("username"),
                            writeMessageEditText.getText().toString(),null));
                    messagesView.getAdapter().notifyDataSetChanged();
                    messagesView.scrollToPosition(0);
                    writeMessageEditText.setText("");
                }
            }
        });
    }
}
