package com.example.flo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {

    // lateinit은 선언은 지금하는데 초기화는 나중에 해준다는 의미이다.
    lateinit var binding: ActivitySongBinding
    lateinit var song: Song
    lateinit var timer: Timer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // inflate은 xml에 표기된 layout들을 메모리에 객체화시키는 행동
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSong()
        setPlayer(song)

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

        binding.musicMiniplayerIv.setOnClickListener{
            setPlayerStatus(true)
        }

        binding.musicMvpauseIv.setOnClickListener{
            setPlayerStatus(false)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
    }

    private fun initSong(){
        if(intent.hasExtra("title") &&intent.hasExtra("singer")){
            song = Song(
                intent.getStringExtra("title")!!,
                intent.getStringExtra("singer")!!,
                intent.getIntExtra("second",0),
                intent.getIntExtra("playTime",0),
                intent.getBooleanExtra("isPlaying",false)
            )
        }
        startTimer()
    }

    private fun setPlayer(song: Song){
        binding.songMusicTitleTv.text=intent.getStringExtra("title")!!
        binding.musicSingerNameTv.text=intent.getStringExtra("singer")!!
        binding.musicStartTimeTv.text= String.format("%02d:%02d",song.second / 60, song.second % 60)
        binding.musicEndTimeTv.text= String.format("%02d:%02d",song.playTime / 60, song.playTime % 60)
        binding.musicPlayIngSb.progress= (song.second * 1000 / song.playTime)

        setPlayerStatus(song.isPlaying)

    }

    private fun setPlayerStatus (isPlaying : Boolean){
        song.isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if(isPlaying){
            binding.musicMiniplayerIv.visibility= View.GONE
            binding.musicMvpauseIv.visibility= View.VISIBLE
        } else {
            binding.musicMiniplayerIv.visibility= View.VISIBLE
            binding.musicMvpauseIv.visibility= View.GONE
        }

    }

    private fun startTimer(){
        timer = Timer(song.playTime,song.isPlaying)
        timer.start()
    }

    inner class Timer(private val playTime: Int,var isPlaying: Boolean = true):Thread(){

        private var second : Int = 0
        private var mills: Float = 0f

        override fun run() {
            super.run()
            try {
                while (true){
                    if (second >= playTime){
                        break
                    }

                    if (isPlaying){
                        sleep(50)
                        mills += 50

                        // 현재 스레드가 UI 스레드라면 UI 자원을 사용하는 행동에 대해서는 즉시 실행된다는 것이고,
                        // 만약 현재 스레드가 UI 스레드가 아니라면 행동은 UI 스레드의 자원 사용 이벤트 큐에 들어가게 되는 것 입니다.
                        // Handler 사용해도 무관
                        runOnUiThread{
                            binding.musicPlayIngSb.progress= ((mills / playTime)*100).toInt()
                        }

                        if (mills % 1000 == 0f){
                            runOnUiThread{
                                binding.musicStartTimeTv.text= String.format("%02d:%02d",second / 60, second % 60)
                            }
                            second++
                        }

                    }

                }

            }catch (e: InterruptedException){
                Log.d("Song","${e.message}")
            }


        }
    }
}