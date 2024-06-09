package com.example.flo

interface LoginView {
    fun onLoginSuccess(code : Int, result : Result)
    fun onLoginFailure(message : String)
    fun onPlayClick(albumId: Int)
    fun onSelectClick(isSelectOn: Boolean)
}