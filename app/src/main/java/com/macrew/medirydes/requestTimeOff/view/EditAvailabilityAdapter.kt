package com.macrew.medirydes.requestTimeOff.view

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.macrew.medirydes.R
import com.macrew.medirydes.dashboard.model.avalibilty.WorkDay
import com.macrew.medirydes.dashboard.model.future.Trips
import com.macrew.medirydes.futureSchedule.FutureTripsListAdapter
import com.macrew.medirydes.utils.DateTimePicker
import com.macrew.medirydes.utils.SharedPrefrencesUtils
import com.macrew.medirydes.utils.Static
import kotlinx.android.synthetic.main.activity_edit_request.*
import kotlinx.android.synthetic.main.activity_edit_request.view.*
import kotlinx.android.synthetic.main.item_edit_availability_list.view.*
import kotlinx.android.synthetic.main.item_trip_sequence_list.view.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class EditAvailabilityAdapter(
    private val workDay: ArrayList<WorkDay>,
    private val activity: Activity,
    private val onItemCheckListener: OnItemCheckListener
) : RecyclerView.Adapter<EditAvailabilityAdapter.MyViewHolder>() {
    val static = Static()
    lateinit var jsonElement: JSONObject
    val gson = Gson()

    interface OnItemCheckListener {
        fun onItemStartView(startTime: String?, position: Int)
        fun onItemEndEdit(endTime: String?, position: Int)
        fun onItemAllDayDelete(allDay: String?, position: Int)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_edit_availability_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return try {
            workDay.size
        } catch (e: Exception) {
            0
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val workDaylist = workDay[position]

        if (position == 0) {
            holder.itemView.txtDay.text = "Monday"
        }
        if (position == 1) {
            holder.itemView.txtDay.text = "Tuesday"
        }
        if (position == 2) {
            holder.itemView.txtDay.text = "Wednesday"
        }
        if (position == 3) {
            holder.itemView.txtDay.text = "Thursday"
        }
        if (position == 4) {
            holder.itemView.txtDay.text = "Friday"
        }
        if (position == 5) {
            holder.itemView.txtDay.text = "Saturday"
        }
        if (position == 6) {
            holder.itemView.txtDay.text = "Sunday"
        }
        holder.itemView.txtStartTime.text = workDay[position].from_time
        holder.itemView.txtEndTime.text = workDay[position].to_time

        if (workDay[position].all_day.equals("1")) {
            holder.itemView.cbAllDay.isChecked = true
        }

        holder.itemView.txtStartTime.setOnClickListener {
            DateTimePicker(activity) {
                val sdf = SimpleDateFormat(DateTimePicker.getFormat("t"), Locale.getDefault())
                holder.itemView.txtStartTime.text = sdf.format(it.calendar.time)
                onItemCheckListener.onItemStartView(sdf.format(it.calendar.time), position)
            }.showTime()

        }
        holder.itemView.txtEndTime.setOnClickListener {
            DateTimePicker(activity) {
                val sdf = SimpleDateFormat(DateTimePicker.getFormat("t"), Locale.getDefault())
                holder.itemView.txtEndTime.text = sdf.format(it.calendar.time)

                onItemCheckListener.onItemEndEdit(sdf.format(it.calendar.time), position)
            }.showTime()

        }
        holder.itemView.cbAllDay.setOnCheckedChangeListener { compoundButton, b ->
            if (compoundButton.isChecked) {
                onItemCheckListener.onItemAllDayDelete("1", position)
            } else {
                onItemCheckListener.onItemAllDayDelete("0", position)
            }
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}

