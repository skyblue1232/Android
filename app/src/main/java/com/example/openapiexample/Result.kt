package com.example.openapiexample

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("boxOfficeResult")
    @Expose
    var boxOfficeResult: BoxOfficeResult?
)
