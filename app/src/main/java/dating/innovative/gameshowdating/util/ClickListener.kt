package dating.innovative.gameshowdating.util

import android.view.View

/**
 * Created by Andreas on 20-Jun-16.
 */
interface ClickListener {
    fun onClick(view: View, position: Int)
    fun onLongClick(view: View, position: Int)
}