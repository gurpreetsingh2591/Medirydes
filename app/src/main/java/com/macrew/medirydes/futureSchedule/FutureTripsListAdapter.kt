package com.macrew.medirydes.futureSchedule

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.macrew.medirydes.R
import com.macrew.medirydes.dashboard.model.future.Trips

import com.macrew.medirydes.utils.SharedPrefrencesUtils
import com.macrew.medirydes.utils.Static
import kotlinx.android.synthetic.main.item_trip_sequence_list.view.*

class FutureTripsListAdapter(
    private val tripList: ArrayList<Trips?>,
    private val activity: Activity,
    private val onItemCheckListener: OnItemCheckListener
) : RecyclerView.Adapter<FutureTripsListAdapter.MyViewHolder>() {
    val static = Static()

    interface OnItemCheckListener {
        fun onItemView(id: String?, position: Int)
        fun onItemEdit(id: String?)
        fun onItemDelete(id: String?)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_trip_sequence_list, parent, false)
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
        holder.itemView.txtPickUpLocation.text =  trips?.origin_name + ", " + trips?.origin_street_address + ", " +
                trips?.origin_suite + ", " + trips?.origin_city + ", " + trips?.origin_state + ", " +
                trips?.origin_postal_code + ", " + trips?.origin_county
        holder.itemView.txtMobility.text ="Mobility Type: "+ SharedPrefrencesUtils.getMobilityType()
        holder.itemView.txtDropLocation.text =
            trips?.destination_name + ", " + trips?.destination_street_address + ", " +
                    trips?.destination_suite + ", " + trips?.destination_city + ", " + trips?.destination_state
        trips?.destination_postal_code + ", " + trips?.destination_county

        holder.itemView.txtTrip.text = "Trip#: " +trips?.id

        holder.itemView.txtPickupTime.text =trips?.trip_time

        holder.itemView.txtNote.text = trips?.origin_comments

        holder.itemView.setOnClickListener {
            onItemCheckListener.onItemView(trips?.id.toString(), position)
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}