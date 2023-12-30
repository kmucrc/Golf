package com.crc.golf.db

import androidx.room.TypeConverter
import com.crc.golf.base.Measurement
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MeasurementTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromMeasurement(measurement: Measurement): String {
        return gson.toJson(measurement)
    }

    @TypeConverter
    fun toMeasurement(measurementString: String): Measurement {
        val type = object : TypeToken<Measurement>() {}.type
        return gson.fromJson(measurementString, type)
    }
}