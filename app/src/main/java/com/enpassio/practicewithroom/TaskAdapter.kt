package com.enpassio.practicewithroom

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.enpassio.practicewithroom.database.TaskEntry
import java.text.SimpleDateFormat
import java.util.*

class TaskAdapter(val context: Context, val listener: ItemClickListener) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    // Constant for date format
    private val DATE_FORMAT = "dd/MM/yyy"

    // Member variable to handle item clicks
    private val mItemClickListener: ItemClickListener? = listener
    // Class variables for the List that holds task data and the Context
    private var mTaskEntries: List<TaskEntry>? = null
    private val mContext: Context = context
    // Date formatter
    private val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())


    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val taskEntry = mTaskEntries?.get(position)
        val description = taskEntry?.description
        val priority = taskEntry?.priority
        val updatedAt = dateFormat.format(taskEntry?.updatedAt)

        //Set values
        holder.taskDescriptionView.text = description
        holder.updatedAtView.text = updatedAt

        // Programmatically set the text and color for the priority TextView
        val priorityString = "" + priority // converts int to String
        holder.priorityView.text = priorityString

        val priorityCircle = holder.priorityView.background as GradientDrawable
        // Get the appropriate background color based on the priority
        val priorityColor = getPriorityColor(priority!!)
        priorityCircle.setColor(priorityColor)
    }

    // Inflates the item views
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): TaskViewHolder {
        val view = LayoutInflater.from(mContext)
                .inflate(R.layout.task_layout, parent, false)

        return TaskViewHolder(view)
    }

    private fun getPriorityColor(priority: Int): Int {
        var priorityColor = 0

        when (priority) {
            1 -> priorityColor = ContextCompat.getColor(mContext, R.color.materialRed)
            2 -> priorityColor = ContextCompat.getColor(mContext, R.color.materialOrange)
            3 -> priorityColor = ContextCompat.getColor(mContext, R.color.materialYellow)
            else -> {
            }
        }
        return priorityColor
    }

    fun setTasks(taskEntries: List<TaskEntry>) {
        mTaskEntries = taskEntries
        notifyDataSetChanged()
    }

    // Gets the number of items in the list
    override fun getItemCount(): Int {
        if (mTaskEntries == null) {
            return 0;
        }
        return mTaskEntries!!.size;
    }

    interface ItemClickListener {
        fun onItemClickListener(itemId: Int)
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        // Class variables for the task description and priority TextViews
        var taskDescriptionView: TextView
        var updatedAtView: TextView
        var priorityView: TextView

        init {

            taskDescriptionView = itemView.findViewById(R.id.taskDescription)
            updatedAtView = itemView.findViewById(R.id.taskUpdatedAt)
            priorityView = itemView.findViewById(R.id.priorityTextView)
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            val elementId = mTaskEntries?.get(adapterPosition)?.id
            mItemClickListener?.onItemClickListener(elementId!!)
        }
    }

}
