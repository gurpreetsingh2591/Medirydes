package com.macrew.medirydes.dashboard.model.future

import android.os.Parcel
import android.os.Parcelable

data class Trips(
    val id: Int,
    val user_id: Int?,
    val requested_trip: String?,
    val trip_date: String?,
    val trip_time: String?,
    val confirmation_number: String?,
    val origin_name: String?,
    val origin_street_address: String?,
    val origin_suite: String?,
    val origin_city: String?,
    val origin_state: String?,
    val origin_postal_code: String?,
    val origin_county: String?,
    val origin_phone: String?,
    val origin_longitude: String?,
    val origin_latitude: String?,
    val origin_comments: String?,
    val destination_name: String?,
    val destination_street_address: String?,
    val destination_suite: String?,
    val destination_city: String?,
    val destination_state: String?,
    val destination_postal_code: String?,
    val destination_county: String?,
    val destination_phone: String?,
    val destination_comments: String?,
    val destination_longitude: String?,
    val destination_latitude: String?,
    val status: String?,
    val trip_miles: String?,
    val trip_duration: String?,
    val trip_revenue: String?,
    val pivot: Pivot?,
    val user: User?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
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
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(Pivot::class.java.classLoader),
        parcel.readParcelable(User::class.java.classLoader)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeValue(user_id)
        parcel.writeString(requested_trip)
        parcel.writeString(trip_date)
        parcel.writeString(trip_time)
        parcel.writeString(confirmation_number)
        parcel.writeString(origin_name)
        parcel.writeString(origin_street_address)
        parcel.writeString(origin_suite)
        parcel.writeString(origin_city)
        parcel.writeString(origin_state)
        parcel.writeString(origin_postal_code)
        parcel.writeString(origin_county)
        parcel.writeString(origin_phone)
        parcel.writeString(origin_longitude)
        parcel.writeString(origin_latitude)
        parcel.writeString(origin_comments)
        parcel.writeString(destination_name)
        parcel.writeString(destination_street_address)
        parcel.writeString(destination_suite)
        parcel.writeString(destination_city)
        parcel.writeString(destination_state)
        parcel.writeString(destination_postal_code)
        parcel.writeString(destination_county)
        parcel.writeString(destination_phone)
        parcel.writeString(destination_comments)
        parcel.writeString(destination_longitude)
        parcel.writeString(destination_latitude)
        parcel.writeString(status)
        parcel.writeString(trip_miles)
        parcel.writeString(trip_duration)
        parcel.writeString(trip_revenue)
        parcel.writeParcelable(pivot, flags)
        parcel.writeParcelable(user, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Trips> {
        override fun createFromParcel(parcel: Parcel): Trips {
            return Trips(parcel)
        }

        override fun newArray(size: Int): Array<Trips?> {
            return arrayOfNulls(size)
        }
    }
}