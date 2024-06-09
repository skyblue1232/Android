package com.example.flo

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.flo.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson


class AlbumFragment : Fragment() {

    lateinit var binding: FragmentAlbumBinding
    private var gson: Gson = Gson()

    private val information = arrayListOf("수록곡", "상세정보", "영상")

    private var isLiked: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)

        // Home에서 넘어온 데이터 받아오기
        val albumJson = arguments?.getString("album")
        val gson = Gson()
        val album = gson.fromJson(albumJson, FloChartAlbums::class.java)
        // Home에서 넘어온 데이터를 반영
        //isLiked = isLikedAlbum(album.id)
        //setInit(album)
        //setOnClickListeners(album)

        val sharedPreferences = activity?.getSharedPreferences("album", AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.putString("albumIdx", "${album.albumIdx}")
        editor?.apply()

        // Home에서 넘어온 데이터 반영
        isLiked = isLikedAlbum(album.albumIdx)
        setInit(album)
        setOnClickListeners(album)

        initViewPager()
        initAlbumback()

        binding.albumBackIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, HomeFragment())
                .commitAllowingStateLoss()
        }

        val albumAdapter = AlbumVPAdapter(this)
        binding.albumContentVp.adapter = albumAdapter
        TabLayoutMediator(binding.albumContentTb, binding.albumContentVp) { tab, position ->
            tab.text = information[position]
        }.attach()

        return binding.root
    }

    private fun initAlbumback(){
        binding.albumBackIv.setOnClickListener {
            val homeFragment = HomeFragment()
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.main_frm, homeFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun initViewPager(){
        with(binding){
            val adapter = AlbumVPAdapter(this@AlbumFragment)
            albumContentVp.adapter = adapter
            // TabLayout과 ViewPager2를 연결하는 중재자
            // Tab이 선택될 때 ViewPager2의 위치와 선택된 탭을 동기화하고
            // 사용자가 ViewPager2를 스크롤 할 때 TabLayout의 스크롤 위치를 동기화한다.
            TabLayoutMediator(albumContentTb, albumContentVp){
                    tab, position ->
                tab.text = information[position]
            }.attach()
        }
    }

    private fun setInit(album: FloChartAlbums) {
        // binding.albumAlbumIv.setImageResource(album.coverImg!!)
        binding.albumMusicTitleTv.text = album.title.toString()
        binding.albumSingerNameTv.text = album.singer.toString()
        if(album.coverImgUrl == "" || album.coverImgUrl == null){

        } else {
            Log.d("image",album.coverImgUrl )
            Glide.with(requireContext()).load(album.coverImgUrl).into(binding.albumAlbumIv)
        }

        if (isLiked) {
            binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_on)
        } else {
            binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }
    }

    private fun initToast(likeText: String) {
        val inflater = LayoutInflater.from(requireContext())
        val layout = inflater.inflate(R.layout.custom_toast, null)
        val text = layout.findViewById<TextView>(R.id.likeTv)
        text.text = likeText

        val toast = Toast(requireContext())
        toast.duration = Toast.LENGTH_LONG
        toast.view = layout
        toast.setGravity(Gravity.FILL_HORIZONTAL or Gravity.BOTTOM, 0, 100)
        toast.show()
    }

    private fun getJwt(): Int {
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getInt("jwt", 0)
    }

    private fun likeAlbum(userId: Int, albumId: Int) {
        val songDB = SongDatabase.getInstance(requireContext())!!
        val like = Like(userId, albumId)

        songDB.albumDao().likeAlbum(like)
    }

    private fun isLikedAlbum(albumId: Int): Boolean {
        val songDB = SongDatabase.getInstance(requireContext())!!
        val userId = getJwt()

        val likeId: Int? = songDB.albumDao().isLikedAlbum(userId, albumId)

        return likeId != null
    }

    private fun disLikedAlbum(albumId: Int) {
        val songDB = SongDatabase.getInstance(requireContext())!!
        val userId = getJwt()

        songDB.albumDao().disLikeAlbum(userId, albumId)
    }

    private fun setOnClickListeners(album: FloChartAlbums) {
        val userId = getJwt()
        binding.albumLikeIv.setOnClickListener {
            if (isLiked) {
                binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_off)
                disLikedAlbum(album.albumIdx)
            } else {
                binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_on)
                likeAlbum(userId, album.albumIdx)
            }
        }
    }
}
