package com.crc.golf.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [GolfEntity::class], version = 1)
@TypeConverters(MeasurementTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getGolfDao() : GolfDao

    companion object {
        val databaseName = "db_golf"
        var appDatabase : AppDatabase? = null

        fun getInstance(context: Context) : AppDatabase? {
            if(appDatabase == null) {
                appDatabase = Room.databaseBuilder(context, AppDatabase::class.java, databaseName).build()
            }
            return appDatabase
        }
    }
}