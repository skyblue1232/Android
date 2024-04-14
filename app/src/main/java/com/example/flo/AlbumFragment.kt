package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentAlbumBinding

class AlbumFragment :Fragment(){
    lateinit var binding : FragmentAlbumBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater,container,false)

        val title =arguments?.getString("albumTitle")
        val singer =arguments?.getString("albumSinger")

        binding.albumMusicTitleTv.text= title
        binding.albumSingerNameTv.text= singer

        binding.albumBackIv.setOnClickListener{
            val homeFragment = HomeFragment()
            val transaction =parentFragmentManager.beginTransaction()
            transaction.replace(R.id.main_frm, homeFragment)
                .addToBackStack(null)
                .commit()
        }

        return binding.root
    }
}