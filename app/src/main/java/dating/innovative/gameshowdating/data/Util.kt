package dating.innovative.gameshowdating.data

import android.net.Uri
import java.io.File

object Util {


    fun Uri.uriToFile(): File = File(this.path)

}