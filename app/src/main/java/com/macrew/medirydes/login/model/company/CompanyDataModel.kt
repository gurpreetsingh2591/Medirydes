package com.macrew.medirydes.login.model.company

import android.os.Parcel
import android.os.Parcelable

data class CompanyDataModel(
    val id: String?,
    val company_code: String?,
    val company_name: String?,
    val phone: String?,
    val address: String?,
    val created_at: String?,
    val updated_at: String?,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(company_code)
        parcel.writeString(company_name)
        parcel.writeString(phone)
        parcel.writeString(address)
        parcel.writeString(created_at)
        parcel.writeString(updated_at)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CompanyDataModel> {
        override fun createFromParcel(parcel: Parcel): CompanyDataModel {
            return CompanyDataModel(parcel)
        }

        override fun newArray(size: Int): Array<CompanyDataModel?> {
            return arrayOfNulls(size)
        }
    }
}
