package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.homeMainAlbumImgIv.setOnClickListener{
            initAlbumFragment(binding.homeMainAlbumTitleTv.text.toString(),
                binding.homeMainAlbumSingerTv.text.toString())
        }
        binding.homeMainAlbumImg2Iv.setOnClickListener{
            initAlbumFragment(binding.homeMainAlbumTitle2Tv.text.toString(),
                binding.homeMainAlbumSinger2Tv.text.toString())
        }

        return binding.root
    }

    private fun initAlbumFragment(titleTV : String, singerTV : String){
        with(binding){
            val albumFragment = AlbumFragment().apply{
                arguments= Bundle().apply{
                    putString("albumTitle", titleTV)
                    putString("albumSinger", singerTV)
                }
            }
            val transaction =parentFragmentManager.beginTransaction()
            transaction.replace(R.id.main_frm, albumFragment)
                .addToBackStack(null)
                .commit()
        }
    }
}