package com.macrew.medirydes.dashboard.model.reports


import android.os.Parcel
import android.os.Parcelable

data class ScheduleData(
    val id: Int?,
    val user_id: String?,
    val notification_count: String?,
    val day: String?,
    val date: String?,
    val start_time: String?,
    val end_time: String?,
    val vehicle_id: Int?,
    val schedule_status: String?,
    val shift_begin_in: String?,
    val created_at: String?,
    val updated_at: String?,
    val vehicle: Vehicle?,
    val trips: ArrayList<Trips?>,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(Vehicle::class.java.classLoader),
        parcel.readArrayList(Trips::class.java.classLoader) as ArrayList<Trips?>,
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(user_id)
        parcel.writeString(notification_count)
        parcel.writeString(day)
        parcel.writeString(date)
        parcel.writeString(start_time)
        parcel.writeString(end_time)
        parcel.writeValue(vehicle_id)
        parcel.writeString(schedule_status)
        parcel.writeString(shift_begin_in)
        parcel.writeString(created_at)
        parcel.writeString(updated_at)
        parcel.writeParcelable(vehicle, flags)
        parcel.writeList(trips)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ScheduleData> {
        override fun createFromParcel(parcel: Parcel): ScheduleData {
            return ScheduleData(parcel)
        }

        override fun newArray(size: Int): Array<ScheduleData?> {
            return arrayOfNulls(size)
        }
    }
}