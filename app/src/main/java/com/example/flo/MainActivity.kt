package com.example.flo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivityMainBinding
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private var song: Song = Song()
    private var gson: Gson = Gson()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_FLO)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inputDummySongs()
        inputDummyAlbums()
        initBottomNavigation()

        Log.d("Song", "제목 : ${song.title} / 가수 : ${song.singer}")
        binding.mainPlayerCl.setOnClickListener {
            val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
            editor.putInt("songId", song.id)
            editor.apply()
            val intent = Intent(this, SongActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)

        val songDB = SongDatabase.getInstance(this)!!

        song = if (songId == 0) {
            songDB.songDao().getSong(1)
        } else {
            songDB.songDao().getSong(songId)
        }

        Log.d("song ID", song.id.toString())
        setMiniPlayer(song)
    }

    private fun initBottomNavigation() {

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()

        binding.mainBnv.setOnItemSelectedListener { item ->
            when (item.itemId) {

                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lookFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LookFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.searchFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, SearchFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lockerFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LockerFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }

    private fun setMiniPlayer(song: Song) {
        binding.mainMiniplayerTitleTv.text = song.title
        binding.mainMiniplayerSingerTv.text = song.singer
        binding.mainProgressSb.progress = (song.second * 100000) / song.playTime
    }

    private fun inputDummySongs() {
        val songDB = SongDatabase.getInstance(this)!!
        val songs = songDB.songDao().getSongs()

        if (songs.isNotEmpty()) return

        songDB.songDao().insert(Song(1, "Lilac", "아이유 (IU)", 0, 150, false, "music_lilac", R.drawable.img_album_exp2, false
            )
        )

        songDB.songDao().insert(Song(2, "Flu", "아이유 (IU)", 0, 170, false, "music_flu", R.drawable.img_album_exp2, false,
            )
        )

        songDB.songDao().insert(Song(3, "Butter", "방탄소년단 (BTS)", 0, 190, false, "music_butter", R.drawable.img_album_exp, false
            )
        )

        songDB.songDao().insert(Song(4, "Next Level", "에스파 (AESPA)", 0, 210, false, "music_next", R.drawable.img_album_exp3, false
            )
        )


        songDB.songDao().insert(Song(5, "Boy with Luv", "방탄소년단(BTS)", 0, 230, false, "music_boy", R.drawable.img_album_exp4, false
            )
        )


        songDB.songDao().insert(Song(6, "BBoom BBoom", "모모랜드 (MOMOLAND)", 0, 250, false, "music_bboom", R.drawable.img_album_exp5, false
            )
        )

        val _songs = songDB.songDao().getSongs()
        Log.d("DB data", _songs.toString())
    }


    private fun inputDummyAlbums() {
        val songDB = SongDatabase.getInstance(this)!!
        val albums = songDB.songDao().getAlbums()

        // songs에 데이터가 이미 있어서 더미 데이터 삽입할 필요 X.
        if (albums.isNotEmpty()) return

        // songs에 데이터 없어서 더미 데이터 삽입 필요.
        songDB.albumDao().insert(Album(1, "Lilac", "아이유 (IU)", R.drawable.img_album_exp2
            )
        )

        songDB.albumDao().insert(Album(2, "Flu", "아이유 (IU)", R.drawable.img_album_exp2
            )
        )

        songDB.albumDao().insert(Album(3, "Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp
            )
        )

        songDB.albumDao().insert(Album(4, "Next Level", "에스파 (AESPA)", R.drawable.img_album_exp3
            )
        )


        songDB.albumDao().insert(Album(5, "Boy with Luv", "방탄소년단(BTS)", R.drawable.img_album_exp4
            )
        )


        songDB.albumDao().insert(Album(6, "BBoom BBoom", "모모랜드 (MOMOLAND)", R.drawable.img_album_exp5
            )
        )

        val _songs = songDB.songDao().getSongs()
        Log.d("DB data", _songs.toString())
    }
}

