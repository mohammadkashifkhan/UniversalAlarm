package com.mdkashif.universalarm.alarm.misc.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Locations")
data class LocationsModel(
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0,

        var address: String = "",

        var city: String = "",

        var latitude: Double = 0.0,

        var longitude: Double = 0.0,

        var note: String = "",

        @ColumnInfo(name = "pid")
        var pIntentRequestCode: Long = 0,

        var status: Boolean = false) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.readString(),
            parcel.readLong(),
            parcel.readByte() != 0.toByte())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(address)
        parcel.writeString(city)
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
        parcel.writeString(note)
        parcel.writeLong(pIntentRequestCode)
        parcel.writeByte(if (status) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LocationsModel> {
        override fun createFromParcel(parcel: Parcel): LocationsModel {
            return LocationsModel(parcel)
        }

        override fun newArray(size: Int): Array<LocationsModel?> {
            return arrayOfNulls(size)
        }
    }
}