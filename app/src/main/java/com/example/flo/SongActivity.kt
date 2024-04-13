package com.example.flo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {

    lateinit var binding : ActivitySongBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent.hasExtra("title") && intent.hasExtra("singer")){
            binding.songMusicTitleTv.text = intent.getStringExtra("title")
            binding.songSingerNameTv.text = intent.getStringExtra("singer")
        }

        binding.songDownIb.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("songsong", binding.songMusicTitleTv.text.toString())
            }
            setResult(Activity.RESULT_OK, intent)
            if (!isFinishing) finish()
        }
            initplayPauseBtn()
        }


    private fun initplayPauseBtn() {
        with(binding){
            songMiniplayerIv.setOnClickListener {
                songMiniplayerIv.visibility= View.GONE
                songPauseIv.visibility= View.VISIBLE
            }
            songPauseIv.setOnClickListener {
                songMiniplayerIv.visibility= View.VISIBLE
                songPauseIv.visibility= View.GONE
            }
        }
    }
}