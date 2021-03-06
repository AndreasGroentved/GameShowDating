package dating.innovative.gameshowdating.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import dating.innovative.gameshowdating.R;
import dating.innovative.gameshowdating.activity.ChatActivity;
import dating.innovative.gameshowdating.model.RemoteUser;

import java.util.ArrayList;

/*  Andreas Jeppesen - ajepp09@student.sdu.dk
    Emil Jensen - emije14@student.sdu.dk
    Nicolai Jensen - nije214@student.sdu.dk */

public class ChatOverviewAdapter extends RecyclerView.Adapter<ChatOverviewAdapter.ChatOverviewViewHolder> {

    public ArrayList<RemoteUser> userDataSet;
    private Context context;


    public ChatOverviewAdapter(ArrayList<RemoteUser> matches) {
        this.userDataSet = matches;
    }

    @NonNull
    @Override
    public ChatOverviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.user_list_view, parent, false);
        return new ChatOverviewViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatOverviewViewHolder holder, int position) {
        final RemoteUser user = userDataSet.get(position);
        if (user.getProfilePicture() != null) {
            holder.profilePictureView.setImageBitmap(
                    Bitmap.createScaledBitmap(BitmapFactory.decodeByteArray(user.getProfilePicture(), 0, user.getProfilePicture().length), 50, 50, false)
            );
        }
        holder.nameTextView.setText(user.get_id());
        Button openChatButton = holder.messageButton;
        openChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chatActivity = new Intent(context, ChatActivity.class);
                chatActivity.putExtra("username", user.get_id());
                context.startActivity(chatActivity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userDataSet.size();
    }

    class ChatOverviewViewHolder extends RecyclerView.ViewHolder {

        ImageView profilePictureView;
        TextView nameTextView;
        Button messageButton;

        ChatOverviewViewHolder(View itemView) {
            super(itemView);
            profilePictureView = itemView.findViewById(R.id.match_profile_image);
            nameTextView = itemView.findViewById(R.id.match_name);
            messageButton = itemView.findViewById(R.id.message_button);
        }
    }
}
