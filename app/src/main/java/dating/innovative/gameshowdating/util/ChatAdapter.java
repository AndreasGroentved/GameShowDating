package dating.innovative.gameshowdating.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import dating.innovative.gameshowdating.R;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private Context context;
    private ArrayList<String> messages;

    public ChatAdapter(ArrayList<String> messages){
        this.messages = messages;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View messageView = layoutInflater.inflate(R.layout.chat_message_view, parent, false);
        ChatViewHolder chatViewHolder = new ChatViewHolder(messageView);
        return chatViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        String message = messages.get(position);
        TextView messageTextView = holder.message;
        messageTextView.setText(message);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder{

        public TextView message;

        public ChatViewHolder(View itemView) {
            super(itemView);

            this.message = itemView.findViewById(R.id.chatMessage);
        }
    }
}
