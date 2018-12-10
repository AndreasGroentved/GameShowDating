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

import java.util.List;

/*  Andreas Jeppesen - ajepp09@student.sdu.dk
    Emil Jensen - emije14@student.sdu.dk
    Nicolai Jensen - nije214@student.sdu.dk */

public class ViewFeedbackAdapter extends RecyclerView.Adapter<ViewFeedbackAdapter.ViewFeedbackHolder> {

    public List<Feedback> feedbackArrayList;

    public ViewFeedbackAdapter(List<Feedback> feedbackArrayList) {
        this.feedbackArrayList = feedbackArrayList;
    }

    @NonNull
    @Override
    public ViewFeedbackHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View feedbackView = layoutInflater.inflate(R.layout.feedback_cell, parent, false);
        return new ViewFeedbackHolder(feedbackView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewFeedbackHolder holder, int position) {
        Feedback feedback = feedbackArrayList.get(position);

        TextView nameTextView = holder.nameTextView;
        nameTextView.setText(feedback.from);

        TextView feedbackTextView = holder.feedbackTextView;
        feedbackTextView.setText(feedback.text);
    }

    @Override
    public int getItemCount() {
        return feedbackArrayList.size();
    }

    static class ViewFeedbackHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView feedbackTextView;

        ViewFeedbackHolder(View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.feedbackNameTextview);
            feedbackTextView = itemView.findViewById(R.id.feedbackFeedbackTextview);
        }
    }
}
