package com.macrew.medirydes.login.model.company

import android.os.Parcel
import android.os.Parcelable

data class CompanyModel(
    val message: String?,
    val StatusCode: Int?,
    val result: CompanyObjectModel?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readParcelable(CompanyObjectModel::class.java.classLoader)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(message)
        parcel.writeValue(StatusCode)
        parcel.writeParcelable(result, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CompanyModel> {
        override fun createFromParcel(parcel: Parcel): CompanyModel {
            return CompanyModel(parcel)
        }

        override fun newArray(size: Int): Array<CompanyModel?> {
            return arrayOfNulls(size)
        }
    }
}