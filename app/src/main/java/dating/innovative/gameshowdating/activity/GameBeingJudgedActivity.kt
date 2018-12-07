package dating.innovative.gameshowdating.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.GridLayoutManager
import android.view.View
import dating.innovative.gameshowdating.R
import dating.innovative.gameshowdating.data.WebSocketHandler
import dating.innovative.gameshowdating.model.Game
import dating.innovative.gameshowdating.util.ClickListener
import dating.innovative.gameshowdating.util.GameUtil
import dating.innovative.gameshowdating.util.JudgerAdapter
import dating.innovative.gameshowdating.util.RecyclerTouchListener
import kotlinx.android.synthetic.main.activity_game_being_judged.*

class GameBeingJudgedActivity : Activity() {

    private lateinit var adapter: JudgerAdapter
    private var lastGameUpdate: Game? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_being_judged)
        val gameId = intent.extras!!.getString("gameId")
        val ws = WebSocketHandler.instance
        ws.confirmGame(true, gameId!!, { game ->
            if (game.userCount == 0) nooneLeft()
            updateUI(game)
            if (GameUtil.didRoundChange(game, lastGameUpdate)) {
                println("round changed")
                GameUtil.loadVideo(ws,game, this, gameVideoView)
            } else {
                println("round didn't change")
            }
        }, {
            if (it.isEmpty()) nooneLeft()
            someLeft(it)
        })
        setUpListView()
    }

    private fun someLeft(it: List<String>) {
        runOnUiThread{
            val recyclerLayoutManager = GridLayoutManager(this, it.size)
            judged_recycler.layoutManager = recyclerLayoutManager
            adapter.names = it
            adapter.totalCount = it.size
            adapter.isInCount = it.size
            judged_recycler.addOnItemTouchListener(
                RecyclerTouchListener(
                    this,
                    judged_recycler,
                    clickListener(it)
                )
            )
            Snackbar.make(being_judged_parent, "Click on user to see more and begin chat", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun nooneLeft() {
        Snackbar.make(being_judged_parent, "Party cancelled", Snackbar.LENGTH_LONG).show()
    }


    private fun clickListener(userNames: List<String>) = object : ClickListener {
        override fun onLongClick(view: View, position: Int) {}
        override fun onClick(view: View, position: Int) {
            val intent: Intent = Intent(this@GameBeingJudgedActivity, ShouldDateActivity::class.java)
                .putExtra("username", userNames[position])
            startActivity(intent)
        }
    }

    private fun updateUI(game: Game) {
        runOnUiThread {
            adapter.totalCount = game.userCount
            adapter.isInCount = game.userLeft
            round_number_view.text = "Round number: ${game.roundNumber}"
        }
    }

    private fun setUpListView() {
        adapter = JudgerAdapter()
        judged_recycler.adapter = adapter
        val recyclerLayoutManager = GridLayoutManager(this, 5)
        judged_recycler.layoutManager = recyclerLayoutManager
    }
}
