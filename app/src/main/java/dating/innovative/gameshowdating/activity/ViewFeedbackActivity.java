package dating.innovative.gameshowdating.activity;

import android.app.Activity;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import dating.innovative.gameshowdating.R;
import dating.innovative.gameshowdating.model.Feedback;
import dating.innovative.gameshowdating.util.ViewFeedbackAdapter;

import java.util.ArrayList;

public class ViewFeedbackActivity extends Activity {

    RecyclerView recyclerView;
    RecyclerView.Adapter recyclerAdapter;
    RecyclerView.LayoutManager recyclerLayoutManager;
    SQLiteHelper dbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_feedback);
        dbHelper = SQLiteHelper.getSqLiteHelperInstance(getApplicationContext());

        //test thang
        //dbHelper.addFeedback("test", "You're mom gay");

        ArrayList<Feedback> feedbackArrayList = new ArrayList<>();
        if(dbHelper.getFeedback().size() > 0){
            for(int i = 0; i < dbHelper.getFeedback().size();i++){
                feedbackArrayList.add(dbHelper.getFeedback().get(i));
            }
        }

        System.out.println(feedbackArrayList);

        recyclerView = (RecyclerView) findViewById(R.id.feedbackRecyclerView);
        recyclerLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerLayoutManager);
        recyclerAdapter = new ViewFeedbackAdapter(feedbackArrayList);
        recyclerView.setAdapter(recyclerAdapter);
    }
}
