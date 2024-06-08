package com.example.openapiexample

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient private constructor() {

    companion object {
        private var instance: RetrofitClient? = null
        private val retrofitInterface: RetrofitInterface
        private const val baseUrl = "https://www.kobis.or.kr"

        init {
            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            retrofitInterface = retrofit.create(RetrofitInterface::class.java)
        }

        @Synchronized
        fun getInstance(): RetrofitClient {
            if (instance == null) {
                instance = RetrofitClient()
            }
            return instance!!
        }

        fun getRetrofitInterface(): RetrofitInterface {
            return retrofitInterface
        }
    }
}
