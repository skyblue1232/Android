package com.example.flo

import HomeFragment
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.flo.databinding.ActivityMainBinding
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private var song: Song = Song()
    private var gson: Gson = Gson()

    private val getResultText = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){result->
        if(result.resultCode== Activity.RESULT_OK){
            val returnString = result.data?.getStringExtra("flomusic")
            Toast.makeText(applicationContext, returnString, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //val text= findViewById<TextView>(R.id.main_player_cl)
        // 가독성이 떨어져서 --->> ViewBinding을 씀.
        //textView.text = 'dd'

        Log.d("Song", "제목 : ${song.title} / 가수 : ${song.singer}")

        binding.mainPlayerCl.setOnClickListener {
            //startActivity(Intent(this, SongActivity::class.java))

            val intent = Intent(this, SongActivity::class.java)
            intent.putExtra("title", song.title)
            intent.putExtra("singer", song.singer)
            intent.putExtra("song", song.second)
            intent.putExtra("playTime", song.playTime)
            intent.putExtra("isPlaying", song.isPlaying)
            intent.putExtra("music", song.music)
            startActivity(intent)

            getResultText.launch(intent)
            // 하나의 액티비티에서 사용하는 택배상자 -> Intent
        }
        initBottomNavigation()

        Log.d("Song", song.title + song.singer)
    }

    private fun initBottomNavigation(){


        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()

        binding.mainBnv.setOnItemSelectedListener{ item ->
            when (item.itemId) {

                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .commitAllowingStateLoss()
                    Toast.makeText(applicationContext, "Home", Toast.LENGTH_SHORT).show()
                    Log.d("bottommmm","Home")
                    return@setOnItemSelectedListener true
                }

                R.id.lookFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LookFragment())
                        .commitAllowingStateLoss()
                    Toast.makeText(applicationContext, "Look", Toast.LENGTH_SHORT).show()
                    Log.d("bottommmm","look")
                    return@setOnItemSelectedListener true
                }
                R.id.searchFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, SearchFragment())
                        .commitAllowingStateLoss()
                    Toast.makeText(applicationContext, "Search", Toast.LENGTH_SHORT).show()
                    Log.d("bottommmm","search")
                    return@setOnItemSelectedListener true
                }
                R.id.lockerFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LockerFragment())
                        .commitAllowingStateLoss()
                    Toast.makeText(applicationContext, "Locker", Toast.LENGTH_SHORT).show()
                    Log.d("bottommmm","locker")
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }

    private fun setMiniPlayer(song: Song) {
        binding.mainMiniplayerTitleTv.text = song.title
        binding.mainMiniplayerSingerTv.text = song.singer
        binding.mainProgressSb.progress = (song.second*100000)/song.playTime
    }

    override fun onStart() {
        super.onStart()
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val songJson = sharedPreferences.getString("songData", null)

        song = if(songJson == null) {
            Song("라일락", "아이유(IU)", 0, 60, false, "music_lilac")
        } else {
            gson.fromJson(songJson, Song::class.java)
        }

        setMiniPlayer(song)
    }
}