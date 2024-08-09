package com.macrew.medirydes.dashboard.model.future

import android.os.Parcel
import android.os.Parcelable


data class Vehicle (
	val id : Int?,
	val name : String?,
	val color : String?,
	val year : String?,
	val make : String?,
	val vehicle_model : String?,
	val capacity : Int?,
	val position : Int?,
	val radio : String?,
	val vin : String?,
	val license_number : String?,
	val license_expire : String?,
	val insurance_expire : String?,
	val created_at : String?,
	val updated_at : String?
):Parcelable {
	constructor(parcel: Parcel) : this(
		parcel.readValue(Int::class.java.classLoader) as? Int,
		parcel.readString(),
		parcel.readString(),
		parcel.readString(),
		parcel.readString(),
		parcel.readString(),
		parcel.readValue(Int::class.java.classLoader) as? Int,
		parcel.readValue(Int::class.java.classLoader) as? Int,
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
		parcel.writeValue(id)
		parcel.writeString(name)
		parcel.writeString(color)
		parcel.writeString(year)
		parcel.writeString(make)
		parcel.writeString(vehicle_model)
		parcel.writeValue(capacity)
		parcel.writeValue(position)
		parcel.writeString(radio)
		parcel.writeString(vin)
		parcel.writeString(license_number)
		parcel.writeString(license_expire)
		parcel.writeString(insurance_expire)
		parcel.writeString(created_at)
		parcel.writeString(updated_at)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<Vehicle> {
		override fun createFromParcel(parcel: Parcel): Vehicle {
			return Vehicle(parcel)
		}

		override fun newArray(size: Int): Array<Vehicle?> {
			return arrayOfNulls(size)
		}
	}
}