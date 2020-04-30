package com.noteworth.simpleredditclient.ui.adapter

import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.noteworth.simpleredditclient.R
import com.noteworth.simpleredditclient.model.RedditPost
import java.util.*

class RedditPostsAdapter(
    private val dataSet: MutableList<RedditPost>,
    private val redditPostSelectedListener: RedditPostSelectedListener
) :
    RecyclerView.Adapter<RedditPostsAdapter.ViewHolder>() {

    private val calendar: Calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RedditPostsAdapter.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_reddit_post, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RedditPostsAdapter.ViewHolder, position: Int) {
        val redditPost = dataSet[position]

        redditPost.data?.run {
            if (this.read) markPostAsRead(holder.readIndicator, holder.itemView.context)

            holder.redditPostAuthor.text = this.author
            holder.redditPostTimeCreated.text =
                DateUtils.getRelativeTimeSpanString(
                    (this.created * 1000),
                    calendar.timeInMillis, DateUtils.MINUTE_IN_MILLIS
                )

            // Some thumbnails comes with 'self' value which I don't really know what it does
            if (this.thumbnail.isNotEmpty() && this.thumbnail != "self") {
                Glide.with(holder.itemView.context)
                    .load(this.thumbnail)
                    .into(holder.redditPostThumbnail)
            }

            holder.redditPostTitle.text = this.title
            holder.redditCommentsCount.text = String.format(
                holder.itemView.context.getString(R.string.comments),
                this.numComments
            )

            holder.redditDismissButton.setOnClickListener {
                dataSet.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, dataSet.size)
            }

            holder.itemView.setOnClickListener {
                this.read = true
                markPostAsRead(holder.readIndicator, holder.itemView.context)
                notifyItemChanged(position)

                redditPostSelectedListener.onClick(redditPost)
            }
        }
    }

    private fun markPostAsRead(readIndicator: View, context: Context) {
        readIndicator.background = ContextCompat.getDrawable(context, R.drawable.ic_indicator_off)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    fun reset(newDataSet: MutableList<RedditPost>) {
        dataSet.clear()
        dataSet.addAll(newDataSet)
        notifyDataSetChanged()
    }

    fun clear() {
        if (dataSet.isNotEmpty()) {
            val dataSetSize = dataSet.size
            dataSet.clear()

            notifyItemRangeRemoved(0, dataSetSize)
            notifyDataSetChanged()
        }
    }

    interface RedditPostSelectedListener {
        fun onClick(selectedRedditPost: RedditPost)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val readIndicator: View = itemView.findViewById(R.id.read_indicator)
        val redditPostAuthor: TextView = itemView.findViewById(R.id.author)
        val redditPostTimeCreated: TextView = itemView.findViewById(R.id.time_created)
        val redditPostThumbnail: ImageView = itemView.findViewById(R.id.thumbnail)
        val redditPostTitle: TextView = itemView.findViewById(R.id.title)
        val redditDismissButton: TextView = itemView.findViewById(R.id.dismiss)
        val redditCommentsCount: TextView = itemView.findViewById(R.id.comments_count)
    }
}
