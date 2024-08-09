package com.macrew.medirydes.dashboard.model.order

import android.os.Parcel
import android.os.Parcelable

data class OrdersDataModel(
    val schedule_id: String?,
    val date: String?,
    val day: String?,
    val start_time: String?,
    val end_time: String?,
    val vehicle_id: String?,
    val trips: String?,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(schedule_id)
        parcel.writeString(date)
        parcel.writeString(day)
        parcel.writeString(start_time)
        parcel.writeString(end_time)
        parcel.writeString(vehicle_id)
        parcel.writeString(trips)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OrdersDataModel> {
        override fun createFromParcel(parcel: Parcel): OrdersDataModel {
            return OrdersDataModel(parcel)
        }

        override fun newArray(size: Int): Array<OrdersDataModel?> {
            return arrayOfNulls(size)
        }
    }
}