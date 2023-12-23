package com.crc.golf.base

import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CommonUtils {
    fun getFileName(swingType : String) : String {
        val strCurrentTime : String = getCurrentDateTime()

        val strFileName = "$swingType$strCurrentTime"

        return strFileName
    }

    fun getCurrentDateTime(): String {
        val dateFormat = SimpleDateFormat("yyMMddHHmmss", Locale.getDefault())
        return dateFormat.format(Date())
    }

    fun writeTextToFile(strMessage : String, strFileName : String) {
        val filePath = MyApplication.ApplicationContext().filesDir.path + "/$strFileName.txt"
        val file = File(filePath)
        val fileWriter = FileWriter(file, true)
        val bufferedWriter = BufferedWriter(fileWriter)

        bufferedWriter.append(strMessage)
        bufferedWriter.newLine()
        bufferedWriter.close()
    }
}