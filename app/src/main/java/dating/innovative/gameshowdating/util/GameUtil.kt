package dating.innovative.gameshowdating.util

import android.app.Activity
import android.os.Environment
import android.widget.VideoView
import dating.innovative.gameshowdating.data.WebSocketHandler
import dating.innovative.gameshowdating.model.Game
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream

/*  Andreas Jeppesen - ajepp09@student.sdu.dk
    Emil Jensen - emije14@student.sdu.dk
    Nicolai Jensen - nije214@student.sdu.dk */

object GameUtil {
    @JvmStatic
    fun didRoundChange(newGameUpdate: Game, lastGameUpdate: Game?) =
        newGameUpdate.roundNumber > (lastGameUpdate?.roundNumber
            ?: 0)
    @JvmStatic
    fun loadVideo(ws: WebSocketHandler, game: Game, activity: Activity, videoView: VideoView) {
        ws.getVideo(game.nonJudger, game.roundNumber) {
            if (it == null) {
                return@getVideo
            }
            activity.runOnUiThread {
                val file = File(Environment.getExternalStorageDirectory(), "video.mp4")
                val bufferedOutputStream = BufferedOutputStream(FileOutputStream(file))
                bufferedOutputStream.apply { write(it); flush(); close() }
                videoView.setVideoPath(file.absolutePath)
                videoView.requestFocus()
                videoView.setOnPreparedListener {
                    it.start()
                }
                videoView.setOnCompletionListener {
                    ws.videoOver(game.gameId)
                }
            }
        }
    }

}