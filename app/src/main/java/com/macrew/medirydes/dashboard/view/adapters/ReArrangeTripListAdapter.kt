package com.macrew.medirydes.dashboard.view.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.macrew.medirydes.R
import com.macrew.medirydes.dashboard.model.currentSchedule.Trips
import com.macrew.medirydes.utils.Static
import kotlinx.android.synthetic.main.item_re_route_trip_list.view.*


class ReArrangeTripListAdapter(
    private val tripList: ArrayList<Trips?>,
    private val activity: Activity,
    private val onItemCheckListener: OnItemCheckListener
) : RecyclerView.Adapter<ReArrangeTripListAdapter.MyViewHolder>() {
    val static = Static()

    interface OnItemCheckListener {
        fun onItemView(id: String?,position: Int)
        fun onItemEdit(id: String?)
        fun onItemDelete(id: String?)
        fun onItemLongPress(id: String?)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_re_route_trip_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return try {
            tripList.size
        } catch (e: Exception) {
            0
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val trips = tripList[position]

        holder.itemView.txtName.text = trips?.user?.name
        holder.itemView.txtPickUpLocation.text = trips?.origin_street_address
        holder.itemView.txtDropLocation.text =trips?.destination_street_address
        holder.itemView.txtTrip.text = "Trips# "+(position+1)
        holder.itemView.txtPickupTime.text = trips?.trip_time

        holder.itemView.setOnClickListener {
            onItemCheckListener.onItemView(trips?.id.toString(),position)
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}