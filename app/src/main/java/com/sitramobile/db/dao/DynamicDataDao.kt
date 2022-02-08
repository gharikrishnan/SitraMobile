package com.sitramobile.db.dao

import androidx.room.*
import com.sitramobile.db.DynamicData

@Dao
interface DynamicDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllDynamicData(mDynamicData: List<DynamicData>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(mDynamicData: DynamicData): Long

    @Query("SELECT * from DynamicData ORDER BY id ASC")
    suspend fun getAllDynamicData() : List<DynamicData>

    @Query("SELECT * from DynamicData WHERE id=:id ORDER BY id ASC")
    suspend fun getAllDynamicDatabyId(id:String) : List<DynamicData>

    @Query("DELETE FROM DynamicData where id=:id")
    suspend fun deleteAllDynamicData(id:String):Int

    @Query("DELETE FROM DynamicData")
    suspend fun deleteAllDynamicData()

    @Update
    suspend fun update(vararg mDynamicData: DynamicData): Int

    @Delete
    suspend fun delete(vararg mDynamicData: DynamicData): Int

    @Query("UPDATE DynamicData SET data=:data Where custid=:Cid AND screenNo=:Sno ")
    fun updatedata(Cid:String,Sno:String,data:String)

    @Query("SELECT id from DynamicData Where custid=:Cid AND screenNo=:Sno LIMIT 1")
    fun filterData(Cid:String,Sno:String): Long?

}