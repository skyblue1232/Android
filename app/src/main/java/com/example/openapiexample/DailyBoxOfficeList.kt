package com.example.openapiexample

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DailyBoxOfficeList(
    @SerializedName("rnum")
    @Expose
    var rnum: String? = null,

    @SerializedName("rank")
    @Expose
    var rank: String? = null,

    @SerializedName("rankInten")
    @Expose
    var rankInten: String? = null,

    @SerializedName("rankOldAndNew")
    @Expose
    var rankOldAndNew: String? = null,

    @SerializedName("movieCd")
    @Expose
    var movieCd: String? = null,

    @SerializedName("movieNm")
    @Expose
    var movieNm: String? = null,

    @SerializedName("openDt")
    @Expose
    var openDt: String? = null,

    @SerializedName("salesAmt")
    @Expose
    var salesAmt: String? = null,

    @SerializedName("salesShare")
    @Expose
    var salesShare: String? = null,

    @SerializedName("salesInten")
    @Expose
    var salesInten: String? = null,

    @SerializedName("salesChange")
    @Expose
    var salesChange: String? = null,

    @SerializedName("salesAcc")
    @Expose
    var salesAcc: String? = null,

    @SerializedName("audiCnt")
    @Expose
    var audiCnt: String? = null,

    @SerializedName("audiInten")
    @Expose
    var audiInten: String? = null,

    @SerializedName("audiChange")
    @Expose
    var audiChange: String? = null,

    @SerializedName("audiAcc")
    @Expose
    var audiAcc: String? = null,

    @SerializedName("scrnCnt")
    @Expose
    var scrnCnt: String? = null,

    @SerializedName("showCnt")
    @Expose
    var showCnt: String? = null
)
