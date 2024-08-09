package com.macrew.medirydes.dashboard.model.avalibilty

import android.os.Parcel
import android.os.Parcelable

data class Result(
    val availability: Availability?
):Parcelable {
    constructor(parcel: Parcel) : this(parcel.readParcelable(Availability::class.java.classLoader)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(availability, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Result> {
        override fun createFromParcel(parcel: Parcel): Result {
            return Result(parcel)
        }

        override fun newArray(size: Int): Array<Result?> {
            return arrayOfNulls(size)
        }
    }
}