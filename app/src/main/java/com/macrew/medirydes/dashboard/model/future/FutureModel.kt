package com.macrew.medirydes.dashboard.model.future

import android.os.Parcel
import android.os.Parcelable

data class FutureModel(
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

    companion object CREATOR : Parcelable.Creator<FutureModel> {
        override fun createFromParcel(parcel: Parcel): FutureModel {
            return FutureModel(parcel)
        }

        override fun newArray(size: Int): Array<FutureModel?> {
            return arrayOfNulls(size)
        }
    }
}