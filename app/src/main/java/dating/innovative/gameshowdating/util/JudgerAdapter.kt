package dating.innovative.gameshowdating.util

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dating.innovative.gameshowdating.R
import dating.innovative.gameshowdating.R.drawable.cross
import dating.innovative.gameshowdating.R.drawable.stick
import kotlinx.android.synthetic.main.list_item_judger.view.*


class JudgerAdapter : RecyclerView.Adapter<JudgerAdapter.GuessHolder>() {

    var names = emptyList<String>()

    var totalCount: Int = 0
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var isInCount = -1
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): GuessHolder =
        GuessHolder(
            LayoutInflater.from(viewGroup.context).inflate(
                R.layout.list_item_judger,
                viewGroup,
                false
            )
        )

    override fun onBindViewHolder(holder: GuessHolder, position: Int) {
        holder.set(isInValue(position), getText(position))
    }

    private fun getText(position: Int) = when {
        names.isNotEmpty() -> names[position]
        position < totalCount -> "unknown"
        else -> "out"
    }

    private fun isInValue(index: Int) = index < isInCount

    class GuessHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun set(isIn: Boolean, username: String) {
            if (isIn) view.imageView.setImageDrawable(view.context.getDrawable(stick))
            else view.imageView.setImageDrawable(view.context.getDrawable(cross))
            view.username.text = username
        }
    }

    override fun getItemCount() = totalCount
}