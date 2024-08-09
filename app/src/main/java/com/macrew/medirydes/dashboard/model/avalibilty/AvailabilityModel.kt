package com.macrew.medirydes.dashboard.model.avalibilty

import android.os.Parcel
import android.os.Parcelable

data class AvailabilityModel(
    val StatusCode: String?,
    val message: String?,
    val result: Result?
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(Result::class.java.classLoader)
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

    companion object CREATOR : Parcelable.Creator<AvailabilityModel> {
        override fun createFromParcel(parcel: Parcel): AvailabilityModel {
            return AvailabilityModel(parcel)
        }

        override fun newArray(size: Int): Array<AvailabilityModel?> {
            return arrayOfNulls(size)
        }
    }
}