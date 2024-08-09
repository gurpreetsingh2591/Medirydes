package com.macrew.medirydes.dashboard.view.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.macrew.medirydes.R
import com.macrew.medirydes.dashboard.model.future.ScheduleData
import com.macrew.medirydes.utils.Static
import kotlinx.android.synthetic.main.item_future_shift_list.view.*

class FutureShiftAdapter(
    private val futureShiftData: ArrayList<ScheduleData?>?,
    private val activity: Activity,
    private val onItemCheckListener: OnItemCheckListener
) : RecyclerView.Adapter<FutureShiftAdapter.MyViewHolder>() {
    val static = Static()


    interface OnItemCheckListener {
        fun onItemView(id: String?,position: Int)
        fun onItemEdit(id: String?)
        fun onItemDelete(id: String?)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_future_shift_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return try {
            futureShiftData!!.size
        } catch (e: Exception) {
            0
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val futureShiftModel = futureShiftData!![position]

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            holder.itemView.txtDate.text = static.dateFormatString(futureShiftModel?.date.toString())+",  "
        }else{
            holder.itemView.txtDate.text=futureShiftModel?.date+ ",  "
        }

        holder.itemView.txtDay.text = static.capitalizeString(futureShiftModel?.day.toString())
        holder.itemView.txtStartTime.text = futureShiftModel?.start_time
        holder.itemView.txtEndTime.text = futureShiftModel?.end_time
        holder.itemView.txtVehicleId.text = futureShiftModel?.vehicle?.vin
        holder.itemView.txtTrips.text = futureShiftModel?.trips?.size.toString()

        holder.itemView.cvCurrentSchedule.setOnClickListener {
            onItemCheckListener.onItemView(futureShiftModel?.id.toString(),position)
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}