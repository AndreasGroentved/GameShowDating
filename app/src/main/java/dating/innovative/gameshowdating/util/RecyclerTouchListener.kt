package dating.innovative.gameshowdating.util

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.GestureDetector
import android.view.MotionEvent
import dating.innovative.gameshowdating.util.ClickListener

/**
 * Created by Andreas on 20-Jun-16.
 */

//Låne kode
class RecyclerTouchListener(context: Context, recyclerView: RecyclerView, private val clickListener: ClickListener) :
    RecyclerView.OnItemTouchListener {
    private val gestureDetector: GestureDetector

    init {
        gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean = true
            override fun onLongPress(e: MotionEvent) {
                val child = recyclerView.findChildViewUnder(e.x, e.y)
                if (child != null) clickListener.onLongClick(child, recyclerView.getChildLayoutPosition(child))
            }
        })
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        val child = rv.findChildViewUnder(e.x, e.y)
        if (child != null && gestureDetector.onTouchEvent(e)) clickListener.onClick(
            child,
            rv.getChildLayoutPosition(child)
        ) //TODO check om succes getChildPosition() //TODO ryk
        return false
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
}