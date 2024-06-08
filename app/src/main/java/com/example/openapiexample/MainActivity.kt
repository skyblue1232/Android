package com.example.openapiexample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var mAdapter: RecyclerView.Adapter<*>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager

        val retrofitClient = RetrofitClient.getInstance()
        val retrofitInterface = RetrofitClient.getRetrofitInterface()

        val API_KEY = "f5eef3421c602c6cb7ea224104795888"
        retrofitInterface.getBoxOffice(API_KEY, "20120101").enqueue(object : Callback<Result> {
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                if (response.isSuccessful && response.body() != null) {
                    val result = response.body()
                    val boxOfficeResult = result?.boxOfficeResult
                    if (boxOfficeResult != null) {
                        Log.d("retrofit", "Data fetch success")
                        for (item in boxOfficeResult.dailyBoxOfficeList!!) {
                            Log.d("retrofit", "Movie: ${item.movieNm}")
                        }
                        mAdapter = MovieAdapter(boxOfficeResult.dailyBoxOfficeList)
                        recyclerView.adapter = mAdapter
                    } else {
                        Log.d("retrofit", "BoxOfficeResult is null")
                    }
                } else {
                    Log.d("retrofit", "Response is not successful or body is null")
                }
            }

            override fun onFailure(call: Call<Result>, t: Throwable) {
                Log.d("retrofit", t.message ?: "Unknown error")
            }
        })
    }
}
