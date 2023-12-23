package com.crc.golf

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Rect
import android.graphics.Typeface
import android.net.wifi.WifiManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import com.crc.golf.base.CommonUtils
import com.crc.golf.base.Constants
import com.crc.golf.base.Measurement
import com.crc.golf.databinding.ActivityMainBinding
import com.crc.golf.db.AppDatabase
import com.crc.golf.db.GolfDao
import com.crc.golf.db.GolfEntity
import com.google.gson.Gson
import com.google.gson.JsonParser
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketException
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding

    private var commonUtils = CommonUtils()

    lateinit var db : AppDatabase
    lateinit var swingDao: GolfDao

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getInstance(applicationContext)!!
        swingDao = db.getGolfDao()

        binding.ivHuman.setOnTouchListener { view, motionEvent ->
            if(motionEvent.action == MotionEvent.ACTION_DOWN) {
                val touchX = motionEvent.x
                val touchY = motionEvent.y

                val chestArea = Rect(420, 310, 720, 680) // Example coordinates
                val handArea = Rect(700, 910, 830, 1060) // Example coordinates
                val wristArea = Rect(750, 620, 850, 850) // Example coordinates
                val stomachArea = Rect(420, 690, 720, 900) // Example coordinates
                val headArea = Rect(490, 50, 680, 250) // Example coordinates

                if (chestArea.contains(touchX.toInt(), touchY.toInt())) {
                    showCustomDialog(Constants.POINT_AREA_CHEST)
                } else if (handArea.contains(touchX.toInt(), touchY.toInt())) {
                    showCustomDialog(Constants.POINT_AREA_HAND)
                } else if (wristArea.contains(touchX.toInt(), touchY.toInt())) {
                    showCustomDialog(Constants.POINT_AREA_WRIST)
                } else if (stomachArea.contains(touchX.toInt(), touchY.toInt())) {
                    showCustomDialog(Constants.POINT_AREA_STOMACH)
                } else if (headArea.contains(touchX.toInt(), touchY.toInt())) {
                    showCustomDialog(Constants.POINT_AREA_HEAD)
                }
            }
            true
        }

        binding.btDraw.setOnClickListener(this)
        binding.btFade.setOnClickListener(this)
        binding.btLayup.setOnClickListener(this)
        binding.btPunch.setOnClickListener(this)
        binding.btChip.setOnClickListener(this)
        binding.btPitch.setOnClickListener(this)
        binding.btFlop.setOnClickListener(this)
        binding.btRawData.setOnClickListener(this)
        binding.btConnect.setOnClickListener(this)
        binding.btStart.setOnClickListener(this)
        binding.btEnd.setOnClickListener(this)
        binding.btData.setOnClickListener(this)

        Constants.mapSelIndex[1] = Pair("13", "15")
        Constants.mapSelIndex[2] = Pair("23", "25")
        Constants.mapSelIndex[3] = Pair("33", "35")
        Constants.mapSelIndex[4] = Pair("43", "45")
        Constants.mapSelIndex[5] = Pair("53", "55")
    }

    override fun onDestroy() {
        super.onDestroy()
        ObNetworkClient.disconnect()
    }

    override fun onClick(p0: View?) {
        var strStartName = ""
        var strCreatedData = ""

        when(p0!!.id) {
            R.id.btDraw -> {
                strCreatedData = commonUtils.getCurrentDateTime()

                Constants.nStatusIndex = Constants.STATUS_INDEX_SWING
                strStartName = "${Constants.SWING_TYPE_DRAW}$strCreatedData"
                sendMessage("start_$strStartName")
                saveRecentData(strCreatedData, Constants.SWING_TYPE_DRAW)
            }
            R.id.btFade -> {
                strCreatedData = commonUtils.getCurrentDateTime()

                Constants.nStatusIndex = Constants.STATUS_INDEX_SWING
                strStartName = "${Constants.SWING_TYPE_FADE}$strCreatedData"
                sendMessage("start_$strStartName")
                saveRecentData(strCreatedData, Constants.SWING_TYPE_FADE)
            }
            R.id.btLayup -> {
                strCreatedData = commonUtils.getCurrentDateTime()

                Constants.nStatusIndex = Constants.STATUS_INDEX_SWING
                strStartName = "${Constants.SWING_TYPE_LAYUP}$strCreatedData"
                sendMessage("start_$strStartName")
                saveRecentData(strCreatedData, Constants.SWING_TYPE_LAYUP)
            }
            R.id.btPunch -> {
                strCreatedData = commonUtils.getCurrentDateTime()

                Constants.nStatusIndex = Constants.STATUS_INDEX_SWING
                strStartName = "${Constants.SWING_TYPE_PUNCH}$strCreatedData"
                sendMessage("start_$strStartName")
                saveRecentData(strCreatedData, Constants.SWING_TYPE_PUNCH)
            }
            R.id.btChip -> {
                strCreatedData = commonUtils.getCurrentDateTime()

                Constants.nStatusIndex = Constants.STATUS_INDEX_SWING
                strStartName = "${Constants.SWING_TYPE_CHIP}$strCreatedData"
                sendMessage("start_$strStartName")
                saveRecentData(strCreatedData, Constants.SWING_TYPE_CHIP)
            }
            R.id.btPitch -> {
                strCreatedData = commonUtils.getCurrentDateTime()

                Constants.nStatusIndex = Constants.STATUS_INDEX_SWING
                strStartName = "${Constants.SWING_TYPE_PITCH}$strCreatedData"
                sendMessage("start_$strStartName")
                saveRecentData(strCreatedData, Constants.SWING_TYPE_PITCH)
            }
            R.id.btFlop -> {
                strCreatedData = commonUtils.getCurrentDateTime()

                Constants.nStatusIndex = Constants.STATUS_INDEX_SWING
                strStartName = "${Constants.SWING_TYPE_FLOP}$strCreatedData"
                sendMessage("start_$strStartName")
                saveRecentData(strCreatedData, Constants.SWING_TYPE_FLOP)
            }
            R.id.btRawData -> {
                connectClient()
            }
            R.id.btConnect -> {
                Thread {
                    ObNetworkClient.connect(Constants.NETWORK_IP, Constants.NETWORK_PORT, applicationContext)
                }.start()
            }
            R.id.btStart -> {
                sendMessage("application")
            }
            R.id.btEnd -> {
                Constants.nStatusIndex = Constants.STATUS_INDEX_RECEIVE
                sendMessage("stop")
            }
            R.id.btData -> {
                val intent = Intent(this@MainActivity, DataActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun showCustomDialog(index : Int) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_custom, null)
        val dialogRoot = dialogView.findViewById<ConstraintLayout>(R.id.clDialog)

        var strMainMessage = "PERCENTAGE"
        var strSimilarity : String = "0"
        var strError : String = "0"

        when(index) {
            Constants.POINT_AREA_CHEST -> {
                val dataItem = Constants.mapSelIndex[Constants.POINT_AREA_CHEST]
                dataItem?.let {
                    strSimilarity = it.first
                    strError = it.second
                }
                // Change the background image
                dialogRoot.setBackgroundResource(R.drawable.popup_1)
            }
            Constants.POINT_AREA_HAND -> {
                val dataItem = Constants.mapSelIndex[Constants.POINT_AREA_HAND]
                dataItem?.let {
                    strSimilarity = it.first
                    strError = it.second
                }
                // Change the background image
                dialogRoot.setBackgroundResource(R.drawable.popup_2)
            }
            Constants.POINT_AREA_WRIST -> {
                val dataItem = Constants.mapSelIndex[Constants.POINT_AREA_WRIST]
                dataItem?.let {
                    strSimilarity = it.first
                    strError = it.second
                }
                // Change the background image
                dialogRoot.setBackgroundResource(R.drawable.popup_3)
            }
            Constants.POINT_AREA_STOMACH -> {
                val dataItem = Constants.mapSelIndex[Constants.POINT_AREA_STOMACH]
                dataItem?.let {
                    strSimilarity = it.first
                    strError = it.second
                }
                // Change the background image
                dialogRoot.setBackgroundResource(R.drawable.popup_4)
            }
            Constants.POINT_AREA_HEAD -> {
                val dataItem = Constants.mapSelIndex[Constants.POINT_AREA_HEAD]
                dataItem?.let {
                    strSimilarity = it.first
                    strError = it.second
                }
                // Change the background image
                dialogRoot.setBackgroundResource(R.drawable.popup_5)
            }
        }

        // Create the dialog
        val dialog = Dialog(this)
        dialog.setContentView(dialogView)
        dialog.setCancelable(true) // Set to false if you don't want it to be dismissible

        // Access TextViews if you need to set text dynamically
        val tvSimilarity = dialogView.findViewById<TextView>(R.id.tvSimilarity)
        val tvErrorRate = dialogView.findViewById<TextView>(R.id.tvErrorRate)

        val strSimilarityP = strSimilarity + getString(R.string.app_common_percentage)
        val strErrorP = strError + getString(R.string.app_common_percentage)
        tvSimilarity.text = strSimilarityP
        tvErrorRate.text = strErrorP

        // Show the dialog
        dialog.show()
    }

    private fun sendMessage(message: String) {
        Thread {
            ObNetworkClient.sendString(message)

            val response = ObNetworkClient.receiveString()
            if(response != null) {
                if (response.contains("Chest")) {
                    val startIndex = response.indexOf('{')
                    if (startIndex != -1) {
                        // Extract the JSON part from the raw data
                        val jsonData = response.substring(startIndex)
                        val modifiedJsonData = jsonData.replace("Back of Hand", "Hand")

                        val gson = Gson()
                        val jsonObject = JsonParser.parseString(modifiedJsonData).asJsonObject

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

                        // Assign the values to your variables
                        Constants.recentChest = chestData
                        Constants.recentHand = handData
                        Constants.recentWrist = wristData
                        Constants.recentStomach = stomachData
                        Constants.recentHead = headData

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
        }.start()
    }

    private fun saveRecentData(strFileName : String, strType : String) {
        Constants.recentCreateddate = strFileName
        Constants.recentbDownloaded = false
        Constants.recentType = strType
    }

    private fun connectClient() {
        Thread {
            val timeoutMillis = 5000

            val socket = Socket()

            try {
                val socketAddress = InetSocketAddress(Constants.NETWORK_IP, Constants.NETWORK_PORT)
                socket.connect(socketAddress, timeoutMillis)

                val writer = BufferedWriter(OutputStreamWriter(socket.getOutputStream()))
                writer.write("application")
                writer.flush()

                while(true) {
                    // Receive data from the socket
                    val inputStream = socket.getInputStream()
                    val buffer = ByteArray(1024)

                    val bytesRead = inputStream.read(buffer)
                    val byteData = if (bytesRead != buffer.size) buffer.copyOfRange(0, bytesRead) else buffer
                    val data = String(buffer, 0, bytesRead)


                }

            } catch (e: Exception) {
                println("Error during connect or transmission: ${e.message}")
            } finally {
                socket.close()
            }
        }.start()
    }

    fun insertTestData() {

        val createddate = commonUtils.getFileName(Constants.SWING_TYPE_PUNCH)
        val bDownloaded = false
        val strType = Constants.SWING_TYPE_PUNCH
        val chest = Measurement(mean = 0.0, max = 0.0)
        val hand = Measurement(mean = 0.0, max = 0.0)
        val wrist = Measurement(mean = 0.0, max = 0.0)
        val stomach = Measurement(mean = 0.0, max = 0.0)
        val head = Measurement(mean = 0.0, max = 0.0)

        Thread {
            swingDao.insertTodo(
                GolfEntity(
                    null,
                    createddate,
                    bDownloaded,
                    strType,
                    chest,
                    hand,
                    wrist,
                    stomach,
                    head
                )
            )
        }.start()
    }
}