package com.example.flo

interface AlbumView {
    fun onGetAlbumLoading()
    fun onGetAlbumSuccess(code: Int, result: AlbumResult)
    fun onGetAlbumFailure(code: Int, message: String)
}