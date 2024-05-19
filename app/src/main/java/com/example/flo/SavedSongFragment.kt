package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.databinding.FragmentSavedSongBinding

class SavedSongFragment : Fragment() {

    private var _binding: FragmentSavedSongBinding? = null
    private val binding get() = _binding!!
    private val MusicItemAdapter = SavedSongRVAdapter(Dummy())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSavedSongBinding.inflate(inflater, container, false)

        binding.lockerSavedSongRecyclerView.adapter= MusicItemAdapter
        val manager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.lockerSavedSongRecyclerView.layoutManager= manager

        MusicItemAdapter.setSavedSongClickListener(object: SavedSongRVAdapter.SavedSongClickListener{
            override fun onRemoveSavedSong(position: Int) {
                MusicItemAdapter.removeSong(position)
            }
        })

        return binding.root
    }

    private fun Dummy() : ArrayList<Album>{
        val dummy1 = Album("Lilac", "아이유 (IU)", R.drawable.img_album_exp2)
        val dummy2 = Album("Next Level", "에스파 (AESPA)", R.drawable.img_album_exp3)
        val dummy3 = Album("Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp)
        val dummy4 = Album("BBoom BBoom", "모모랜드 (MOMOLAND)", R.drawable.img_album_exp5)
        val dummy5 = Album("Boy with Luv", "방탄소년단 (BTS)", R.drawable.img_album_exp4)
        val dummy6 = Album("Weekend", "태연 (Tae Yeon)", R.drawable.img_album_exp6)
        val dummy7 = Album("Lilac", "아이유 (IU)", R.drawable.img_album_exp2)
        val dummy8 = Album("Next Level", "에스파 (AESPA)", R.drawable.img_album_exp3)
        val dummy9 = Album("Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp)
        val dummy10 = Album("BBoom BBoom", "모모랜드 (MOMOLAND)", R.drawable.img_album_exp5)
        val dummy11 = Album("Boy with Luv", "방탄소년단 (BTS)", R.drawable.img_album_exp4)
        val dummy12 = Album("Weekend", "태연 (Tae Yeon)", R.drawable.img_album_exp6)

        val arr = ArrayList<Album>()
        arr.add(dummy1)
        arr.add(dummy2)
        arr.add(dummy3)
        arr.add(dummy4)
        arr.add(dummy5)
        arr.add(dummy6)
        arr.add(dummy7)
        arr.add(dummy8)
        arr.add(dummy9)
        arr.add(dummy10)
        arr.add(dummy11)
        arr.add(dummy12)

        return arr
    }

}