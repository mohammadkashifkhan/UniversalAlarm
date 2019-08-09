package com.mdkashif.universalarm.alarm.misc.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(tableName = "Days", foreignKeys = [ForeignKey(entity = TimingsModel::class, parentColumns = ["id"], childColumns = ["alarmId"], onUpdate = CASCADE, onDelete = CASCADE)])
data class DaysModel(
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0,

        @ColumnInfo(name = "alarmId", index = true)
        var fkAlarmId: Long = 0,

        @ColumnInfo(name = "day")
        var repeatDay: String = "") : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readLong(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeLong(fkAlarmId)
        parcel.writeString(repeatDay)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DaysModel> {
        override fun createFromParcel(parcel: Parcel): DaysModel {
            return DaysModel(parcel)
        }

        override fun newArray(size: Int): Array<DaysModel?> {
            return arrayOfNulls(size)
        }
    }

}