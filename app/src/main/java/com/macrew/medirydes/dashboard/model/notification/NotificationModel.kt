package com.macrew.medirydes.dashboard.model.notification

import android.os.Parcel
import android.os.Parcelable

data class NotificationModel(
    val ResponseMessage: String?,
    val responsecode: Int?,
    val responsebody: ArrayList<NotificationDataModel?>?
) :Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readArrayList(NotificationDataModel::class.java.classLoader) as ArrayList<NotificationDataModel?>,
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(ResponseMessage)
        parcel.writeValue(responsecode)
        parcel.writeList(responsebody)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NotificationModel> {
        override fun createFromParcel(parcel: Parcel): NotificationModel {
            return NotificationModel(parcel)
        }

        override fun newArray(size: Int): Array<NotificationModel?> {
            return arrayOfNulls(size)
        }
    }
}