package com.macrew.medirydes.dashboard.model.profile

import android.os.Parcel
import android.os.Parcelable


data class ProfileModel(
    val message: String?,
    val StatusCode: String?,
    val result: Result?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(Result::class.java.classLoader)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(message)
        parcel.writeString(StatusCode)
        parcel.writeParcelable(result, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProfileModel> {
        override fun createFromParcel(parcel: Parcel): ProfileModel {
            return ProfileModel(parcel)
        }

        override fun newArray(size: Int): Array<ProfileModel?> {
            return arrayOfNulls(size)
        }
    }
}