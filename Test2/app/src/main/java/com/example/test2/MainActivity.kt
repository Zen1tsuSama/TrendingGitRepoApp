package com.example.test2

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request

class MainActivity : AppCompatActivity() {

    var hasResult: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
//        supportActionBar?.setCustomView(R.layout.app_bar_layout)

        getTrendingRepoData().execute()
        val exampleList = generateDummyList(20)

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.adapter = RecyclerViewAdapter(exampleList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
    }

    internal inner class getTrendingRepoData : AsyncTask<Void, Void, String> () {

        override fun doInBackground(vararg params: Void?): String {
            val client = OkHttpClient()
            val url = "https://zen1tsusama.github.io/CV/hobbies.html"
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()

            if (response != null) {
                hasResult = true
            }

            return response.body()?.string().toString()
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            val textView: TextView = findViewById(R.id.text_view_id)
            if (hasResult) {
                textView.text = result
            } else {
                textView.text = "Failed"
            }

        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_resource_file, menu)
        return true
    }

    private fun generateDummyList(size: Int): List<ItemClass> {
        val list = ArrayList<ItemClass>()
        for (i in 0 until size) {
            val drawable = when (i % 3) {
                0 -> R.drawable.ic_launcher_background
                1 -> R.drawable.ic_launcher_foreground
                else -> R.drawable.ic_search_black_24dp
            }
            val item = ItemClass(drawable, "Item $i", "Resource data for $i",
                        "Description for $i", "C++", "1201", "101")
            list += item
        }
        return list
    }
}