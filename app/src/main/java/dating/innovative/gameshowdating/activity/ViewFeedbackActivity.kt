package dating.innovative.gameshowdating.activity

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import dating.innovative.gameshowdating.R
import dating.innovative.gameshowdating.data.WebSocketHandler
import dating.innovative.gameshowdating.util.PreferenceManagerClass
import dating.innovative.gameshowdating.util.SQLiteHelper
import dating.innovative.gameshowdating.util.ViewFeedbackAdapter
import kotlinx.android.synthetic.main.activity_view_feedback.*
import java.util.*

class ViewFeedbackActivity : Activity() {

    private lateinit var recyclerAdapter: ViewFeedbackAdapter
    private lateinit var dbHelper: SQLiteHelper

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_feedback)
        dbHelper = SQLiteHelper.getSqLiteHelperInstance(applicationContext)

        feedbackRecyclerView.layoutManager = LinearLayoutManager(this)
        recyclerAdapter = ViewFeedbackAdapter(ArrayList())
        feedbackRecyclerView.adapter = recyclerAdapter

        val username = dbHelper.getUserByUsername(PreferenceManagerClass.getUsername(applicationContext))!!.username
        WebSocketHandler.instance.getComments(username) {
            runOnUiThread {
                recyclerAdapter.feedbackArrayList = it
                recyclerAdapter.notifyDataSetChanged()
            }
        }

    }
}
