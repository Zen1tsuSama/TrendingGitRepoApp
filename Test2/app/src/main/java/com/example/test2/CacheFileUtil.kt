package com.example.test2

import android.content.Context
import android.content.SharedPreferences
import java.io.*

class CacheFileUtil {
    companion object Factory {
        fun writeToCache(jsonStr: String, context: Context) {
            try {
                val file = File(context.cacheDir.absolutePath, "myCachedData.txt")
                val fileOutputStream = FileOutputStream(file)
                val outputWriter = OutputStreamWriter(fileOutputStream)
                outputWriter.write(jsonStr)
                outputWriter.close()
            }
            catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun readFromCache(context: Context): String {
            var string: String? = ""
            val stringBuilder = StringBuilder()

            try {
                val file = File(context.cacheDir, "myCachedData.txt")
                val fileInputStream = FileInputStream(file)
                val reader = BufferedReader(InputStreamReader(fileInputStream))

                while (true) {
                    try {
                        if (reader.readLine().also { string = it } == null) break
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    stringBuilder.append(string).append("\n")
                }

                fileInputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return stringBuilder.toString()
        }

        fun clearCacheFileData(myPref: SharedPreferences, context: Context) {
            myPref.edit().clear().apply()
            val file = File(context.cacheDir, "myCachedData.txt")
            val fileWriter = PrintWriter(file)
            fileWriter.print("")
            fileWriter.close()
        }
    }
}