package dating.innovative.gameshowdating.data

import android.net.Uri
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dating.innovative.gameshowdating.data.Util.uriToFile
import dating.innovative.gameshowdating.model.Game
import dating.innovative.gameshowdating.model.GameData
import dating.innovative.gameshowdating.model.User
import okhttp3.WebSocketListener
import org.json.JSONObject


class WebSocketHandler private constructor() : WebSocketListener() {


    private object Holder {
        val INSTANCE = WebSocketHandler()
    }

    private lateinit var token: String
    private val gson = Gson()

    companion object {
        @JvmStatic
        val instance: WebSocketHandler by lazy { Holder.INSTANCE }
    }


    private val socket: Socket = IO.socket("http://192.168.0.10:3000")

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


    fun sendOrUpdateVideo(uri: Uri, username: String, roundNumber: Int, callBack: (Boolean) -> Unit) {
        socket.on("uploadFile") {
            val success = it[0] as String == "success"
            callBack(success)
        }
        socket.emit("uploadFile", token, username, roundNumber, uri.uriToFile().readBytes())
    }

    fun getUser(username: String, callBack: (User?) -> Unit) {
        socket.on("getUser") {
            socket.off("getUser")
            val returnVal = try {
                it[0] as String?
            } catch (e: Exception) {
                "success"
            }
            if (returnVal == "failure") callBack(null)
            val data = (it[0] as JSONObject).toString()
            val gsonToken = object : TypeToken<User>() {}.type
            val user = gson.fromJson<User>(data, gsonToken)
            callBack(user)
        }

        socket.emit("getUser", token, username)
    }

    fun updateProfilePicture(profilePicture: Uri, callBack: (Boolean) -> Unit) {
        socket.on("updateProfilePicture") {
            val success = it[0] as String == "success"
            callBack(success)
        }
        socket.emit("updateProfilePicture", token, profilePicture.uriToFile().readBytes())
    }

    fun updateBiography(bio: String, callBack: (Boolean) -> Unit) {
        socket.on("updateBiography") {
            val success = it[0] as String == "success"
            callBack(success)
        }
        socket.emit("updateBiography", token, bio)
    }

    fun subscribeToGame() {

    }

    fun imOut(gameId: String, videoTimeStamp: Long) {
        socket.emit("vote", token, gameId, videoTimeStamp)
    }

    fun comment(gameId: String, comment: String) {
        socket.emit("comment", token, gameId, comment)
    }

    fun confirm(confirm: Boolean, gameId: String, gameStart: (Game) -> Unit, gameUpdates: (String) -> Unit) {
        socket.on("startGame") {
            socket.off("startGame")
            val gameId = it[0] as String
            val userCount = it[1] as Int
            val userName = it[2] as String
            gameStart(Game(userName, userCount, gameId))
        }
        socket.on("gameUpdate") {
            val userCount = it[0] as Int
            val roundNumber = it[1] as Int
        }
        socket.emit("confirmParticipation", token, gameId)

    }

    //TODO
    fun match(judger: Boolean, inQueueCallback: (Boolean) -> Unit, matchAcceptedIdCallback: (Int) -> Unit) {
        socket.on("inQueue") {
            val success = it[0] as Boolean
            inQueueCallback(success)
        }
        socket.on("match") {

        }
        socket.emit("match", token, judger)
    }
}