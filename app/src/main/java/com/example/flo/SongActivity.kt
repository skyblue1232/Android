package com.example.flo

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {

    lateinit var binding : ActivitySongBinding
//    lateinit var song: Song
    lateinit var timer:Timer
    private var mediaPlayer: MediaPlayer? = null
//    private var gson: Gson = Gson()

    val songs = arrayListOf<Song>()
    lateinit var songDB: SongDatabase
    var nowPos = 0

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

        binding.musicMiniplayerIv.setOnClickListener {
            setPlayerStatus(true)
//            startStopService()
        }

        binding.musicMvpauseIv.setOnClickListener {
            setPlayerStatus(false)
//            startStopService()
        }

        initPlayPauseBtn()
        initRepeatBtn()
        initRandomBtn()
        initPlayList()
        initSong()
        initClickListener()
        setPlayer(songs[nowPos])

    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("message", "뒤로가기")

        setResult(RESULT_OK, intent)
        finish()
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

    private fun setPlayer(song:Song){
        // binding.songMusicTitleTv.text=intent.getStringExtra("title")!! 인텐트에서 변경
        binding.songMusicTitleTv.text=song.title
        binding.musicSingerNameTv.text=song.singer
        binding.musicStartTimeTv.text = String.format("%02d:%02d", song.second / 60, song.second % 60)
        binding.musicEndTimeTv.text = String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)
        // coverImg도 렌더링 해줌.
        binding.musicMiniplayerIv.setImageResource(songs[nowPos].coverImg!!)
        binding.musicPlayIngSb.progress = (song.second * 1000 / song.playTime)

        val music = resources.getIdentifier(song.music,"raw",this.packageName)
        mediaPlayer = MediaPlayer.create(this,music)

        if(song.isLike) {
            binding.musicLoveIv.setImageResource(R.drawable.ic_my_like_on)
        }
        else {
            binding.musicLoveIv.setImageResource(R.drawable.ic_my_like_off)
        }

        setPlayerStatus(song.isPlaying)
    }

    private fun setPlayerStatus(isPlaying : Boolean) {
        songs[nowPos].isPlaying = isPlaying
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

    private fun initPlayList(){
        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getSongs())
    }

    private fun initClickListener(){
        binding.songDownIb.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("message", songs[nowPos].title + "LILAC" + songs[nowPos].singer)
            setResult(RESULT_OK, intent)
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

        binding.musicLoveIv.setOnClickListener{
            setLike(songs[nowPos].isLike)
        }
    }

    private fun initSong(){
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)

        nowPos = getPlayingSongPosition(songId)
//        if(intent.hasExtra("title") && intent.hasExtra("singer")){
//            song = Song(
//                intent.getStringExtra("title")!!,
//                intent.getStringExtra("singer")!!,
//                intent.getIntExtra("second",0),
//                intent.getIntExtra("playTime", 0),
//                intent.getBooleanExtra("isPlaying",false),
//                intent.getStringExtra("music")!!
//            )
//        }
        Log.d("now Song ID", songs[nowPos].id.toString())
        startTimer()
        setPlayer(songs[nowPos])
    }

    private fun setLike(isLike: Boolean){
        songs[nowPos].isLike = !isLike
        songDB.songDao().updateIsLikeById(!isLike, songs[nowPos].id)

        //좋아요
        if (!isLike) {
            binding.musicLoveIv.setImageResource(R.drawable.ic_my_like_on)
        } else {
            binding.musicLoveIv.setImageResource(R.drawable.ic_my_like_off)
        }
    }

    private fun moveSong(direct: Int){
        if (nowPos + direct < 0){
            //Toast.makeText(this,"first song",Toast.LENGTH_SHORT).show()
            CustomSnackbar.make(binding.root, "첫 곡").show()
            return
        }

        else if (nowPos + direct >= songs.size){
            //Toast.makeText(this,"last song",Toast.LENGTH_SHORT).show()
            CustomSnackbar.make(binding.root, "마지막 곡").show()
            return
        }

        nowPos += direct

        timer.interrupt()
        startTimer()

        mediaPlayer?.release()
        mediaPlayer = null

        setPlayer(songs[nowPos])
    }
    private fun getPlayingSongPosition(songId: Int): Int{
        for (i in 0 until songs.size){
            if (songs[i].id == songId){
                return i
            }
        }
        return 0
    }

    private fun startTimer(){
        timer = Timer(songs[nowPos].playTime,songs[nowPos].isPlaying)
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

                    while(!isPlaying) {
                        sleep(200) // 0.2초 대기
                    }

                    if (isPlaying) {
                        sleep(50)
                        mills += 50

                        runOnUiThread {
                            binding.musicPlayIngSb.progress = ((mills/playTime) * 100).toInt()
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
                Log.d("SongActivity", "Thread가 죽었습니다. ${e.message}")
            }
        }
    }

    // 사용자가 포커스를 잃었을 때 음악 중지.
    override fun onPause() {
        super.onPause()
        songs[nowPos].second = (songs[nowPos].playTime * binding.musicPlayIngSb.progress) / 100000
        Log.d("second", songs[nowPos].second.toString())
        songs[nowPos].isPlaying = false
        setPlayerStatus(false)

        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit() //에디터
//        val songJson = gson.toJson(song)
//        editor.putString("songData", songJson)
        editor.putInt("songId", songs[nowPos].id)
        editor.putInt("second", songs[nowPos].second)
        editor.apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
        mediaPlayer?.release() //미디어플레이어가 갖고 있던 리소스를 해제
        mediaPlayer = null //미디어 플레이어 해제
    }

//    private fun startStopService() {
//        if (isServiceRunning(ForegroundService::class.java)) {
//            Toast.makeText(this, "Foreground Service Stopped", Toast.LENGTH_SHORT).show()
//            stopService(Intent(this, ForegroundService::class.java))
//        }
//        else {
//            Toast.makeText(this, "Foreground Service Started", Toast.LENGTH_SHORT).show()
//            startService(Intent(this, ForegroundService::class.java))
//        }
//    }

//    private fun isServiceRunning(inputClass : Class<ForegroundService>) : Boolean {
//        val manager : ActivityManager = getSystemService(
//            Context.ACTIVITY_SERVICE
//        ) as ActivityManager

//        for (service : ActivityManager.RunningServiceInfo in manager.getRunningServices(Integer.MAX_VALUE)) {
//            if (inputClass.name.equals(service.service.className)) {
//                return true
//            }

//        }
//        return false
//    }

}
