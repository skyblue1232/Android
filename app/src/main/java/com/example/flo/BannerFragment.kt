package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentBannerBinding

class BannerFragment(val imgRes : Int) : Fragment() { // 리스트에 새로운 것들이 추가될 때마다 새로운 이미지를 넣을 수 있도록 함.


    lateinit var binding : FragmentBannerBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBannerBinding.inflate(inflater, container, false)


        //이미지로 바꾸는 방법 - 인자값으로 받은 이미지뷰로 src값이 변경됨.
        binding.bannerImageIv.setImageResource(imgRes)


        return binding.root
    }


}