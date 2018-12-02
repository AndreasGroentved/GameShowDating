package dating.innovative.gameshowdating.data

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Base64
import dating.innovative.gameshowdating.activity.MessageScheduler
import java.io.File
import java.io.FileOutputStream


object Util {


    fun Uri.uriToFile(): File = File(this.path)


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