package com.mdkashif.universalarm.alarm.misc.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "Timings")
data class TimingsModel(
        @PrimaryKey(autoGenerate = true)
        var id: Long = 0,

        var hour: String = "",

        var minute: String = "",

        var note: String = "",

        @ColumnInfo(name = "type")
        var alarmType: String = "",

        var repeat: Boolean = false,

        var status: Boolean = false,

        @Ignore
        var repeatDays: List<DaysModel>? = null,

        @ColumnInfo(name = "pid")
        var pIntentRequestCode: Long = 0) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            listOf<DaysModel>().apply {
                parcel.readList(this, DaysModel::class.java.classLoader)
            },
            parcel.readLong())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(hour)
        parcel.writeString(minute)
        parcel.writeString(note)
        parcel.writeString(alarmType)
        parcel.writeByte(if (repeat) 1 else 0)
        parcel.writeByte(if (status) 1 else 0)
        parcel.writeList(repeatDays)
        parcel.writeLong(pIntentRequestCode)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TimingsModel> {
        override fun createFromParcel(parcel: Parcel): TimingsModel {
            return TimingsModel(parcel)
        }

        override fun newArray(size: Int): Array<TimingsModel?> {
            return arrayOfNulls(size)
        }
    }
}