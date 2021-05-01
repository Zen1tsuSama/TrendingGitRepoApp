package com.example.test2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.okhttp.Callback
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response
import java.io.*
import java.lang.reflect.Type


class MainActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var myPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myPref = applicationContext.getSharedPreferences("MyPref", Context.MODE_PRIVATE)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)

        checkSharedPrefs()
        val cachedStr = CacheFileUtil.readFromCache(applicationContext)

        if (cachedStr.isEmpty()) {
            if (isOnline()) {
                fetchJson()
            } else {
                startActivity(Intent(this@MainActivity, NoInternetActivity::class.java))
                finish()
            }
        } else {
            val collectionType: Type = object : TypeToken<List<TrendingFeed?>?>() {}.type
            val trendingFeed: List<TrendingFeed> = Gson()
                .fromJson(cachedStr, collectionType) as List<TrendingFeed>

            runOnUiThread {
                recyclerView.adapter = RecyclerViewAdapter(trendingFeed, true)
            }
        }
    }

    private fun fetchJson() {
        val url =
            "https://private-anon-d3c85ad60d-githubtrendingapi.apiary-mock.com/repositories?language=&since=daily&spoken_language_code="
        val request = Request.Builder().url(url).build()

        OkHttpClient().newCall(request).enqueue(object: Callback {
            override fun onResponse(response: Response?) {
                val jsonStr = response?.body()?.string().toString()
                val collectionType: Type = object : TypeToken<List<TrendingFeed?>?>() {}.type
                val trendingFeed: List<TrendingFeed> = Gson()
                        .fromJson(jsonStr, collectionType) as List<TrendingFeed>

                runOnUiThread {
                    recyclerView.adapter = RecyclerViewAdapter(trendingFeed, false)
                }

                CacheFileUtil.clearCacheFileData(myPref, applicationContext)
                CacheFileUtil.writeToCache(jsonStr, applicationContext)
                myPref.edit().putString("insertTime", System.currentTimeMillis().toString()).apply()
            }

            override fun onFailure(request: Request?, e: IOException?) {
                startActivity(Intent(this@MainActivity, NoInternetActivity::class.java))
                finish()
            }
        })
    }

    private fun isOnline(): Boolean {
        val connectivityManager =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null
        }

        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return true
            }
        }

        return false
    }

    private fun checkSharedPrefs() {
        val insertTimeStr = myPref.getString("insertTime", "0")
        val currentTime = System.currentTimeMillis()
        try {
            val insertTime = insertTimeStr?.toLong()

            if (currentTime - insertTime!! > 7200000) {
                CacheFileUtil.clearCacheFileData(myPref, applicationContext)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_resource_file, menu)
        return true
    }
}