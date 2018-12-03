package dating.innovative.gameshowdating.activity

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import dating.innovative.gameshowdating.R
import dating.innovative.gameshowdating.data.WebSocketHandler
import dating.innovative.gameshowdating.util.PreferenceManagerClass
import dating.innovative.gameshowdating.util.ViewFeedbackAdapter
import java.util.*

class ViewFeedbackActivity : Activity() {

    var recyclerView: RecyclerView? = null
    var recyclerAdapter: ViewFeedbackAdapter? = null
    var recyclerLayoutManager: RecyclerView.LayoutManager? = null
    var dbHelper: SQLiteHelper? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_feedback)
        dbHelper = SQLiteHelper.getSqLiteHelperInstance(applicationContext)

        //test thang
        //dbHelper.addFeedback("test", "You're mom gay");

        /*   ArrayList<Feedback> feedbackArrayList = new ArrayList<>();
        if(dbHelper.getFeedback().size() > 0){
            feedbackArrayList.addAll(dbHelper.getFeedback());
        }*/

        /* System.out.println(feedbackArrayList);
         */



        recyclerView = findViewById(R.id.feedbackRecyclerView)
        recyclerLayoutManager = LinearLayoutManager(this)
        recyclerView!!.layoutManager = recyclerLayoutManager
        recyclerAdapter = ViewFeedbackAdapter(ArrayList())
        recyclerView!!.adapter = recyclerAdapter

        val username = dbHelper!!.getUserByUsername(PreferenceManagerClass.getUsername(applicationContext))!!.username
        WebSocketHandler.instance.getComments(username) {
            recyclerAdapter!!.feedbackArrayList = it
            recyclerAdapter!!.notifyDataSetChanged()
        }

    }
}
