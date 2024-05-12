package com.example.flo

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar

import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding
import com.google.gson.Gson


class SongActivity : AppCompatActivity() {

    //전역 변수
    lateinit var binding : ActivitySongBinding
    lateinit var song : Song

    private val songs = listOf(
        Song(playTime = 120, isPlaying = false),
        Song(playTime = 180, isPlaying = true),
        // Add more songs as needed
    )
    private var nowPos = 0 // Current position in the songs list

    private val timer = Timer(songs[nowPos])



    //lateinit var timer : Timer(songs[nowPos].playTime, songs[nowPos].isPlaying)
    private var mediaPlayer : MediaPlayer? = null
    private var gson: Gson = Gson()

//    var nowPos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSong()
        initClickListener()
        setPlayer(song)


    }

    private fun initClickListener(){
        binding.songDownIb.setOnClickListener {
            finish()
        }

        binding.musicMiniplayerIv.setOnClickListener {
            setPlayerStatus(true)
        }

        binding.musicMvpauseIv.setOnClickListener {
            setPlayerStatus(false)
        }
//
//        binding.musicNextIv.setOnClickListener {
//            moveSong(+1)
//        }
//
//        binding.musicPreviousIv.setOnClickListener {
//            moveSong(-1)
//        }
//
//        binding.musicLoveIv.setOnClickListener {
//            setLike(songs[nowPos].isLike)
//        }
    }

    private fun initSong(){
        if(intent.hasExtra("title") && intent.hasExtra("singer") ){
            song = Song(
                intent.getStringExtra("title")!!,
                intent.getStringExtra("singer")!!,
                intent.getIntExtra("second", 0),
                intent.getIntExtra("playTime", 0),
                intent.getBooleanExtra("isplaying", false),
                intent.getStringExtra("music")!!
            )
        }
        startTimer()
    }

//    private fun setPlayer(song: Song){
//        binding.songMusicTitleTv.text = song.title
//        binding.musicSingerNameTv.text = song.singer
//        binding.musicStartTimeTv.text = String.format("%02d:%02d",song.second / 60, song.second % 60)
//        binding.musicEndTimeTv.text = String.format("%02d:%02d",song.playTime / 60, song.playTime % 60)
//        binding.musicPlayIngSb.progress = (song.second * 1000 / song.playTime)
//
//        val music = resources.getIdentifier(song.music, "raw", this.packageName)
//        mediaPlayer = MediaPlayer.create(this, music)
//
//        setPlayerStatus(song.isPlaying)
//
//    }
private fun setPlayer(song: Song) {
    binding.songMusicTitleTv.text = song.title
    binding.musicSingerNameTv.text = song.singer
    binding.musicStartTimeTv.text = formatTimeInSeconds(song.second)
    binding.musicEndTimeTv.text = formatTimeInSeconds(song.playTime)
    binding.musicPlayIngSb.max = song.playTime
    binding.musicPlayIngSb.progress = song.second

    val music = resources.getIdentifier(song.music, "raw", this.packageName)
    mediaPlayer = MediaPlayer.create(this, music)
    mediaPlayer?.setOnCompletionListener {
        setPlayerStatus(false)
    }
    mediaPlayer?.setOnCompletionListener {
        setPlayerStatus(false)
    }
}


    private fun setPlayerStatus (isPlaying : Boolean){
        timer.isPlaying = isPlaying

        if (isPlaying) {
            binding.musicMiniplayerIv.visibility = View.GONE
            binding.musicMvpauseIv.visibility = View.VISIBLE
            mediaPlayer?.start()

            // Add SeekBar listener
            binding.musicPlayIngSb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        mediaPlayer?.seekTo(progress * 1000)
                        mediaPlayer?.start()
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}
                override fun onStopTrackingTouch(seekBar: SeekBar) {}
            })

            updateProgressBar()
            updateTimeLabel()

        } else {
            binding.musicMiniplayerIv.visibility = View.VISIBLE
            binding.musicMvpauseIv.visibility = View.GONE
            if(mediaPlayer?.isPlaying == true){
                mediaPlayer?.pause()
            }

        // Remove the SeekBar listener
        binding.musicPlayIngSb.setOnSeekBarChangeListener(null)
        }

    }

    private fun updateProgressBar() {
        runOnUiThread {
            binding.musicPlayIngSb.progress = (mediaPlayer?.currentPosition ?: 0) / 1000
        }
    }

    private fun updateTimeLabel() {
        runOnUiThread {
            binding.musicStartTimeTv.text = formatTimeInSeconds((mediaPlayer?.currentPosition ?: 0) / 1000)
        }
    }

    private fun formatTimeInSeconds(seconds: Int): String {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return "%02d:%02d".format(minutes, remainingSeconds)
    }
    private fun startTimer(){
        timer.start()
    }

    private inner class Timer(private val song: Song) : Thread() {
        private var elapsedTimeInMillis: Long = 0L
        private var _isPlaying: Boolean = song.isPlaying
        var isPlaying: Boolean
            get() = _isPlaying
            set(value) {
                _isPlaying = value
            }

        override fun run() {
            super.run()
            try {
                while (elapsedTimeInMillis < song.playTime * 1000L) {
                    if (song.isPlaying) {
                        sleep(50)
                        elapsedTimeInMillis += 50L
                        updateProgressBar()
                        updateTimeLabel()
                    }
                }
            } catch (e: InterruptedException) {
                handleInterruptedException(e)
            }
        }

        private fun updateProgressBar() {
            runOnUiThread {
                binding.musicPlayIngSb.progress = ((elapsedTimeInMillis / (song.playTime * 1000f)).toInt())
            }
        }

        private fun updateTimeLabel() {
            runOnUiThread {
                binding.musicStartTimeTv.text = formatTimeInSeconds((elapsedTimeInMillis / 1000).toInt())
            }
        }

        private fun formatTimeInSeconds(seconds: Int): String {
            val minutes = seconds / 60
            val remainingSeconds = seconds % 60
            return "%02d:%02d".format(minutes, remainingSeconds)
        }

        private fun handleInterruptedException(e: InterruptedException) {
            Log.d("Song", "Thread가 죽었습니다. ${e.message}")
            // Add any additional error handling logic here
        }
    }

    // 사용자가 포커스를 잃었을 때 음악 중지.
    override fun onPause() {
        super.onPause()
        setPlayerStatus(false)
        song.second = ((binding.musicPlayIngSb.progress * song.playTime)/100)/1000
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit() // 에디터
        val songJson = gson.toJson(song)
        editor.putString("songData", songJson)

        editor.apply()
    }


    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
        mediaPlayer?.release() // 미디어플레이어가 갖고 있던 리소스를 해제
        mediaPlayer = null // 미디어 플레이어 해제
    }

}