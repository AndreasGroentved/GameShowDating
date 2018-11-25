package dating.innovative.gameshowdating.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import dating.innovative.gameshowdating.activity.ChatActivity;
import dating.innovative.gameshowdating.R;
import dating.innovative.gameshowdating.activity.SQLiteHelper;
import dating.innovative.gameshowdating.model.User;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private ArrayList<User> userDataSet;
    private Context context;
    SQLiteHelper dbHelper;


    public ChatAdapter(ArrayList<User> matches){
        this.userDataSet = matches;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        dbHelper = SQLiteHelper.getSqLiteHelperInstance(context);
        LayoutInflater inflater = LayoutInflater.from(context);


        View contactView = inflater.inflate(R.layout.user_list_view, parent, false);
        ChatViewHolder chatViewHolder = new ChatViewHolder(contactView);
        return chatViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        final User user = userDataSet.get(position);
        ImageView matchProfileImage = holder.profilePictureView;
        Uri profilePicUri = Uri.parse(user.profileImage);
        InputStream imageStream = null;
        try {
            imageStream = context.getContentResolver().openInputStream(profilePicUri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap matchImage = BitmapFactory.decodeStream(imageStream);
        matchProfileImage.setImageBitmap(resizeBitmap(matchImage,50));

        TextView nameView = holder.nameTextView;
        nameView.setText(user.getUsername());

        Button openChatButton = holder.messageButton;
        openChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chatActivity = new Intent(context,ChatActivity.class);
                chatActivity.putExtra("username", user.getUsername());
                context.startActivity(chatActivity);
            }
        });

    }

    public Bitmap resizeBitmap(Bitmap image, int maxSize){

        int bitmapWidth = image.getWidth();
        int bitmapHeight = image.getHeight();

        float bitmapRatio = (float) bitmapWidth / (float) bitmapHeight;
        if(bitmapRatio > 1){
            bitmapWidth = maxSize;
            bitmapHeight = (int) (bitmapWidth / bitmapRatio);
        } else {
            bitmapHeight = maxSize;
            bitmapWidth = (int) (bitmapHeight * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, bitmapWidth, bitmapHeight, true);
    }

    @Override
    public int getItemCount() {
        return userDataSet.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {

        public ImageView profilePictureView;
        public TextView nameTextView;
        public Button messageButton;

        public ChatViewHolder(View itemView) {
            super(itemView);

            this.profilePictureView = itemView.findViewById(R.id.match_profile_image);
            this.nameTextView = itemView.findViewById(R.id.match_name);
            this.messageButton = itemView.findViewById(R.id.message_button);
        }
    }
}
