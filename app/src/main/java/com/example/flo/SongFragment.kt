package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentDetailBinding
import com.example.flo.databinding.FragmentSongBinding

class SongFragment : Fragment() {

    lateinit var binding: FragmentSongBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSongBinding.inflate(inflater,container, false)

        initMixBtn()

        return binding.root
    }

    private fun initMixBtn(){
        with(binding){
            songMixoffTg.setOnClickListener{
                songMixoffTg.visibility= View.GONE
                songMixonTg.visibility= View.VISIBLE
            }
            songMixonTg.setOnClickListener{
                songMixonTg.visibility= View.GONE
                songMixoffTg.visibility= View.VISIBLE
            }
        }
    }
}