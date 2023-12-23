package com.crc.golf

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.crc.golf.base.Constants
import com.crc.golf.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    var mapDetailIndex = mutableMapOf<Int, Pair<String, String>>()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mapDetailIndex[1] = Pair("16", "18")
        mapDetailIndex[2] = Pair("26", "28")
        mapDetailIndex[3] = Pair("36", "38")
        mapDetailIndex[4] = Pair("46", "48")
        mapDetailIndex[5] = Pair("56", "58")

        binding.ivDetailHuman.setOnTouchListener { view, motionEvent ->
            if(motionEvent.action == MotionEvent.ACTION_DOWN) {
                val touchX = motionEvent.x
                val touchY = motionEvent.y

                val chestArea = Rect(390, 380, 730, 820) // Example coordinates
                val handArea = Rect(770, 1120, 870, 1300) // Example coordinates
                val wristArea = Rect(770, 770, 910, 1050) // Example coordinates
                val stomachArea = Rect(400, 850, 730, 1060) // Example coordinates
                val headArea = Rect(470, 40, 680, 300) // Example coordinates

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
                Log.e("eleutheria", "touchX : $touchX, touchY : $touchY")
            }
            true
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
                val dataItem = Constants.golfData.chest
                dataItem.let {
                    strSimilarity = it.mean.toString()
                    strError = it.max.toString()
                }
                // Change the background image
                dialogRoot.setBackgroundResource(R.drawable.popup_1)
            }
            Constants.POINT_AREA_HAND -> {
                val dataItem = Constants.golfData.hand
                dataItem.let {
                    strSimilarity = it.mean.toString()
                    strError = it.max.toString()
                }
                // Change the background image
                dialogRoot.setBackgroundResource(R.drawable.popup_2)
            }
            Constants.POINT_AREA_WRIST -> {
                val dataItem = Constants.golfData.wrist
                dataItem.let {
                    strSimilarity = it.mean.toString()
                    strError = it.max.toString()
                }
                // Change the background image
                dialogRoot.setBackgroundResource(R.drawable.popup_3)
            }
            Constants.POINT_AREA_STOMACH -> {
                val dataItem = Constants.golfData.stomach
                dataItem.let {
                    strSimilarity = it.mean.toString()
                    strError = it.max.toString()
                }
                // Change the background image
                dialogRoot.setBackgroundResource(R.drawable.popup_4)
            }
            Constants.POINT_AREA_HEAD -> {
                val dataItem = Constants.golfData.head
                dataItem.let {
                    strSimilarity = it.mean.toString()
                    strError = it.max.toString()
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
}