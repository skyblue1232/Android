package com.example.flo

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {

    lateinit var binding : ActivitySongBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)  //선언
        binding = ActivitySongBinding.inflate(layoutInflater)   //초기화
        setContentView(binding.root)
        binding.songDownIb.setOnClickListener {
            finish()
        }
        binding.songPlayButtonIv.setOnClickListener {
            setPlayerStatus(false)
        }
        binding.songPauseIv.setOnClickListener {
            setPlayerStatus(true)
        }
        if(intent.hasExtra("title")&&intent.hasExtra("singer")){
            binding.songTitleTv.text=intent.getStringExtra("title")
            binding.songSingerTv.text=intent.getStringExtra("singer")
        }
    }
    fun setPlayerStatus(isPlaying : Boolean) {
        if(isPlaying){
            binding.songPlayButtonIv.visibility= View.VISIBLE
            binding.songPauseIv.visibility=View.GONE
        }
        else {
            binding.songPlayButtonIv.visibility=View.GONE
            binding.songPauseIv.visibility=View.VISIBLE
        }
    }
}