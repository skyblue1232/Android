package com.example.flo

import android.telecom.Call
import android.util.Log
import androidx.sqlite.db.SupportSQLiteOpenHelper
import okhttp3.Callback
import okhttp3.Response

class AlbumService {

    private lateinit var albumView: AlbumView

    private lateinit var albumSongView: AlbumSongView

    fun setAlbumView(albumView: AlbumView){
        Log.d("HOME/ALBUM-RESPONSE", "setting")
        this.albumView = albumView
    }

    fun setAlbumSongView(albumSongView: AlbumSongView){
        Log.d("SongFragment-RESPONSE", "setting")
        this.albumSongView = albumSongView
    }

    fun getAlbums() {
        val albumService = getRetrofit().create(AlbumRetrofitInterface::class.java)

        albumView.onGetAlbumLoading()

        albumService.getAlbums().enqueue(object : Callback<AlbumResponse> {
            override fun onResponse(call: Call<AlbumResponse>, response: Response<AlbumResponse>) {
                Log.d("HOME/ALBUM-RESPONSE", "여기일수도")
                if (response.isSuccessful && response.code() == 200) {
                    val albumResponse: AlbumResponse = response.body()!!

                    Log.d("HOME/ALBUM-RESPONSE", "$response")

                    when (val code = albumResponse.code) {
                        1000 -> {
                            albumView.onGetAlbumSuccess(code, albumResponse.result!!)
                        }

                        else -> albumView.onGetAlbumFailure(code, albumResponse.message)
                    }
                }else{
                    Log.d("HOME/ALBUM-RESPONSE", "${response.isSuccessful}  ${response.code()}")
                }
            }

            override fun onFailure(call: Call<AlbumResponse>, t: Throwable) {
                Log.d("HOME/ALBUM-RESPONSE", "$t")
                albumView.onGetAlbumFailure(400, "네트워크 오류가 발생했습니다.")
            }
        })
    }

    fun getAlbumSong(albumIdx: Int) {
        val albumService = getRetrofit().create(AlbumRetrofitInterface::class.java)

        albumSongView.onGetAlbumSongLoading()

        albumService.getAlbum(albumIdx).enqueue(object :
            SupportSQLiteOpenHelper.Callback<AlbumSongRes> {

            override fun onResponse(call: Call<AlbumSongRes>, response: Response<AlbumSongRes>) {
                Log.d("SongFragment-RESPONSE", "여기일수도")
                if (response.isSuccessful && response.code() == 200) {
                    val albumSongResponse: AlbumSongRes = response.body()!!

                    Log.d("SongFragment-RESPONSE", "$response")

                    when (val code = albumSongResponse.code) {
                        1000 -> {
                            albumSongView.onGetAlbumSongSuccess(code, albumSongResponse)
                        }

                        else -> albumSongView.onGetAlbumSongFailure(code, albumSongResponse.message)
                    }
                }else{
                    Log.d("SongFragment-RESPONSE", "${response.isSuccessful}  ${response.code()}")
                }
            }

            override fun onFailure(call: Call<AlbumSongRes>, t: Throwable) {
                Log.d("SongFragment-RESPONSE", "$t")
                albumView.onGetAlbumFailure(400, "네트워크 오류가 발생했습니다.")
            }
        })
    }

}