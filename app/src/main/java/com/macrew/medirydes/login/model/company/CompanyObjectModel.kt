package com.macrew.medirydes.login.model.company

import android.os.Parcel
import android.os.Parcelable

data class CompanyObjectModel(
    val company: CompanyDataModel?,
) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readParcelable(CompanyDataModel::class.java.classLoader)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(company, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CompanyObjectModel> {
        override fun createFromParcel(parcel: Parcel): CompanyObjectModel {
            return CompanyObjectModel(parcel)
        }

        override fun newArray(size: Int): Array<CompanyObjectModel?> {
            return arrayOfNulls(size)
        }
    }
}