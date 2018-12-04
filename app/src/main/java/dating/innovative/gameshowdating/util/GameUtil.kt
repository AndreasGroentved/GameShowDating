package dating.innovative.gameshowdating.util

import android.app.Activity
import android.os.Environment
import android.widget.VideoView
import dating.innovative.gameshowdating.data.WebSocketHandler
import dating.innovative.gameshowdating.model.Game
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream

object GameUtil {

    @JvmStatic
    fun didRoundChange(newGameUpdate: Game, lastGameUpdate: Game?) =
        newGameUpdate.roundNumber > (lastGameUpdate?.roundNumber
            ?: 0)

    @JvmStatic
    fun loadVideo(ws: WebSocketHandler, game: Game, activity: Activity, videoView: VideoView) {
        ws.getVideo(game.nonJudger, game.roundNumber) {
            println("boom")
            if (it == null) {
                println("VIDEOOOOOOOOOOOOOO NULLLLLLLLLLLLLLLLLLLL")
                return@getVideo
            }
            activity.runOnUiThread {
                val file = File(Environment.getExternalStorageDirectory(), "virker.mp4")
                val bufferedOutputStream = BufferedOutputStream(FileOutputStream(file))
                bufferedOutputStream.apply { write(it); flush(); close() }
                videoView.setVideoPath(file.absolutePath)
                videoView.requestFocus()
                videoView.setOnPreparedListener {
                    println("asddddddddddddddddddddddd")
                    it.start()
                }
                videoView.setOnCompletionListener {
                    ws.videoOver(game.gameId)
                    //TODO slet video
                }
            }
        }
    }

}