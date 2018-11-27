package dating.innovative.gameshowdating.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import dating.innovative.gameshowdating.R;
import dating.innovative.gameshowdating.model.Feedback;

import java.util.ArrayList;

public class ViewFeedbackAdapter extends RecyclerView.Adapter<ViewFeedbackAdapter.ViewFeedbackHolder> {

    ArrayList<Feedback> feedbackArrayList;
    Context context;

    public ViewFeedbackAdapter(ArrayList<Feedback> feedbackArrayList){
        this.feedbackArrayList = feedbackArrayList;
    }

    @NonNull
    @Override
    public ViewFeedbackHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View feedbackView = layoutInflater.inflate(R.layout.feedback_cell,parent,false);
        ViewFeedbackHolder viewFeedbackHolder = new ViewFeedbackHolder(feedbackView);
        return viewFeedbackHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewFeedbackHolder holder, int position) {
        Feedback feedback = feedbackArrayList.get(position);

        TextView nameTextView = holder.nameTextView;
        nameTextView.setText(feedback.name);

        TextView feedbackTextView = holder.feedbackTextView;
        feedbackTextView.setText(feedback.feedback);
    }

    @Override
    public int getItemCount() {
        return feedbackArrayList.size();
    }

    public static class ViewFeedbackHolder extends RecyclerView.ViewHolder{

        TextView nameTextView;
        TextView feedbackTextView;

        public ViewFeedbackHolder(View itemView) {
            super(itemView);

            this.nameTextView = (TextView) itemView.findViewById(R.id.feedbackNameTextview);
            this.feedbackTextView = (TextView) itemView.findViewById(R.id.feedbackFeedbackTextview);

        }
    }
}
