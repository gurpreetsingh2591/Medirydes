package com.macrew.medirydes.dashboard.model.currentSchedule

import android.os.Parcel
import android.os.Parcelable

data class CurrentScheduleModel(
    val StatusCode: String?,
    val message: String?,
    val result: ListObject?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(ListObject::class.java.classLoader)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(StatusCode)
        parcel.writeString(message)
        parcel.writeParcelable(result, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CurrentScheduleModel> {
        override fun createFromParcel(parcel: Parcel): CurrentScheduleModel {
            return CurrentScheduleModel(parcel)
        }

        override fun newArray(size: Int): Array<CurrentScheduleModel?> {
            return arrayOfNulls(size)
        }
    }
}