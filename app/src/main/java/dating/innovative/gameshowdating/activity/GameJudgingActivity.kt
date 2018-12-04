package dating.innovative.gameshowdating.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import dating.innovative.gameshowdating.R
import dating.innovative.gameshowdating.data.WebSocketHandler
import dating.innovative.gameshowdating.model.Game
import dating.innovative.gameshowdating.util.GameUtil
import kotlinx.android.synthetic.main.activity_game_judging.*

class GameJudgingActivity : Activity() {

    private lateinit var gameId: String
    private lateinit var ws: WebSocketHandler
    private var lastGameUpdate: Game? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_judging)
        ws = WebSocketHandler.instance
        gameId = intent.getStringExtra("gameId")
        handleGameUpdates(gameId)
        setOutButton()
    }

    private fun handleGameUpdates(gameId: String) {
        ws.confirmGame(true, gameId, { game ->
            updateViews(game)
            if (GameUtil.didRoundChange(game, lastGameUpdate)) {
                println("round changed")
                GameUtil.loadVideo(ws, game, this, gameJudgingVideoView)
            } else {
                println("round didn't change")
            }
            lastGameUpdate = game
        }, {
            //TODO tid til at lave damer/mænd(/starte chat)
            Snackbar.make(judging_parent, "Wait and see if you are chosen", Snackbar.LENGTH_LONG).show()
            this.finish()
        })
    }


    private fun setOutButton() {
        gameJudgingOutButton.setOnClickListener {
            val timeStamp = gameJudgingVideoView.currentPosition
            ws.stopGameUpdates()
            ws.imOut(gameId, timeStamp.toLong())
            val intent = Intent(applicationContext, ProvideFeedbackActivity::class.java)
                .putExtra("gameId", gameId)
                .putExtra("timeStamp", timeStamp)
                .putExtra("username", lastGameUpdate!!.nonJudger)
                .putExtra("roundnumber", lastGameUpdate!!.roundNumber)
            startActivity(intent)
            this@GameJudgingActivity.finish()
        }
    }

    private fun updateViews(game: Game) {
        runOnUiThread {
            gameJudgingRemainingUsers.text = "Users remaining: ${game.userLeft}"
            setJudgedBio(game.nonJudger)
        }
    }

    private fun setJudgedBio(userName: String) {
        ws.getUser(userName) {
            if (it == null) return@getUser
            runOnUiThread {
                gameJudgingUserBio.text = it.biography
            }
        }
    }
}
