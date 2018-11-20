package dating.innovative.gameshowdating.data

import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.WebSocketListener
import java.io.File

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

    fun download(name: String, callBack: (ByteArray) -> Unit) {
        socket.on("download") {
            println("download")
            socket.off("downloadFile")
            val byteArr = it[0] as ByteArray
            callBack(byteArr)
        }
        socket.emit("downloadFile", name)
    }

    fun sendFile(file: File, name: String) {
        socket.emit("uploadVideo", name, file.readBytes())
    }


    /*

       try {
                    InputStream inputStream = getResources().openRawResource(R.raw.defaultprofilepic);
                    File tempFile = File.createTempFile("pre", "suf");
                    copyFile(inputStream, new FileOutputStream(tempFile));
                    ws.sendFile(tempFile, "tester");
                } catch (IOException e) {
                    throw new RuntimeException("Can't create temp file ", e);
                }


                    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

     */
}