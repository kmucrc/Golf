package com.crc.golf.base

import android.annotation.SuppressLint
import android.content.Context
import com.crc.golf.db.GolfEntity

class Constants {
    companion object {
        const val SWING_TYPE_DRAW = "draw"
        const val SWING_TYPE_FADE = "fade"
        const val SWING_TYPE_LAYUP = "layup"
        const val SWING_TYPE_PUNCH = "punch"
        const val SWING_TYPE_CHIP = "chip"
        const val SWING_TYPE_PITCH = "pitch"
        const val SWING_TYPE_FLOP = "flop"


        const val POINT_AREA_CHEST : Int = 1
        const val POINT_AREA_HAND : Int = 2
        const val POINT_AREA_WRIST : Int = 3
        const val POINT_AREA_STOMACH : Int = 4
        const val POINT_AREA_HEAD : Int = 5


        const val STATUS_INDEX_IDLE : Int = 0
        const val STATUS_INDEX_CONNECT : Int = 1
        const val STATUS_INDEX_WAITING : Int = 2
        const val STATUS_INDEX_SWING : Int = 3
        const val STATUS_INDEX_RECEIVE : Int = 4
        const val STATUS_INDEX_RAWDATA : Int = 5

        const val NETWORK_IP = "192.168.4.1" // Replace with the IP address you want to connect to
        const val NETWORK_PORT = 80 // Replace with the port number you want to connect to

        val chest = Measurement(mean = 0.0, max = 0.0)
        val hand = Measurement(mean = 0.0, max = 0.0)
        val wrist = Measurement(mean = 0.0, max = 0.0)
        val stomach = Measurement(mean = 0.0, max = 0.0)
        val head = Measurement(mean = 0.0, max = 0.0)

        var golfData: GolfEntity = GolfEntity(
            1,
            "0",
            false,
            SWING_TYPE_PUNCH,
            chest,
            hand,
            wrist,
            stomach,
            head)

        var nStatusIndex : Int = STATUS_INDEX_IDLE
        var strSaveFileName : String = ""
        var mapSelIndex = mutableMapOf<Int, Pair<String, String>>()

        var recentCreateddate = ""
        var recentbDownloaded = false
        var recentType = SWING_TYPE_PUNCH
        var recentChest = Measurement(mean = 0.0, max = 0.0)
        var recentHand = Measurement(mean = 0.0, max = 0.0)
        var recentWrist = Measurement(mean = 0.0, max = 0.0)
        var recentStomach = Measurement(mean = 0.0, max = 0.0)
        var recentHead = Measurement(mean = 0.0, max = 0.0)
    }
}