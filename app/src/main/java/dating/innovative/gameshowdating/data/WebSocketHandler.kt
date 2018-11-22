package dating.innovative.gameshowdating.data

import android.net.Uri
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dating.innovative.gameshowdating.User
import okhttp3.WebSocketListener
import dating.innovative.gameshowdating.data.Util.uriToFile

class WebSocketHandler : WebSocketListener() {


    private val socket: Socket = IO.socket("http://10.0.2.2:3000")
    private val gson = Gson()

    init {
        socket.connect();
    }

    fun logon(userName: String, password: String, callBack: (String) -> Unit) {
        socket.on("login") {
            socket.off("login")
            val json = it[0] as String;
            val response =
                gson.fromJson<ResponseObject<String>>(json, object : TypeToken<ResponseObject<String>>() {}.type)
            callBack(response.response)
        }
        socket.emit("login", userName, password)
    }

    fun getVideo(name: String, roundNumber: Int, callBack: (ByteArray) -> Unit) {
        socket.on("download") {
            println("download")
            socket.off("download")
            val byteArr = it[0] as ByteArray
            callBack(byteArr)
        }
        socket.emit("downloadVideo", name,roundNumber)
    }

    fun createUser(user:User){
        
    }

    fun sendOrUpdateVideo(uri: Uri, name: String, roundNumber:Int) {
        socket.emit("uploadVideo", name, roundNumber, uri.uriToFile().readBytes())
    }


}