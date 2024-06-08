package com.example.openapiexample

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitInterface {

    // http://www.kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json
    @GET("/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json?")
    fun getBoxOffice(@Query("key") key: String, @Query("targetDt") targetDt: String): Call<Result>
}