package dating.innovative.gameshowdating;

import android.content.Context;
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

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<User> userDataSet;
    private Context context;


    public ChatAdapter(List<User> userDataSet){
        this.userDataSet = userDataSet;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.user_list_view, parent, false);
        ChatViewHolder chatViewHolder = new ChatViewHolder(contactView);
        return chatViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        User user = userDataSet.get(position);

        ImageView matchProfileImage = holder.profilePictureView;
        Uri profilePicUri = Uri.parse(user.profileImage);
        InputStream imageStream = null;
        try {
            imageStream = context.getContentResolver().openInputStream(profilePicUri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap matchImage = BitmapFactory.decodeStream(imageStream);
        matchProfileImage.setImageBitmap(resizeBitmap(matchImage,50,50));

        TextView nameView = holder.nameTextView;
        nameView.setText(user.getUsername());

    }

    public Bitmap resizeBitmap(Bitmap image, int bitmapWidth, int bitmapHeight){
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
