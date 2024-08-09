package com.macrew.medirydes.dashboard.model.notification

import android.os.Parcel
import android.os.Parcelable

data class NotificationDataModel(
    val notification_id: String?,
    val date: String?,
    val time: String?,
    val title: String?,
    val description: String?,

    ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(notification_id)
        parcel.writeString(date)
        parcel.writeString(time)
        parcel.writeString(title)
        parcel.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NotificationDataModel> {
        override fun createFromParcel(parcel: Parcel): NotificationDataModel {
            return NotificationDataModel(parcel)
        }

        override fun newArray(size: Int): Array<NotificationDataModel?> {
            return arrayOfNulls(size)
        }
    }
}
