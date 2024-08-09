package com.macrew.medirydes.dashboard.model.shiftDetail

import android.os.Parcel
import android.os.Parcelable

data class TripSequenceDataModel(
    val Schedule_id: String?,
    val rider_name: String?,
    val rider_id: String?,
    val pickup_time: String?,
    val pickup_location: String?,
    val drop_off_location: String?,
    val note: String?,
    val trips: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
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
        parcel.writeString(Schedule_id)
        parcel.writeString(rider_name)
        parcel.writeString(rider_id)
        parcel.writeString(pickup_time)
        parcel.writeString(pickup_location)
        parcel.writeString(drop_off_location)
        parcel.writeString(note)
        parcel.writeString(trips)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TripSequenceDataModel> {
        override fun createFromParcel(parcel: Parcel): TripSequenceDataModel {
            return TripSequenceDataModel(parcel)
        }

        override fun newArray(size: Int): Array<TripSequenceDataModel?> {
            return arrayOfNulls(size)
        }
    }
}