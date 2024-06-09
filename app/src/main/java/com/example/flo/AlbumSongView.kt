package com.example.flo

interface AlbumSongView {

    fun onGetAlbumSongLoading()
    fun onGetAlbumSongSuccess(code: Int, result: AlbumSongRes)
    fun onGetAlbumSongFailure(code: Int, message: String)

}