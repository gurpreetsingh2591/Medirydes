package com.macrew.medirydes.dashboard.model.profile

import android.os.Parcel
import android.os.Parcelable

data class User_detail(
    val id: Int?,
    val user_id: Int?,
    val gender: String?,
    val age: String?,
    val image_file_name: String?,
    val image_file_size: Int?,
    val image_content_type: String?,
    val image_updated_at: String?,
    val created_at: String?,
    val updated_at: String?,
    val image_url: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeValue(user_id)
        parcel.writeString(gender)
        parcel.writeString(age)
        parcel.writeString(image_file_name)
        parcel.writeValue(image_file_size)
        parcel.writeString(image_content_type)
        parcel.writeString(image_updated_at)
        parcel.writeString(created_at)
        parcel.writeString(updated_at)
        parcel.writeString(image_url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User_detail> {
        override fun createFromParcel(parcel: Parcel): User_detail {
            return User_detail(parcel)
        }

        override fun newArray(size: Int): Array<User_detail?> {
            return arrayOfNulls(size)
        }
    }
}