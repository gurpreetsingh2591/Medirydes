package com.macrew.medirydes.dashboard.model.shiftDetail

import android.os.Parcel
import android.os.Parcelable


data class ShiftDetailModel (
    val ResponseMessage: String?,
    val responsecode: Int?,
    val responsebody:ShiftDetailDataListModel?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readParcelable(ShiftDetailDataListModel::class.java.classLoader)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(ResponseMessage)
        parcel.writeValue(responsecode)
        parcel.writeParcelable(responsebody, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ShiftDetailModel> {
        override fun createFromParcel(parcel: Parcel): ShiftDetailModel {
            return ShiftDetailModel(parcel)
        }

        override fun newArray(size: Int): Array<ShiftDetailModel?> {
            return arrayOfNulls(size)
        }
    }
}