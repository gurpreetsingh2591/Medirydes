package com.macrew.medirydes.dashboard.model.reports

import android.os.Parcel
import android.os.Parcelable

data class Pivot (
	val schedule_id : Int,
	val trip_id : Int
):Parcelable {
	constructor(parcel: Parcel) : this(
		parcel.readInt(),
		parcel.readInt()
	) {
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeInt(schedule_id)
		parcel.writeInt(trip_id)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<Pivot> {
		override fun createFromParcel(parcel: Parcel): Pivot {
			return Pivot(parcel)
		}

		override fun newArray(size: Int): Array<Pivot?> {
			return arrayOfNulls(size)
		}
	}
}