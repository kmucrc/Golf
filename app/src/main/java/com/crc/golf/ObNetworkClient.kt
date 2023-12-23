package com.crc.golf

import android.content.Context
import com.crc.golf.base.CommonUtils
import com.crc.golf.base.Constants
import com.crc.golf.base.Measurement
import com.crc.golf.db.AppDatabase
import com.crc.golf.db.GolfDao
import com.crc.golf.db.GolfEntity
import com.google.gson.Gson
import com.google.gson.JsonParser
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.IOException
import java.io.OutputStreamWriter
import java.net.InetSocketAddress
import java.net.Socket

object ObNetworkClient {
    private var socket: Socket? = null
    private var output: BufferedWriter? = null
    private var input: BufferedReader? = null
    private var isConnected = false

    private var commonUtils = CommonUtils()


    lateinit var db : AppDatabase
    lateinit var swingDao: GolfDao

    fun connect(ipAddress: String, portNumber: Int, context: Context) : Boolean {

        db = AppDatabase.getInstance(context)!!
        swingDao = db.getGolfDao()

        val timeoutMillis = 5000 // Timeout in milliseconds (5 seconds)

        socket = Socket()
        return try {
            if (!isConnected) {
                val socketAddress = InetSocketAddress(Constants.NETWORK_IP, Constants.NETWORK_PORT)
                socket!!.connect(socketAddress, timeoutMillis)
                output = BufferedWriter(OutputStreamWriter(socket!!.getOutputStream()))
                isConnected = true
            }
            true
        } catch (e: IOException) {
            e.printStackTrace()
            // Handle connection error
            false
        }
    }

    fun sendString(data: String) {
        if (isConnected) {
            output!!.write(data)
            output!!.flush()
        }

        while(true) {
            // Receive data from the socket
            val inputStream = socket!!.getInputStream()
            val buffer = ByteArray(1024)

            val bytesRead = inputStream.read(buffer)
            val bufferData = String(buffer, 0, bytesRead)

            if(Constants.nStatusIndex == Constants.STATUS_INDEX_RAWDATA) {
                commonUtils.writeTextToFile(bufferData, Constants.strSaveFileName)
            } else if(Constants.nStatusIndex == Constants.STATUS_INDEX_RECEIVE) {
                if (bufferData.contains("Chest")) {
                    val startIndex = bufferData.indexOf('{')
                    if (startIndex != -1) {
                        // Extract the JSON part from the raw data
                        val jsonData = bufferData.substring(startIndex)

                        val gson = Gson()
                        val jsonObject = JsonParser.parseString(jsonData).asJsonObject

                        val chestJson = jsonObject.getAsJsonObject("Chest")
                        val chestData = gson.fromJson(chestJson, Measurement::class.java)

                        val handJson = jsonObject.getAsJsonObject("Hand")
                        val handData = gson.fromJson(handJson, Measurement::class.java)

                        val wristJson = jsonObject.getAsJsonObject("Wrist")
                        val wristData = gson.fromJson(wristJson, Measurement::class.java)

                        val stomachJson = jsonObject.getAsJsonObject("Stomach")
                        val stomachData = gson.fromJson(stomachJson, Measurement::class.java)

                        val headJson = jsonObject.getAsJsonObject("Head")
                        val headData = gson.fromJson(headJson, Measurement::class.java)

                        Constants.recentChest = chestData
                        Constants.recentHand = handData
                        Constants.recentWrist = wristData
                        Constants.recentStomach = stomachData
                        Constants.recentHead = headData

                        Constants.mapSelIndex[1] = Pair(chestData.mean.toString(), chestData.max.toString())
                        Constants.mapSelIndex[2] = Pair(handData.mean.toString(), handData.max.toString())
                        Constants.mapSelIndex[3] = Pair(wristData.mean.toString(), wristData.max.toString())
                        Constants.mapSelIndex[4] = Pair(stomachData.mean.toString(), stomachData.max.toString())
                        Constants.mapSelIndex[5] = Pair(headData.mean.toString(), headData.max.toString())

                        Thread {
                            swingDao.insertTodo(
                                GolfEntity(
                                    null,
                                    Constants.recentCreateddate,
                                    Constants.recentbDownloaded,
                                    Constants.recentType,
                                    Constants.recentChest,
                                    Constants.recentHand,
                                    Constants.recentWrist,
                                    Constants.recentStomach,
                                    Constants.recentHead
                                )
                            )
                        }.start()
                    }
                }
            }
        }
    }

    fun receiveString() : String? {
        val strResult = input?.readLine()

        if (isConnected) {
            return strResult
        } else {
            return null
        }
    }

    fun disconnect() {
        try {
            input?.close()
            output?.close()
            socket?.close()
            isConnected = false
        } catch (e: IOException) {
            e.printStackTrace()
            // Handle disconnection error
        }
    }
}