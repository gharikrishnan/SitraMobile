package com.sitramobile.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sitramobile.db.dao.DynamicDataDao


@Database(entities = [DynamicData::class], version = 1,exportSchema = false)

@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase()
{

    abstract fun getDynamicDataDao() : DynamicDataDao

    companion object{

        @Volatile
        private var instance : AppDatabase? =null
        private val LOCK = Any()

        operator fun invoke (context: Context)= instance?: synchronized(LOCK)
        {
            instance?: buildDatabse(context)
        }

        private fun buildDatabse(context: Context) = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "Sitra_db").build()
            //.fallbackToDestructiveMigration() //destory old database data

    }

}