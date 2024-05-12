package com.example.flo

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding
import com.google.gson.Gson

class SongActivity : AppCompatActivity() {

    lateinit var binding : ActivitySongBinding
    lateinit var song: Song
    lateinit var timer:Timer
    private var mediaPlayer: MediaPlayer? = null
    private var gson: Gson = Gson()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent.hasExtra("title") &&intent.hasExtra("singer")){
            binding.songMusicTitleTv.text=intent.getStringExtra("title")
            binding.musicSingerNameTv.text=intent.getStringExtra("singer")
        }

        binding.songDownIb.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java).apply{
                putExtra("songsong", binding.songMusicTitleTv.text.toString())
            }
            setResult(Activity.RESULT_OK, intent)
            if(!isFinishing) finish()
        }

        initPlayPauseBtn()
        initRepeatBtn()
        initRandomBtn()
        initSong()
        setPlayer(song)
    }
    private fun initRepeatBtn(){
        with(binding){
            musicRepeatOffIv.setOnClickListener {
                musicRepeatOffIv.visibility=View.GONE
                musicRepeatOnIv.visibility=View.VISIBLE
            }
            musicRepeatOnIv.setOnClickListener {
                musicRepeatOnIv.visibility=View.GONE
                musicRepeatOffIv.visibility=View.VISIBLE
            }
        }
    }
    private fun initPlayPauseBtn(){
        with(binding){
            musicMiniplayerIv.setOnClickListener{
                musicMiniplayerIv.visibility= View.GONE
                musicMvpauseIv.visibility= View.VISIBLE
            }
            musicMvpauseIv.setOnClickListener{
                musicMiniplayerIv.visibility= View.VISIBLE
                musicMvpauseIv.visibility= View.GONE
            }
        }

        binding.songDownIb.setOnClickListener {
            finish()
        }
        binding.musicMiniplayerIv.setOnClickListener {
            setPlayerStatus(true)
        }
        binding.musicMvpauseIv.setOnClickListener {
            setPlayerStatus(false)
        }
        if(intent.hasExtra("title")&&intent.hasExtra("singer")){
            binding.songMusicTitleTv.text=intent.getStringExtra("title")
            binding.musicSingerNameTv.text=intent.getStringExtra("singer")
        }
    }
    private fun initRandomBtn(){
        with(binding){
            musicRandomOffIv.setOnClickListener {
                musicRandomOnIv.visibility = View.GONE
                musicRandomOffIv.visibility = View.VISIBLE
            }
            musicRandomOnIv.setOnClickListener {
                musicRandomOnIv.visibility = View.VISIBLE
                musicRandomOffIv.visibility = View.GONE
            }
        }
    }
    private fun setPlayerStatus(isPlaying : Boolean) {
        song.isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if(isPlaying){
            binding.musicMiniplayerIv.visibility= View.GONE
            binding.musicMvpauseIv.visibility=View.VISIBLE
            mediaPlayer?.start()
        }
        else {
            binding.musicMiniplayerIv.visibility=View.VISIBLE
            binding.musicMvpauseIv.visibility=View.GONE
            if(mediaPlayer?.isPlaying == true){
                mediaPlayer?.pause()
            }
        }
    }

    private fun initSong(){
        if(intent.hasExtra("title") && intent.hasExtra("singer")){
            song = Song(
                intent.getStringExtra("title")!!,
                intent.getStringExtra("singer")!!,
                intent.getIntExtra("second",0),
                intent.getIntExtra("playTime", 0),
                intent.getBooleanExtra("isPlaying",false),
                intent.getStringExtra("music")!!
            )
        }
        startTimer()
    }

    private fun setPlayer(song:Song){
        binding.songMusicTitleTv.text=intent.getStringExtra("title")!!
        binding.musicSingerNameTv.text=intent.getStringExtra("singer")!!
        binding.musicStartTimeTv.text = String.format("%02d:%02d", song.second / 60, song.second % 60)
        binding.musicEndTimeTv.text = String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)
        binding.musicPlayIngSb.progress = (song.second * 1000 / song.playTime)
        val music = resources.getIdentifier(song.music,"raw",this.packageName)
        mediaPlayer = MediaPlayer.create(this,music)
        setPlayerStatus(song.isPlaying)
    }

    private fun startTimer(){
        timer = Timer(song.playTime,song.isPlaying)
        timer.start()
    }

    inner class Timer(private val playTime: Int,var isPlaying: Boolean=true):Thread(){

        private var second : Int = 0
        private var mills: Float = 0f

        override fun run() {
            super.run()
            try {
                while (true) {

                    if (second >= playTime) {
                        break
                    }

                    if (isPlaying) {
                        sleep(50)
                        mills += 50

                        runOnUiThread {
                            binding.musicPlayIngSb.progress = ((mills / playTime) * 10).toInt()
                        }

                        if (mills % 1000 == 0f) {
                            runOnUiThread {
                                binding.musicStartTimeTv.text =
                                    String.format("%02d:%02d", second / 60, second % 60)
                            }
                            second++
                        }
                    }
                }
            }catch(e:InterruptedException){
                Log.d("Song", "Thread가 죽었습니다. ${e.message}")
            }
        }
    }

    // 사용자가 포커스를 잃었을 때 음악 중지.
    override fun onPause() {
        super.onPause()
        setPlayerStatus(false)
        song.second = ((binding.musicPlayIngSb.progress * song.playTime)/100)/1000
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit() //에디터
        val songJson = gson.toJson(song)
        editor.putString("songData", songJson)

        editor.apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
        mediaPlayer?.release() //미디어플레이어가 갖고 있던 리소스를 해제
        mediaPlayer = null //미디어 플레이어 해제
    }
}
