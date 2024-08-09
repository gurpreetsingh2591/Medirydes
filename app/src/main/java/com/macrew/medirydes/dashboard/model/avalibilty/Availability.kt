package com.macrew.medirydes.dashboard.model.avalibilty

import android.os.Parcel
import android.os.Parcelable

data class Availability(
    val created_at: String?,
    val id: Int?,
    val updated_at: String?,
    val user_id: Int?,
    val work_days: String?
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(created_at)
        parcel.writeValue(id)
        parcel.writeString(updated_at)
        parcel.writeValue(user_id)
        parcel.writeString(work_days)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Availability> {
        override fun createFromParcel(parcel: Parcel): Availability {
            return Availability(parcel)
        }

        override fun newArray(size: Int): Array<Availability?> {
            return arrayOfNulls(size)
        }
    }
}