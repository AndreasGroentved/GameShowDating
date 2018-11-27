package dating.innovative.gameshowdating.util;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.RelativeLayout;
import android.widget.TextView;
import dating.innovative.gameshowdating.R;
import dating.innovative.gameshowdating.model.Message;


import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private Context context;
    private ArrayList<Message> messages;

    public ChatAdapter(ArrayList<Message> messages){
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
        Message message = messages.get(position);
        TextView messageTextView = holder.message;
        if(message.getMessageFromSelf() != null){
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)messageTextView.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            messageTextView.setTextSize(18);
            messageTextView.setLayoutParams(params);
            messageTextView.setBackgroundResource(R.drawable.message_bubble_own);
            messageTextView.setText(message.messageFromSelf);
        } else {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)messageTextView.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            messageTextView.setTextSize(18);
            messageTextView.setLayoutParams(params);
            messageTextView.setBackgroundResource(R.drawable.message_bubble_match);
            messageTextView.setText(message.messageFromMatch);
        }

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
