package dating.innovative.gameshowdating.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import okio.ByteString
import java.util.concurrent.TimeUnit

class WebSocketHandler : WebSocketListener() {
    private var wSocket: WebSocket? = null
    private val gson = Gson()

    fun run() {
        val client = OkHttpClient.Builder()
            .readTimeout(0, TimeUnit.MILLISECONDS)
            .build()

        val request = Request.Builder()
            .url("ws://10.0.2.2:3000")
            .build()
        client.newWebSocket(request, this)

    }

    override fun onOpen(webSocket: WebSocket, response: Response?) {
        wSocket = webSocket
    }

    override fun onMessage(webSocket: WebSocket?, text: String?) {
        println("MESSAGE: " + text!!)
        println(gson.fromJson<ResponseObject<Any>>(text, object : TypeToken<ResponseObject<Any>>() {}.type))
        val serverResponse = gson.fromJson<ResponseObject<Any>>(text, object : TypeToken<ResponseObject<Any>>() {}.type)
        val callback = mapOfCallBacks[serverResponse.id]
        callback?.invoke(serverResponse)
    }

    override fun onMessage(webSocket: WebSocket?, bytes: ByteString) {
        println("MESSAGE: " + bytes.hex())

    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String?) {
        webSocket.close(1000, null)
        println("CLOSE: $code $reason")
    }

    override fun onFailure(webSocket: WebSocket?, t: Throwable, response: Response?) {
        t.printStackTrace()
    }

    fun <T> send(data: CallObject, callBack: (ResponseObject<T>) -> Unit) {
        println(gson.toJson(data))
        if (wSocket != null) wSocket?.send(gson.toJson(data)) else throw RuntimeException("socket error")
        mapOfCallBacks[data.id] = callBack as (ResponseObject<Any>) -> Unit //Alting kan castes til Any
    }

    private val mapOfCallBacks = mutableMapOf<String, (ResponseObject<Any>) -> Unit>()
}