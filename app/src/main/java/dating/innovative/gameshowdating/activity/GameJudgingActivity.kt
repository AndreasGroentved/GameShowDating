package dating.innovative.gameshowdating.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.MediaController
import dating.innovative.gameshowdating.R
import dating.innovative.gameshowdating.data.WebSocketHandler
import dating.innovative.gameshowdating.model.Game
import kotlinx.android.synthetic.main.activity_game_judging.*
import kotlinx.android.synthetic.main.activity_videoplayer.*
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream

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
            if (didRoundChange(game)) {
                loadVideo(game)
            }
            lastGameUpdate = game
        }, {
            //TODO tid til at lave damer/mænd(/starte chat)
            Snackbar.make(judging_parent, "Wait and see if you are chosen", Snackbar.LENGTH_LONG).show()
            this.finish()

        })
    }

    private fun loadVideo(game: Game) {
        ws.getVideo(game.nonJudger, game.roundNumber) {
            if (it == null) {
                println("VIDEOOOOOOOOOOOOOO NULLLLLLLLLLLLLLLLLLLL")
                return@getVideo
            }
            //val path = it?.saveToSdCard("video" + game.roundNumber)
            //println("file size " + it!!.size)
            val file = File(Environment.getExternalStorageDirectory(), "virker.mp4")
            println(file.absoluteFile)
            val bufferedOutputStream = BufferedOutputStream(FileOutputStream(file))
            bufferedOutputStream.apply { write(it); flush(); close() }
            println("wrote file")
            val mediaController = MediaController(this)
            //mediaController.setAnchorView(gameJudgingVideoView)
            mediaController.setMediaPlayer(gameJudgingVideoView)
            gameJudgingVideoView.visibility = View.VISIBLE;
            gameJudgingVideoView.setMediaController(mediaController)
            println(file.exists())
            val uri = Uri.fromFile(file)

            gameJudgingVideoView.setVideoURI(uri)

            // setVideoPath(file.absolutePath)
            gameJudgingVideoView.requestFocus()
            gameJudgingVideoView.setZOrderOnTop(true)
            gameJudgingVideoView.seekTo(1000)
            gameJudgingVideoView.start()

            gameJudgingVideoView.setOnCompletionListener {
                ws.videoOver(game.gameId)
                //TODO slet video
            }
        }
    }

    private fun didRoundChange(newGameUpdate: Game) = newGameUpdate.roundNumber > (lastGameUpdate?.roundNumber
        ?: 0)

    private fun setOutButton() {
        gameJudgingOutButton.setOnClickListener {
            val timeStamp = videoPlayerView.currentPosition
            ws.stopGameUpdates()
            ws.imOut(gameId, timeStamp.toLong())
            val outIntent = Intent(applicationContext, ProvideFeedbackActivity::class.java)
            outIntent.putExtra("gameId", gameId)
            outIntent.putExtra("timeStamp", timeStamp)
            startActivity(outIntent)
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
