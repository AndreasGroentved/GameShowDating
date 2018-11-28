package dating.innovative.gameshowdating.data

import android.net.Uri
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dating.innovative.gameshowdating.data.Util.uriToFile
import dating.innovative.gameshowdating.model.GameData
import dating.innovative.gameshowdating.model.User
import okhttp3.WebSocketListener

internal class WebSocketHandler : WebSocketListener() {


    private object Holder {
        val INSTANCE = WebSocketHandler()
    }

    private lateinit var token: String
    private val gson = Gson()

    companion object {
        @JvmStatic
        val instance: WebSocketHandler by lazy { Holder.INSTANCE }
    }


    private val socket: Socket = IO.socket("http://10.0.2.2:3000")

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
        socket.emit("downloadFile", token, username, roundNumber)
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
        println("register")
        socket.emit("createUser", user.username, user.password, user.sex, user.age)
    }


    fun getListOfGames(games: (List<GameData>?) -> Unit) {
        socket.on("getListOfGames") {
            val success = it[0] as String == "success"
            if (!success) {
                games(null)
            }
            val json = it[0] as String
            val gsonToken = object : TypeToken<List<GameData>>() {}.type
            val user = gson.fromJson<List<GameData>>(json, gsonToken)
            games(user)

        }
        socket.emit("getListOfGames", token)
    }


    fun joinGame(gameId: String, gameUpdates: (String) -> Unit) {
        socket.on("game") {
            val status = it[0] as String
            //TODO
            gameUpdates(status)
        }
        socket.emit("joinGame", token, gameId)
    }


    fun registerGame(gameUpdates: (String) -> Unit) {
        socket.on("game") {
            val status = it[0] as String
            //TODO
            gameUpdates(status)
        }
        socket.emit("registerGame", token)
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
            socket.off("getUer")
            val returnVal = it[0] as String
            if (returnVal == "failure") callBack(null)
            val gsonToken = object : TypeToken<User>() {}.type
            val user = gson.fromJson<User>(returnVal, gsonToken)
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

    //TODO
    fun match(callBack: (Boolean) -> Unit) {
        socket.on("match") {
            val success = it[0] as String == "success"
            callBack(success)
        }
        socket.emit("match", token)
    }
}