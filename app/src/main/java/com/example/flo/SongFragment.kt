package com.example.flo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentSongBinding

class SongFragment : Fragment(), AlbumSongView {

    private var _binding: FragmentSongBinding? = null
    private val binding get() = _binding!!

    private var albumIDX: Int = 0
    private lateinit var adapter: AlbumSongRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSongBinding.inflate(inflater,container, false)

        // initMixBtn()

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        getAlbumSong()
    }

    private fun initRecyclerView(result: AlbumSongRes) {
        adapter = AlbumSongRVAdapter(requireContext(), result.result as ArrayList<AlbumSongResult>)

        binding.songMusicListRv.adapter = adapter
    }

    private fun getAlbumSong() {
        val spf = activity?.getSharedPreferences("album", AppCompatActivity.MODE_PRIVATE)
        albumIDX = spf!!.getString("albumIdx", "0")!!.toInt()

        Log.d("SongFragment-RESPONSE", "넘어는 가냐")
        val albumService = AlbumService()
        albumService.setAlbumSongView(this)

        Log.d("SongFragment-RESPONSE", "어디까지 가는거지")
        albumService.getAlbumSong(albumIDX)

    }



    override fun onGetAlbumSongLoading() {

    }

    override fun onGetAlbumSongSuccess(code: Int, result: AlbumSongRes) {
        Log.d("SongFragment-RESPONSE", "$code $result")
        initRecyclerView(result)
    }

    override fun onGetAlbumSongFailure(code: Int, message: String) {
        Log.d("SongFragment-RESPONSE", "$code $message")
    }
}