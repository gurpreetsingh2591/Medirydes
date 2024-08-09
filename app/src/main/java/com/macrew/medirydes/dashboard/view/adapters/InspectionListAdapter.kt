package com.macrew.medirydes.dashboard.view.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.macrew.medirydes.R
import com.macrew.medirydes.dashboard.model.InspectionJson
import com.macrew.medirydes.utils.Static
import kotlinx.android.synthetic.main.item_inspection_list.view.*

class InspectionListAdapter(
    private val inspectionJson: ArrayList<InspectionJson?>,
    private val activity: Activity,
    private val onItemCheckListener: OnItemCheckListener
) : RecyclerView.Adapter<InspectionListAdapter.MyViewHolder>() {
    val static = Static()
    var mSelectedItem = -1

    interface OnItemCheckListener {
        fun onItemView(id: String?,value:String?)
        fun onItemEdit(id: String?)
        fun onItemDelete(id: String?)
    }

    fun setSelection(position: Int) {
        mSelectedItem = position
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
                .inflate(R.layout.item_inspection_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return try {
            inspectionJson.size
        } catch (e: Exception) {
            0
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val inspection = inspectionJson[position]

        holder.itemView.txtName.text = inspection?.name

        holder.itemView.rgInspection.setOnCheckedChangeListener { radioGroup, i ->
            val rb = holder.itemView.findViewById(i) as RadioButton

            if (rb.text.equals("Yes")) {
               onItemCheckListener.onItemView(inspection?.slug,"1")
                holder.itemView.rbYes.setTextColor(Color.WHITE)
                holder.itemView.rbNo.setTextColor(Color.BLACK)
            } else {
                onItemCheckListener.onItemView(inspection?.slug,"0")
                holder.itemView.rbYes.setTextColor(Color.BLACK)
                holder.itemView.rbNo.setTextColor(Color.WHITE)
            }
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


}