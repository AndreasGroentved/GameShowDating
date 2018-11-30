package dating.innovative.gameshowdating.data

import android.net.Uri
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dating.innovative.gameshowdating.data.Util.uriToFile
import dating.innovative.gameshowdating.model.Game
import dating.innovative.gameshowdating.model.RemoteUser
import dating.innovative.gameshowdating.model.User
import okhttp3.WebSocketListener
import org.json.JSONObject
import java.io.File


class WebSocketHandler private constructor() : WebSocketListener() {


    private lateinit var token: String
    private val gson = Gson()

    companion object {
        @JvmStatic
        val instance: WebSocketHandler by lazy { WebSocketHandler() }
    }


    private val socket: Socket = IO.socket("http://10.126.85.21:3000")

    init {
        socket.connect()
    }


    /**
     *  Returns user token or "failure", call first!!!!!!!!!!!!!!
     */
    fun logon(userName: String, password: String, callBack: (Boolean) -> Unit) {
        socket.on("login") {
            socket.off("login")
            val json = it[0] as String
            if (json != "failure") token = json
            callBack(json != "failure")
        }
        socket.emit("login", userName, password)
    }

    /**
     *  returns byte array of video or null if failed
     */
    fun getVideo(username: String, roundNumber: Int, callBack: (ByteArray?) -> Unit) {
        socket.on("getVideo") {
            socket.off("getVideo")

            val failure = (it[0] as? String ?: "") == "failure"
            if (failure) callBack(null)
            else {
                val byteArr = it[0] as ByteArray
                callBack(byteArr)
            }
        }
        socket.emit("getVideo", token, username, roundNumber)
    }

    /**
     *  Please subscribe to listen for logon problems
     */
    fun subscribeInvalidToken(callBack: (Boolean) -> Unit) {
        socket.on("invalid token") {
            callBack(true)
        }
    }


    fun createUser(user: User, callBack: (Boolean) -> Unit) {
        socket.on("createUser") {
            socket.off("createUser")
            val success = it[0] as String == "success"
            callBack(success)
        }
        socket.emit("createUser", user.username, user.password, user.sex, user.age)
    }


    fun sendOrUpdateVideo(file: File, username: String, roundNumber: Int, callBack: (Boolean) -> Unit) {
        socket.on("uploadFile") {
            val success = it[0] as String == "success"
            callBack(success)
        }
        socket.emit("uploadFile", token, username, roundNumber, file)
    }

    fun getUser(username: String, callBack: (RemoteUser?) -> Unit) {
        socket.on("getUser") {
            socket.off("getUser")
            val returnVal = try {
                it[0] as String?
            } catch (e: Exception) {
                "success"
            }
            if (returnVal == "failure") callBack(null)
            val data = (it[0] as JSONObject).toString()
            val gsonToken = object : TypeToken<RemoteUser>() {}.type
            val user = gson.fromJson<RemoteUser>(data, gsonToken)
            callBack(user)
        }

        socket.emit("getUser", token, username)
    }

    fun updateProfilePicture(profilePicture: File, callBack: (Boolean) -> Unit) {
        socket.on("updateProfilePicture") {
            val success = it[0] as String == "success"
            callBack(success)
        }
        socket.emit("updateProfilePicture", token, profilePicture)
    }

    fun updateBiography(bio: String, callBack: (Boolean) -> Unit) {
        socket.on("updateBiography") {
            val success = it[0] as String == "success"
            callBack(success)
        }
        socket.emit("updateBiography", token, bio)
    }


    fun imOut(gameId: String, videoTimeStamp: Long) {
        socket.emit("vote", token, gameId, videoTimeStamp)
    }

    fun videoWatched(gameId: String) {
        socket.emit("videoOver", token, gameId)
    }

    fun comment(gameId: String, comment: String) {
        socket.emit("comment", token, gameId, comment)
    }

    fun confirmGame(confirm: Boolean, gameId: String, gameUpdates: (Game) -> Unit) {
        var userName = ""
        socket.on("startGame") {
            socket.off("startGame")
            val gameId = it[0] as String
            val userCount = it[1] as Int
            userName = it[2] as String
            gameUpdates(Game(userName, userCount, userCount, gameId, 0))
        }
        socket.on("gameUpdate") {
            val userCount = it[0] as Int
            val roundNumber = it[2] as Int
            val userTotal = it[1] as Int
            gameUpdates(Game(userName, userTotal, userCount, gameId, roundNumber))
        }
        socket.on("gameOver") {

        }
        socket.emit("confirmParticipation", token, gameId, confirm)
    }

    fun match(judger: Boolean, inQueueCallback: (Boolean) -> Unit, matchAcceptedIdCallback: (String) -> Unit) {
        socket.on("inQueue") {
            val success = it[0] as Boolean
            inQueueCallback(success)
        }
        socket.on("match") {
            val success = it[0] as String == "success"
            val gameId = it[1] as String
            matchAcceptedIdCallback(gameId)
        }
        socket.emit("match", token, judger)
    }

    fun sendChatMessage(to: String, message: String, timeStamp: Long) {

    }

    fun getChat(with: String) {

    }
}