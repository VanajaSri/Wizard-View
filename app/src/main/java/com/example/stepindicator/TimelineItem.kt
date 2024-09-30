package com.example.stepindicator

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.widget.ContentLoadingProgressBar
import androidx.recyclerview.widget.RecyclerView

data class TimelineItem(
    val title: String,
    val descriptions: List<String>,
    val iconResId: Int,
    var isCompleted: Boolean = false,
    var visibleDescriptions: Int = 0
)

class TimelineAdapter(val items: List<TimelineItem>) : RecyclerView.Adapter<TimelineAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.timelineTitle)
        val descriptionsContainer: LinearLayout = view.findViewById(R.id.descriptionsContainer)
        val timelinePoint: ImageView = view.findViewById(R.id.timelinePoint)
        val timelinePointBackground: View = view.findViewById(R.id.timelinePointContainer)
        val timelineLine: ContentLoadingProgressBar = view.findViewById(R.id.timelineLine)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.timeline_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.titleTextView.text = item.title

        holder.descriptionsContainer.removeAllViews()

        item.descriptions.forEachIndexed { index, description ->
            if (index < item.visibleDescriptions) {
                val descriptionCard = LayoutInflater.from(holder.itemView.context)
                    .inflate(R.layout.description_item, holder.descriptionsContainer, false) as CardView
                val descriptionTextView = descriptionCard.findViewById<TextView>(R.id.descriptionText)
                descriptionTextView.text = description
                holder.descriptionsContainer.addView(descriptionCard)
            }
        }

        holder.timelinePoint.setImageResource(item.iconResId)

        val context = holder.itemView.context
        val backgroundDrawable = ContextCompat.getDrawable(context, R.drawable.timeline_point_background)?.mutate() as? GradientDrawable
        if (item.isCompleted) {
            backgroundDrawable?.setColor(ContextCompat.getColor(context, R.color.timelinePointColorGreen))
            holder.timelineLine.setBackgroundColor(ContextCompat.getColor(context, R.color.timelineLineColorGreen))
        } else {
            backgroundDrawable?.setColor(ContextCompat.getColor(context, R.color.timelinePointColorGrey))
            holder.timelineLine.setBackgroundColor(ContextCompat.getColor(context, R.color.timelineLineColorGrey))
        }
        holder.timelinePointBackground.background = backgroundDrawable

        if (position == items.size - 1) {
            holder.timelineLine.visibility = View.GONE
        } else {
            holder.timelineLine.visibility = View.VISIBLE
        }
    }

    override fun getItemCount() = items.size

    fun updateProgress(position: Int) {
        if (position < items.size) {
            items[position].isCompleted = true
            if (items[position].visibleDescriptions < items[position].descriptions.size) {
                items[position].visibleDescriptions++
            }
            notifyItemChanged(position)
        }
    }
}