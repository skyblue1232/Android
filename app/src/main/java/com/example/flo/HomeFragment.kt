package com.example.flo

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.databinding.FragmentHomeBinding
import com.google.gson.Gson

class HomeFragment : Fragment(), AlbumView {

    interface OnPlayClickListener {
        fun onPlayClick(albumId: Int)
    }

    private var listener: OnPlayClickListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnPlayClickListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnPlayClickListener")
        }
    }


    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var gson: Gson = Gson()

    val albums = arrayListOf<Album>()
    private lateinit var songDB: SongDatabase
    var nowPos = 0

    private fun changeAlbumFragment(album: Album) {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, AlbumFragment().apply {
                arguments = Bundle().apply {
                    val gson = Gson()
                    val albumJson = gson.toJson(album)
                    putString("album", albumJson)
                }
            })
            .commitAllowingStateLoss()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        songDB = SongDatabase.getInstance(requireContext())!!
        albums.addAll(songDB.albumDao().getAlbums())
        Log.d("album-list", albums.toString())

        //val albumRVAdapter = AlbumRVAdapter(albums)
        //binding.homeTodayMusicAlbumRv.adapter = albumRVAdapter
        //albumRVAdapter.setMyItemClickListener(object: AlbumRVAdapter.MyItemClickListener {
        //    override fun onItemClick(album: Album) {
              //  changeAlbumFragment(album)
            //}

            //override fun onRemoveAlbum(position: Int) {
            //    albumRVAdapter.removeItem(position)
            //}
       // })

        //binding.homeTodayMusicAlbumRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val bannerAdapter = BannerVPAdapter(this)
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        binding.homeBannerVp.adapter = bannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        // ImageVPAdapter 생성 및 프래그먼트 추가
        val imageAdapter = ImageVPAdapter(this)

        imageAdapter.addFragment(ImageFragment(R.drawable.img_first_album_default))
        imageAdapter.addFragment(ImageFragment(R.drawable.img_album_exp3))
        imageAdapter.addFragment(ImageFragment(R.drawable.img_album_exp5))
        imageAdapter.addFragment(ImageFragment(R.drawable.img_album_exp6))
        binding.homePannelBackgroundVp.adapter = imageAdapter
        binding.homePannelBackgroundVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        val indicator = binding.homePannelIndicatorCi
        indicator.setViewPager(binding.homePannelBackgroundVp)
        autoSlide()

        return binding.root
    }

    private val sliderHandler = Handler(Looper.getMainLooper())
    private var sliderRunnable: Runnable? = null
    private fun autoSlide() {
        sliderRunnable = Runnable {
            val viewPager = binding.homePannelBackgroundVp
            viewPager.adapter?.let { adapter ->
                viewPager.currentItem =
                    if (viewPager.currentItem < adapter.itemCount - 1) {
                        viewPager.currentItem + 1
                    } else {
                        0
                    }
            }
            sliderHandler.postDelayed(sliderRunnable!!, 3000)
        }
        sliderHandler.post(sliderRunnable!!)
    }

    private fun getAlbums() {
        Log.d("HOME/ALBUM-RESPONSE", "넘어가기")
        val albumService = AlbumService()
        albumService.setAlbumView(this)

        Log.d("HOME/ALBUM-RESPONSE", "WHERE")
        albumService.getAlbums()

    }

    override fun onGetAlbumLoading() {
        Log.d("HOME/ALBUM-RESPONSE", "HERE")
    }

    private fun initAlbumFragment(album: Album){
        with(binding){
            val albumFragment = AlbumFragment().apply {
                arguments = Bundle().apply {
                    val gson = Gson()
                    val albumJson = gson.toJson(album)
                    putString("album", albumJson)
                }
            }
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.main_frm, albumFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onGetAlbumSuccess(code: Int, result: AlbumResult) {
        Log.d("HOME/ALBUM-RESPONSE", "성공 $result")
        val albumAdapter = AlbumRVAdapter(requireContext())

        binding.homeTodayMusicAlbumRv.adapter = albumAdapter
        val manager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.homeTodayMusicAlbumRv.layoutManager = manager

        albumAdapter.setMyItemClickListener(object: AlbumRVAdapter.MyItemClickListener{

            override fun onPlayClick(album: Album) {
                Log.d("id찾기", "${album.id}")
                Log.d("id찾기", "${album.title}")


                listener?.onPlayClick(album.id) // MainActivity로 AlbumId 전달
            }

            override fun onItemClick(album: Album) {
                initAlbumFragment(album)
            }

        })
    }
    override fun onGetAlbumFailure(code: Int, message: String) {
        Log.d("HOME/ALBUM-RESPONSE", "$code $message")
    }
}

