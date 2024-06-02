package com.example.flo

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding
import com.google.gson.Gson
import java.util.Timer

class SongActivity : AppCompatActivity() {

    lateinit var binding: ActivitySongBinding
    private var timer: Timer? = null
    private var mediaPlayer: MediaPlayer? = null
    private val gson: Gson = Gson()

    private val songs = arrayListOf<Song>()
    private lateinit var songDB: SongDatabase
    private var nowPos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPlayList()
        initSong()
        initClickListener()
    }

    override fun onPause() {
        super.onPause()
        if (songs.isNotEmpty()) {
            val currentSong = songs[nowPos]
            currentSong.second =
                ((binding.musicPlayIngSb.progress * currentSong.playTime) / 100) / 1000
            currentSong.isPlaying = false
            setPlayerStatus(false)

            val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
            sharedPreferences.edit().apply {
                putInt("songId", currentSong.id)
                apply()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.interrupt()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun initPlayList() {
        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getSongs())
    }

    private fun initClickListener() {
        binding.songDownIb.setOnClickListener {
            finish()
        }
        binding.musicMiniplayerIv.setOnClickListener {
            setPlayerStatus(true)
        }

        binding.musicMvpauseIv.setOnClickListener {
            setPlayerStatus(false)
        }
        binding.musicNextIv.setOnClickListener {
            moveSong(+1)
        }
        binding.musicPreviousIv.setOnClickListener {
            moveSong(-1)
        }

        binding.musicLoveIv.setOnClickListener {
            setLike(songs[nowPos].isLike)
        }
    }

    private fun initSong() {
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)

        if (songs.isNotEmpty()) {
            nowPos = getPlayingSongPosition(songId)
            Log.d("now Song ID", songs[nowPos].id.toString())
            startTimer()
            setPlayer(songs[nowPos])
        } else {
            Log.e("SongActivity", "No songs found in the playlist.")
            showToast("No songs found in the playlist.")
            finish()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun setLike(isLike: Boolean) {
        if (songs.isNotEmpty()) {
            val currentSong = songs[nowPos]
            currentSong.isLike = !isLike
            songDB.songDao().updateIsLikeById(!isLike, currentSong.id)

            binding.musicLoveIv.setImageResource(
                if (!isLike) R.drawable.ic_my_like_on else R.drawable.ic_my_like_off
            )
            initToast(if (!isLike) "Liked!" else "Unliked!")
        }
    }

    private fun initToast(likeText: String) {
        val inflater = layoutInflater
        val layout = inflater.inflate(
            R.layout.custom_toast,
            binding.root.findViewById(R.id.custom_toast_container)
        )
        val text = layout.findViewById<TextView>(R.id.likeTv)
        text.text = likeText

        val toast = Toast(this)
        toast.duration = Toast.LENGTH_LONG
        toast.view = layout
        toast.setGravity(Gravity.FILL_HORIZONTAL or Gravity.BOTTOM, 0, 100)
        toast.show()
    }

    private fun moveSong(direct: Int) {
        if (songs.isNotEmpty()) {
            if (nowPos + direct < 0) {
                showToast("First song")
                return
            }

            if (nowPos + direct >= songs.size) {
                showToast("Last song")
                return
            }

            nowPos += direct
            timer?.interrupt()
            startTimer()

            mediaPlayer?.release()
            mediaPlayer = null

            setPlayer(songs[nowPos])
        }
    }

    private fun getPlayingSongPosition(songId: Int): Int {
        return songs.indexOfFirst { it.id == songId }.takeIf { it != -1 } ?: 0
    }

    private fun setPlayer(song: Song) {
        binding.songMusicTitleTv.text = song.title
        binding.musicSingerNameTv.text = song.singer
        binding.musicStartTimeTv.text =
            String.format("%02d:%02d", song.second / 60, song.second % 60)
        binding.musicEndTimeTv.text =
            String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)
        binding.IUMusicAlbumIv.setImageResource(song.coverImg ?: R.drawable.img_album_exp2)
        binding.musicPlayIngSb.progress = (song.second * 1000 / song.playTime)

        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this, music)

        binding.musicLoveIv.setImageResource(
            if (song.isLike) R.drawable.ic_my_like_on else R.drawable.ic_my_like_off
        )

        setPlayerStatus(song.isPlaying)
    }

    private fun setPlayerStatus(isPlaying: Boolean) {
        if (songs.isNotEmpty()) {
            val currentSong = songs[nowPos]
            currentSong.isPlaying = isPlaying
            timer?.isPlaying = isPlaying

            if (isPlaying) {
                binding.musicMiniplayerIv.visibility = View.GONE
                binding.musicMvpauseIv.visibility = View.VISIBLE
                mediaPlayer?.start()
            } else {
                binding.musicMiniplayerIv.visibility = View.VISIBLE
                binding.musicMvpauseIv.visibility = View.GONE
                mediaPlayer?.pause()
            }
        }
    }

    private fun startTimer() {
        timer = Timer(songs[nowPos].playTime, songs[nowPos].isPlaying)
        timer?.start()
    }

    inner class Timer(private val playTime: Int, var isPlaying: Boolean = true) :
        Thread() {
        private var second: Int = 0
        private var mills: Float = 0f
        override fun run() {
            super.run()
            try {
                while (true) {
                    if (second >= playTime) break

                    if (isPlaying) {
                        sleep(50)
                        mills += 50
                        runOnUiThread {
                            binding.musicPlayIngSb.progress = ((mills / playTime) * 100).toInt()
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
            } catch (e: InterruptedException) {
                Log.d("Song", "Timer thread interrupted: ${e.message}")
            }
        }
    }
}