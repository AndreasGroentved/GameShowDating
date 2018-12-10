package dating.innovative.gameshowdating.data

import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dating.innovative.gameshowdating.model.*
import okhttp3.WebSocketListener
import org.json.JSONArray
import org.json.JSONObject

/*  Andreas Jeppesen - ajepp09@student.sdu.dk
    Emil Jensen - emije14@student.sdu.dk
    Nicolai Jensen - nije214@student.sdu.dk */

class WebSocketHandler private constructor() : WebSocketListener() {


    private lateinit var token: String

    private val gson = Gson()

    companion object {
        @JvmStatic
        val instance: WebSocketHandler by lazy { WebSocketHandler() }
    }

    fun isTokenSet() = ::token.isInitialized


    private val socket: Socket = IO.socket("http://10.126.48.222:3001")

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
            try {
                val data = (it[0] as JSONObject)
                val video = data.get("video") as ByteArray
                callBack(video)
            } catch (e: Exception) {
                callBack(null)
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


    fun sendOrUpdateVideo(file: ByteArray, roundNumber: Int, callBack: (Boolean) -> Unit) {
        socket.on("uploadFile") {
            val success = it[0] as String == "success"
            callBack(success)
        }
        println("file size")
        println(file.size)
        socket.emit("uploadFile", token, roundNumber, file)
    }

    fun getUser(username: String, callBack: (RemoteUser?) -> Unit) {
        println("user to get $username")
        var callBack: ((RemoteUser?) -> Unit)? = callBack
        socket.on("getUser") {
            //socket.off("getUser")
            println(it)
            val returnVal = try {
                it[0] as String?
            } catch (e: Exception) {
                "success"
            }
            if (returnVal == "failure") {
                println("failure")
                callBack(null)
                callBack = null
                return@on
            }
            val data = (it[0] as JSONObject)
            val password = data.getString("password")
            val biography = data.getString("biography")
            val sex = data.getString("sex")
            val age = data.getInt("age")
            val picture =
                try {
                    data.get("profilePicture") as ByteArray
                } catch (e: java.lang.Exception) {
                    null
                }
            val user = RemoteUser(username, password, picture, biography, sex, age)
            if (username == user._id) {
                callBack?.invoke(user)
                callBack = null
            }
        }

        socket.emit("getUser", token, username)
    }

    fun updateProfilePicture(profilePicture: ByteArray, callBack: (Boolean) -> Unit) {
        socket.on("updateProfilePicture") {
            val success = it[0] as String == "success"
            callBack(success)
        }
        println(profilePicture.size)
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

    fun comment(username: String, comment: String, timeStamp: Long, roundNumber: Int) {
        socket.emit("comment", token, username, comment, timeStamp, roundNumber)
    }

    fun getComments(username: String, callBack: (List<Feedback>) -> Unit) {
        socket.on("getComments") {
            socket.off("getComments")
            val comments = try {
                val data = (it[0] as JSONArray).toString()
                val gsonToken = object : TypeToken<List<Feedback>>() {}.type
                gson.fromJson<List<Feedback>>(data, gsonToken)
            } catch (e: java.lang.Exception) {
                emptyList<Feedback>()
            }
            callBack(comments)
        }
        socket.emit("getComments", token, username)
    }

    fun confirmGame(
        confirm: Boolean,
        gameId: String,
        gameUpdates: (Game) -> Unit,
        gameOverCallBack: ((List<String>) -> Unit)
    ) {
        var userName = ""
        socket.on("startGame") {
            socket.off("startGame")
            val gameId = it[0] as String
            val userCount = it[1] as Int
            userName = it[2] as String
            gameUpdates(Game(userName, userCount, userCount, gameId, 1))
        }
        socket.on("gameUpdate") {
            val userLeft = it[0] as Int
            val userTotal = it[1] as Int
            val roundNumber = it[2] as Int
            val game = Game(userName, userLeft, userTotal, gameId, roundNumber)
            println("gameUpdate")
            println(game)
            gameUpdates(game)
        }

        socket.on("gameOver") {
            socket.off("gameOver")
            if (it == null) gameOverCallBack(listOf())
            println(it[0])
            gameOverCallBack(
                try {
                    val data = (it[0] as JSONArray).toString()
                    val gsonToken = object : TypeToken<List<String>>() {}.type
                    gson.fromJson<List<String>>(data, gsonToken)
                } catch (e: java.lang.Exception) {
                    emptyList<String>()
                }
            )
        }

        socket.emit("confirmParticipation", token, gameId, confirm)
    }

    fun match(judger: Boolean, inQueueCallback: (String) -> Unit, matchAcceptedIdCallback: (String) -> Unit) {
        socket.on("inQueue") {
            val success = it[0] as String
            inQueueCallback(success)
        }
        socket.on("match") {
            val gameId = it[0] as String
            matchAcceptedIdCallback(gameId)
        }
        socket.emit("match", token, judger)
    }

    fun sendChatMessage(to: String, message: String, timeStamp: Long, messageSentCallBack: (Boolean) -> Unit) {
        socket.on("sendMessage") {
            socket.off("sendMessage")
            messageSentCallBack(true)//TODO
        }
        socket.emit("sendMessage", token, to, message, timeStamp)
    }

    /**
     * Alle samtaler, {hvem med mapper til en liste af beskeder}
     */
    fun getMessages(username: String, messageCallback: (Map<String, List<Message>>) -> Unit) {
        socket.on("messageRecieved") {
            getMessageUpdate(username, messageCallback)
        }
        getMessageUpdate(username, messageCallback)
    }

    fun videoOver(gameId: String) {
        socket.emit("videoOver", token, gameId)
    }

    private fun getMessageUpdate(username: String, callBack: (Map<String, List<Message>>) -> Unit) {
        socket.emit("getMessages", token)
        socket.on("getMessages") {
            socket.off("getMessages")
            println(it[0])
            val returnVal = try {
                it[0] as String?
            } catch (e: Exception) {
                "success"
            }
            val remoteMessage =
                (try {
                    if (returnVal == "failure") callBack(emptyMap())
                    val data = (it[0] as JSONArray).toString()
                    val gsonToken = object : TypeToken<List<RemoteMessage>>() {}.type //Get all messages from server
                    gson.fromJson<List<RemoteMessage>>(data, gsonToken) //Try to deserialize
                } catch (e: java.lang.Exception) {
                    emptyList<RemoteMessage>() //If this failes, make empty list
                }).groupBy { if (username == it.reciever) it.sender else it.reciever } //Group messages based on person
                    .map { (key, value) ->
                        //Map it to key value pairs, with user as key and messages as value
                        key to value.map {
                            val other = if (it.reciever == username) it.sender else it.reciever
                            val self = it.sender == username
                            val messageSelf = if (self) it.message else null
                            val messageOther = if (self) null else it.message
                            Message(username, other, messageSelf, messageOther)
                        }.reversed()
                    }.toMap()
            println(remoteMessage)
            callBack(remoteMessage)
        }
    }

    fun stopGameUpdates() {
        socket.off("gameUpdates")
    }
}
