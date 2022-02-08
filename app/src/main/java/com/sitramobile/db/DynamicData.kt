package com.sitramobile.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "DynamicData")
data class DynamicData(

    @ColumnInfo(name = "custid")
    var custid: String,
    @ColumnInfo(name = "screenNo")
    var screenNo: String,
    @ColumnInfo(name = "data")
    var data: String
    //@ColumnInfo(name = "createdDate")var createdDate: Date = Date(System.currentTimeMillis())
)
{
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}