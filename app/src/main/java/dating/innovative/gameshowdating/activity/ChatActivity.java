package dating.innovative.gameshowdating.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;
import dating.innovative.gameshowdating.R;
import dating.innovative.gameshowdating.util.ChatAdapter;

public class ChatActivity extends Activity {

    RecyclerView messagesView;
    RecyclerView.Adapter messagesAdapter;
    RecyclerView.LayoutManager messagesLayoutManager;
    EditText writeMessageEditText;
    Button sendButton;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messagesView = (RecyclerView) findViewById(R.id.chatRecyclerView);
        messagesLayoutManager = new LinearLayoutManager(this);
        messagesView.setLayoutManager(messagesLayoutManager);
        messagesAdapter = new ChatAdapter();
        messagesView.setAdapter(messagesAdapter);

        writeMessageEditText = (EditText) findViewById(R.id.chatEditText);
        sendButton = (Button) findViewById(R.id.chatSendMessageButton);
    }
}
