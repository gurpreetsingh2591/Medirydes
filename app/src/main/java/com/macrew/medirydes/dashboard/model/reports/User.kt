package com.macrew.medirydes.dashboard.model.reports

import android.os.Parcel
import android.os.Parcelable


data class User(
    val id: Int?,
    val name: String?,
    val username: String?,
    val email: String?,
    val phone: String?,
    val email_verified_at: String?,
    val is_phone_verified: String?,
    val status: String?,
    val device_token: String?,
    val isd_code: String?,
    val last_login: String?,
    val total_login: String?,
    val created_at: String?,
    val updated_at: String?,
    val deleted_at: String?,
    val user_detail: UserDetail?
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(UserDetail::class.java.classLoader),
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(name)
        parcel.writeString(username)
        parcel.writeString(email)
        parcel.writeString(phone)
        parcel.writeString(email_verified_at)
        parcel.writeString(is_phone_verified)
        parcel.writeString(status)
        parcel.writeString(device_token)
        parcel.writeString(isd_code)
        parcel.writeString(last_login)
        parcel.writeString(total_login)
        parcel.writeString(created_at)
        parcel.writeString(updated_at)
        parcel.writeString(deleted_at)
        parcel.writeParcelable(user_detail, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}