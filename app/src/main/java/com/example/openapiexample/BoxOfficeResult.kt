package com.example.openapiexample

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BoxOfficeResult(
    @SerializedName("boxofficeType")
    @Expose
    var boxofficeType: String? = null,

    @SerializedName("showRange")
    @Expose
    var showRange: String? = null,

    @SerializedName("yearDailyTime")
    @Expose
    var yearDailyTime: String? = null,

    @SerializedName("dailyBoxOfficeList")
    @Expose
    var dailyBoxOfficeList: List<DailyBoxOfficeList>? = null
)
