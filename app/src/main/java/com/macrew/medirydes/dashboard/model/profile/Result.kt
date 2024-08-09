package com.macrew.medirydes.dashboard.model.profile


import android.os.Parcel
import android.os.Parcelable

data class Result(
    val user: User?
) : Parcelable {
    constructor(parcel: Parcel) :
            this(parcel.readParcelable(User::class.java.classLoader)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(user, flags)
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