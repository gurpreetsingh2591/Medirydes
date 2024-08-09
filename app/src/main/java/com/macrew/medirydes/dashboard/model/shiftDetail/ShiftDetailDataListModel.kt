package com.macrew.medirydes.dashboard.model.shiftDetail

import android.os.Parcel
import android.os.Parcelable

data class ShiftDetailDataListModel(
    val Trip_sequence: ArrayList<TripSequenceDataModel?>,
    val Reroute_trip: ArrayList<ReRouteTripDataModel?>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readArrayList(TripSequenceDataModel::class.java.classLoader) as ArrayList<TripSequenceDataModel?>,
        parcel.readArrayList(ReRouteTripDataModel::class.java.classLoader) as ArrayList<ReRouteTripDataModel?>
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeList(Trip_sequence)
        parcel.writeList(Reroute_trip)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ShiftDetailDataListModel> {
        override fun createFromParcel(parcel: Parcel): ShiftDetailDataListModel {
            return ShiftDetailDataListModel(parcel)
        }

        override fun newArray(size: Int): Array<ShiftDetailDataListModel?> {
            return arrayOfNulls(size)
        }
    }
}