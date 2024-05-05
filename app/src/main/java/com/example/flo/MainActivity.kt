package com.example.flo

import HomeFragment
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.flo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

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
        val splashScreen = installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //val text= findViewById<TextView>(R.id.main_player_cl)
        // 가독성이 떨어져서 --->> ViewBinding을 씀.
        //textView.text = 'dd'

        val song = Song(binding.mainMiniplayerTitleTv.text.toString(), binding.mainMiniplayerSingerTv.text.toString())

        Log.d("Song", "제목 : ${song.title} / 가수 : ${song.singer}")

        binding.mainPlayerCl.setOnClickListener {
            //startActivity(Intent(this, SongActivity::class.java))

            val intent = Intent(this, SongActivity::class.java)
            intent.putExtra("title", song.title)
            intent.putExtra("singer", song.singer)
            intent.putExtra("title", song.second)
            intent.putExtra("singer", song.playTime)
            intent.putExtra("title", song.isPlaying)
            intent.putExtra("singer", song.singer)
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
}