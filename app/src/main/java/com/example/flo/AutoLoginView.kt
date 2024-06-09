package com.example.flo

interface AutoLoginView {
    fun onAutoSuccess()
    fun onAutoFailure(code: Int, message: String)
}