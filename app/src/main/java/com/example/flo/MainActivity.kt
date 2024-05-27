package com.example.flo

import HomeFragment
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.flo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), HomeFragment.OnPlayClickListener {

    lateinit var binding: ActivityMainBinding
    // private var gson: Gson = Gson()
    private var mediaPlayer: MediaPlayer? = null

    val songs = arrayListOf<Song>()
    lateinit var songDB: SongDatabase
    var nowPos = 0



    private val getResultText = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){result->
        val songProgress = result.data?.getIntExtra("song_progress", 0) ?: 0
        songs[nowPos].second = songProgress
        setMiniPlayer(songs[nowPos])
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //val text= findViewById<TextView>(R.id.main_player_cl)
        // 가독성이 떨어져서 --->> ViewBinding을 씀.
        //textView.text = 'dd'

        initBottomNavigation()
        initPlayList()
        inputDummySongs()

        Log.d("Song", "제목 : ${songs[nowPos].title} / 가수 : ${songs[nowPos].singer}")


        binding.mainPlayerCl.setOnClickListener {
            //startActivity(Intent(this, SongActivity::class.java))
            val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
            editor.putInt("songId", songs[nowPos].id)
            editor.apply()

            val intent = Intent(this, SongActivity::class.java)
            startActivity(intent)
//            intent.putExtra("title", song.title)
//            intent.putExtra("singer", song.singer)
//            intent.putExtra("song", song.second)
//            intent.putExtra("playTime", song.playTime)
//            intent.putExtra("isPlaying", song.isPlaying)
//            intent.putExtra("music", song.music)
//            startActivity(intent)
            // 하나의 액티비티에서 사용하는 택배상자 -> Intent
        }


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

//        binding.mainProgressSb.max = song.playTime
//        binding.mainProgressSb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
//            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
//                // 진행 상태 변경 시 처리할 로직
//                // 예: 노래의 현재 재생 시간을 업데이트
//                song.second = progress
//                setMiniPlayer(song)
//            }
//
//            override fun onStartTrackingTouch(seekBar: SeekBar) {
//                // 사용자가 SeekBar 터치를 시작할 때 처리할 로직
//            }
//
//            override fun onStopTrackingTouch(seekBar: SeekBar) {
//                // 사용자가 SeekBar 터치를 중지할 때 처리할 로직
//                // 예: 노래의 재생 위치를 변경
//                song.second = seekBar.progress
//                setMiniPlayer(song)
//            }
//        })

    }

    private fun initClickListener(){
        binding.mainPlayerCl.setOnClickListener{
            val editor = getSharedPreferences("song",MODE_PRIVATE).edit()
            editor.putInt("songId", songs[nowPos].id)
            editor.apply()

            val intent = Intent(this, SongActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onStart() {
        super.onStart()
//        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
//        val songJson = sharedPreferences.getString("songData", null)
//
//        song = if(songJson == null) {
//            Song("라일락", "아이유(IU)", 0, 60, false, "music_lilac")
//        } else {
//            gson.fromJson(songJson, Song::class.java)
//        }
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)

        val songDB = SongDatabase.getInstance(this)!!

        songs[nowPos] = if(songId ==0) {
            songDB.songDao().getSong(1)
        } else{
            songDB.songDao().getSong(songId)
        }

        Log.d("song ID", songs[nowPos].id.toString())
        setMiniPlayer(songs[nowPos])
    }

    // SongActivity에서 뒤로 가기 버튼 누르면 콜백 함수 호출되게 함.
    override fun onResume() {
        super.onResume()

        val spf= getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)

        nowPos = getPlayingSongPosition(songId)
        setMiniPlayer(songs[nowPos])
    }

    private fun getPlayingSongPosition(songId: Int): Int{
        for (i in 0 until songs.size){
            if (songs[i].id == songId){
                return i
            }
        }
        return 0
    }

    private fun initPlayList(){
        songDB = SongDatabase.getInstance(this@MainActivity)!!
        songs.addAll(songDB.songDao().getSongs())
    }

    private fun setMiniPlayer(song : Song) {
        binding.mainMiniplayerTitleTv.text = song.title
        binding.mainMiniplayerSingerTv.text = song.singer
        Log.d("songInfo", song.toString())
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val second = sharedPreferences.getInt("second", 0)
        Log.d("spfSecond", second.toString())
        binding.mainProgressSb.progress = (second * 100000 / song.playTime)
    }

    private fun moveSong(direct: Int){
        if(nowPos + direct < 0 ){
            Toast.makeText(this@MainActivity, "first song", Toast.LENGTH_SHORT).show()
            return
        }
        if(nowPos + direct >= songs.size){
            Toast.makeText(this@MainActivity, "last song", Toast.LENGTH_SHORT).show()
            return
        }

        nowPos += direct

        mediaPlayer?.release() // 미디어 플레이어가 갖고 있던 리소스 해제
        mediaPlayer = null // 미디어 플레이어 해제

        setMiniPlayer(songs[nowPos])
    }


    override fun onPlayClick(songData: Int) {
        songs[nowPos].isPlaying = false
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null

        songs.clear()   // 재생하려고 넣어놨던 데이터들을 제거
        Log.d("id찾기", "${songs[nowPos].id}")

        // 앨범 아이디가 같은 모든 song을 불러와서 songs에 넣는다
        songs.addAll(songDB.songDao().getSongsByalbumIdx(songs[nowPos].id))
        // 처음 곡부터 재생할 것이므로 nowPos 초기화
        nowPos = 0
        if(songs.isNotEmpty()) {
            songs[nowPos].isPlaying = true
            setMiniPlayer(songs[nowPos])
        } else {
            Log.d("id찾기", "빔")
        }
    }

//    override fun onSelectClick(isSelectOn: Boolean) {
//        // 삭제 등의 작업을 진행하는 창이 올라옴
//        if(isSelectOn){
//            //Toast.makeText(this@MainActivity, "true넘어옴", Toast.LENGTH_SHORT).show()
//            chooseBottom(isSelectOn)
//
//            binding.sheetBnv.setOnItemSelectedListener{item->
//                when(item.itemId) {
//                    R.id.btn_delete-> {
//                        chooseBottom(isSelectOn)
//                        songDB.songDao().updateIsLikeAllFalse()
//
//                        val fragment = SavedSongFragment()
//                        fragment.deleteAll()
//
//                        chooseBottom(false)
//
//                        return@setOnItemSelectedListener true
//                    }
//
//                    else -> {
//                        return@setOnItemSelectedListener true
//                    }
//                }
//            }
//
//        }else{
//            //Toast.makeText(this@MainActivity, "false넘어옴", Toast.LENGTH_SHORT).show()
//            chooseBottom(isSelectOn)
//        }
//    }

    private fun inputDummySongs(){
        val songDB = SongDatabase.getInstance(this)!!
        val songs = songDB.songDao().getSongs()

        // songs에 데이터가 이미 있어서 더미 데이터 삽입할 필요 X.
        if (songs.isNotEmpty()) return

        // songs에 데이터 없어서 더미 데이터 삽입 필요.
        songDB.songDao().insert(
            Song(
                "Lilac",
                "아이유 (IU)",
                0,
                200,
                false,
                "music_lilac",
                false,
                R.drawable.img_album_exp2,
                1
            )
        )

        songDB.songDao().insert(
            Song(
                "Flu",
                "아이유 (IU)",
                0,
                200,
                false,
                "music_flu",
                false,
                R.drawable.img_album_exp2,
                1
            )
        )

        songDB.songDao().insert(
            Song(
                "Butter",
                "방탄소년단 (BTS)",
                0,
                190,
                false,
                "music_butter",
                false,
                R.drawable.img_album_exp,
                2
            )
        )

        songDB.songDao().insert(
            Song(
                "Next Level",
                "에스파 (AESPA)",
                0,
                210,
                false,
                "music_next",
                false,
                R.drawable.img_album_exp3,
                3
            )
        )


        songDB.songDao().insert(
            Song(
                "Boy with Luv",
                "music_boy",
                0,
                230,
                false,
                "music_lilac",
                false,
                R.drawable.img_album_exp4,
                4
            )
        )


        songDB.songDao().insert(
            Song(
                "BBoom BBoom",
                "모모랜드 (MOMOLAND)",
                0,
                240,
                false,
                "music_bboom",
                false,
                R.drawable.img_album_exp5,
                5
            )
        )

        val songDBData = songDB.songDao().getSongs()
        Log.d("DB data", songDBData.toString())
    }
}


