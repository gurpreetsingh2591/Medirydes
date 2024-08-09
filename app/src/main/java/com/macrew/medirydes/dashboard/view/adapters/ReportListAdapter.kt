package com.macrew.medirydes.dashboard.view.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.macrew.medirydes.R
import com.macrew.medirydes.dashboard.model.reports.ScheduleData

import com.macrew.medirydes.utils.Static
import kotlinx.android.synthetic.main.item_report_list.view.*

class ReportListAdapter(
    private val reportsModel: ArrayList<ScheduleData?>?,
    private val activity: Activity,
    private val onItemCheckListener: OnItemCheckListener
) : RecyclerView.Adapter<ReportListAdapter.MyViewHolder>() {
    val static = Static()

    interface OnItemCheckListener {
        fun onItemView(id: String?, position: Int)
        fun onItemEdit(id: String?)
        fun onItemDelete(id: String?)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_report_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return try {
            reportsModel!!.size
        } catch (e: Exception) {
            0
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val reportsModel = reportsModel!![position]


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.itemView.txtDate.text =
                static.dateFormatString(reportsModel?.date.toString()) + ",  "
        } else {
            holder.itemView.txtDate.text = reportsModel?.date.toString() + ",  "
        }
        holder.itemView.txtDay.text = static.capitalizeString(reportsModel?.day.toString())
        holder.itemView.txtStartTime.text = reportsModel?.start_time
        holder.itemView.txtEndTime.text = reportsModel?.end_time
        holder.itemView.txtVehicleId.text = reportsModel?.vehicle?.vin
        holder.itemView.txtTrips.text = reportsModel?.trips?.size.toString()

        holder.itemView.cvReports.setOnClickListener {
            onItemCheckListener.onItemView(reportsModel?.id.toString(), position)
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}