package com.macrew.medirydes.dashboard.model.currentSchedule


import android.os.Parcel
import android.os.Parcelable

data class ListObject(
    val schedules: ArrayList<ScheduleData?>,
    val server_time: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readArrayList(ScheduleData::class.java.classLoader) as ArrayList<ScheduleData?>,
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeList(schedules)
        parcel.writeString(server_time)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ListObject> {
        override fun createFromParcel(parcel: Parcel): ListObject {
            return ListObject(parcel)
        }

        override fun newArray(size: Int): Array<ListObject?> {
            return arrayOfNulls(size)
        }
    }
}