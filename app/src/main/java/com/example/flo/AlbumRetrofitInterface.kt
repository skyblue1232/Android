package com.example.flo

import android.telecom.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface AlbumRetrofitInterface {

    @GET("/albums")
    fun getAlbums(): Call<AlbumResponse>

    @GET("/albums/{albumIdx}")
    fun getAlbum(@Path("albumIdx") albumIdx: Int) : Call<AlbumSongRes>

}