package dating.innovative.gameshowdating.activity

import android.app.job.JobParameters
import android.app.job.JobService
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import dating.innovative.gameshowdating.R
import dating.innovative.gameshowdating.data.Util
import dating.innovative.gameshowdating.data.WebSocketHandler
import dating.innovative.gameshowdating.util.PreferenceManagerClass


class MessageScheduler : JobService() {

    override fun onStartJob(params: JobParameters): Boolean {
        println("background!!!!")
        if (PreferenceManagerClass.getUsername(this).isNotEmpty()) {
            val userName = PreferenceManagerClass.getUsername(this)
            val password = SQLiteHelper.getSqLiteHelperInstance(this).getUserByUsername(userName).password
            val ws = WebSocketHandler.instance
            if (ws.isTokenSet()) {
                getMessages(userName)
            } else {
                ws.logon(userName, password) {
                    if (!it) TODO()
                    getMessages(userName)
                }
            }
        }

        Util.scheduleJob(applicationContext) // reschedule the job
        return true
    }

    private fun getMessages(userName: String) {
        println("get messsssssssssssssssssaaaaaaaaaaaaaggggggggggeeeeeeeeeesssssssssss")
        val oldHash = PreferenceManagerClass.getMessageHash(this)
        WebSocketHandler.instance.getMessages(userName) {
            val newHash = it.map { it.value.count() }.sum().toString() //Den eneste rigtige måde at udregne hashes på!
            if (newHash != oldHash) {
                createNotification()
            }
            PreferenceManagerClass.setMessageHash(this, newHash)
        }

    }

    private fun createNotification() {
        val builder = NotificationCompat.Builder(this, "123" /*didn't want to read documentation...*/)
            .setSmallIcon(R.drawable.cross)
            .setContentTitle("New message")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(123, builder)
        }
    }

    override fun onStopJob(params: JobParameters): Boolean = true


}