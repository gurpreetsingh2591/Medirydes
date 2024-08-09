package com.macrew.medirydes.dashboard.model.order

import android.os.Parcel
import android.os.Parcelable
import com.macrew.medirydes.dashboard.model.notification.NotificationDataModel

data class OrderModel(
    val ResponseMessage: String?,
    val responsecode: Int?,
    val responsebody: ArrayList<OrdersDataModel?>?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readArrayList(OrdersDataModel::class.java.classLoader) as ArrayList<OrdersDataModel?>,

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

    companion object CREATOR : Parcelable.Creator<OrderModel> {
        override fun createFromParcel(parcel: Parcel): OrderModel {
            return OrderModel(parcel)
        }

        override fun newArray(size: Int): Array<OrderModel?> {
            return arrayOfNulls(size)
        }
    }
}