package dating.innovative.gameshowdating.data

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Base64
import dating.innovative.gameshowdating.util.MessageScheduler
import dating.innovative.gameshowdating.model.RemoteUser
import dating.innovative.gameshowdating.model.User
import java.io.File
import java.io.FileOutputStream

/*  Andreas Jeppesen - ajepp09@student.sdu.dk
    Emil Jensen - emije14@student.sdu.dk
    Nicolai Jensen - nije214@student.sdu.dk */

object Util {

    @JvmStatic
    fun fileToBytes(file: File): ByteArray = file.readBytes()

    fun Uri.uriToFile(): File = File(this.path)

    @JvmStatic
    fun remoteUserToUser(remoteUser: RemoteUser) =
        User(remoteUser._id, remoteUser.password, "", remoteUser.biography, "", "", "", remoteUser.sex, remoteUser.age)

    //https://stackoverflow.com/questions/17149205/android-convert-bytes-array-to-file-and-save-file-in-sd-card
    fun ByteArray.saveToSdCard(name: String): String =
        try {
            /* file_byte is yous json string*/
            val decodestring = Base64.decode(this, Base64.DEFAULT)
            val file = Environment.getExternalStorageDirectory()
            val dir = File(file.absolutePath + "/videos/")
            if (!dir.exists()) dir.mkdirs()
            val document = File(dir, name)
            if (document.exists()) document.delete()

            val fos = FileOutputStream(document.path)
            fos.write(decodestring)
            fos.close()
            dir.path + name
        } catch (e: Exception) {
            ""
        }

    // schedule the start of the service every 10 - 30 seconds
    @JvmStatic
    fun scheduleJob(context: Context) {
        val serviceComponent = ComponentName(context, MessageScheduler::class.java)
        val builder = JobInfo.Builder(0, serviceComponent)
        builder.setMinimumLatency((5 * 1000).toLong()) // wait at least
        builder.setOverrideDeadline((10 * 1000).toLong()) // maximum delay
        val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.schedule(builder.build())
    }

}