package dating.innovative.gameshowdating.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import dating.innovative.gameshowdating.R;
import dating.innovative.gameshowdating.model.Message;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    public List<Message> messages;

    public ChatAdapter(List<Message> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View messageView = layoutInflater.inflate(R.layout.chat_message_view, parent, false);
        return new ChatViewHolder(messageView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Message message = messages.get(position);
        TextView messageTextView = holder.message;
        if (message.getMessageFromSelf() != null) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) messageTextView.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            messageTextView.setTextSize(18);
            messageTextView.setLayoutParams(params);
            messageTextView.setBackgroundResource(R.drawable.message_bubble_own);
            messageTextView.setText(message.messageFromSelf);
        } else {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) messageTextView.getLayoutParams();
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

    static class ChatViewHolder extends RecyclerView.ViewHolder {

        public TextView message;

        ChatViewHolder(View itemView) {
            super(itemView);

            this.message = itemView.findViewById(R.id.chatMessage);
        }
    }
}
