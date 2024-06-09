package com.example.flo

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivityMainBinding
import com.google.gson.Gson

class MainActivity : AppCompatActivity(), LoginView {

    lateinit var binding: ActivityMainBinding

    private var song: Song = Song()
    private var gson: Gson = Gson()


    private var mediaPlayer: MediaPlayer? = null

    val songs = arrayListOf<Song>()
    lateinit var songDB: SongDatabase
    var nowPos = 0

    private val getResultText = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){result ->
        if(result.resultCode == Activity.RESULT_OK){
            val returnString = result.data?.getStringExtra("songsong")
            Toast.makeText(applicationContext, returnString, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_FLO)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initPlayList()

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

        Log.d("MAUN/JWT_TO_SERVER", getJwt().toString())

        initClickListener()
        initMusicPlay()
    }

    private fun initPlayList(){
        songDB = SongDatabase.getInstance(this@MainActivity)!!
        songs.addAll(songDB.songDao().getSongs())
    }

    private fun initClickListener(){
        binding.mainPlayerCl.setOnClickListener {
            val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
            editor.putInt("songId", songs[nowPos].id)
            editor.apply()

            val intent = Intent(this, SongActivity::class.java)
            startActivity(intent)
        }

        binding.mainMiniplayerBtn.setOnClickListener {
            val intent = Intent(this, TestActivity::class.java)
            startActivity(intent)
        }

        binding.mainNextSongBtn.setOnClickListener {
            moveSong(1)
        }
        binding.mainBackSongBtn.setOnClickListener {
            moveSong(-1)
        }
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
        songs[nowPos].isPlaying = false

        nowPos += direct

        mediaPlayer?.release() // 미디어 플레이어가 갖고 있던 리소스 해제
        mediaPlayer = null // 미디어 플레이어 해제

        setMiniPlayer(songs[nowPos])
    }

    private fun getPlayingSongPosition(songId: Int): Int{
        for(i in 0 until songs.size){
            if(songs[i].id == songId){
                return i
            }
        }
        return 0
    }

    private fun getJwt() : String? {
        val spf = this.getSharedPreferences("auth2", MODE_PRIVATE)

        return spf!!.getString("jwt", "")
    }

    override fun onStart() {
        super.onStart()

        songs.clear()
        initPlayList()
        // 액티비티 전환이 될때 onStart()부터 해주기 때문에 여기서 Song 데이터를 가져옴

        // spf에 sharedpreference에 저장되어있던 값을 가져옴
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        // spf Array의 가장 처음 song의 id를 가져옴
        val songId = spf.getInt("songId", 0)

        nowPos = getPlayingSongPosition(songId)
        Log.d("songIDID", nowPos.toString())
        Log.d("songIDID", songs[nowPos].id.toString())
        setMiniPlayer(songs[nowPos])
    }

    // 사용자가 포커스를 잃었을 때 음악 중지
    override fun onPause() {
        super.onPause()
        setPlayerStatus(false)
        songs[nowPos].second = ((binding.mainProgressSb.progress * songs[nowPos].playTime)/100)/1000
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release() // 미디어 플레이어가 갖고 있던 리소스 해제
        mediaPlayer = null // 미디어 플레이어 해제
    }

    override fun onPlayClick(albumId: Int) {
        songs[nowPos].isPlaying = false
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null


        songs.clear()   // 재생하려고 넣어놨던 데이터들을 제거
        Log.d("id찾기", "$albumId")

        // 앨범 아이디가 같은 모든 song을 불러와서 songs에 넣는다
        songs.addAll(songDB.songDao().getSongsByalbumIdx(albumId))
        // 처음 곡부터 재생할 것이므로 nowPos 초기화
        nowPos = 0
        if(songs.isNotEmpty()) {
            songs[nowPos].isPlaying = true
            setMiniPlayer(songs[nowPos])
        } else {
            Log.d("id찾기", "빔")
        }
    }

    private fun initMusicPlay(){
        with(binding){
            // 음악이 정지해있을때 재생
            mainMiniplayerBtn.setOnClickListener {
                setPlayerStatus(true)
            }
            // 음악이 재생중일때 정지
            mainPauseBtn.setOnClickListener {
                setPlayerStatus(false)
            }
        }
    }

    private fun setPlayerStatus(isPlaying: Boolean){
        with(binding){
            songs[nowPos].isPlaying =  isPlaying

            if(isPlaying){
                binding.mainMiniplayerBtn.visibility = View.GONE
                binding.mainPauseBtn.visibility = View.VISIBLE
                mediaPlayer?.start()
            } else {
                binding.mainMiniplayerBtn.visibility = View.VISIBLE
                binding.mainPauseBtn.visibility = View.GONE
                if(mediaPlayer?.isPlaying == true){
                    mediaPlayer?.pause()
                }else{

                }
            }

        }
    }

    private fun setMiniPlayer(song: Song){
        binding.mainPlayingSongTitleTv.text = song.title
        binding.mainPlayingSingerTv.text = song.singer
        binding.mainProgressSb.progress = (song.second*100000)/song.playTime
        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this@MainActivity, music)
        setPlayerStatus(song.isPlaying)

    }

    override fun onSelectClick(isSelectOn: Boolean) {
        // 삭제 등의 작업을 진행하는 창이 올라옴
        if(isSelectOn){
            //Toast.makeText(this@MainActivity, "true넘어옴", Toast.LENGTH_SHORT).show()
            chooseBottom(isSelectOn)

            binding.sheetBnv.setOnItemSelectedListener { item ->
                when(item.itemId) {
                    R.id.btn_delete -> {
                        chooseBottom(isSelectOn)
                        songDB.songDao().updateIsLikeAllFalse()

                        // val fragment = SavedSongFragment()
                        // fragment.deleteAll()

                        chooseBottom(false)

                        return@setOnItemSelectedListener true
                    }

                    else -> {
                        return@setOnItemSelectedListener true
                    }
                }
            }

        }else{
            //Toast.makeText(this@MainActivity, "false넘어옴", Toast.LENGTH_SHORT).show()
            chooseBottom(isSelectOn)
        }
    }

    private fun chooseBottom(isSelect: Boolean){
        if(isSelect){
            binding.mainBnv.visibility = View.GONE
            binding.sheetBnv.visibility = View.VISIBLE
        }else{
            binding.mainBnv.visibility = View.VISIBLE
            binding.sheetBnv.visibility = View.GONE
        }

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

    override fun onLoginSuccess(code: Int, result: Result) {
        TODO("Not yet implemented")
    }

    override fun onLoginFailure(message: String) {
        TODO("Not yet implemented")
    }

    companion object {
        private fun setMiniPlayer(mainActivity: MainActivity, song: Song) {
            mainActivity.binding.mainPlayingSongTitleTv.text = song.title
            mainActivity.binding.mainPlayingSingerTv.text = song.singer
            mainActivity.binding.mainProgressSb.progress = (song.second * 100000) / song.playTime
        }
    }
}

