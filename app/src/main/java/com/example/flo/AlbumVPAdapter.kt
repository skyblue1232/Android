package com.example.flo

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class AlbumVPAdapter(fragment:Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    //각각의 다른 뷰를 가지고 있기 때문에 프래그먼트를 만들어줘서 각각 연결.
    //when 이용. -> 조건에 따라 다른 작업을 할 수 있게 해주는 문법.
    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> SongFragment()
            1 -> DetailFragment()
            else -> VideoFragment()
        }
    }
}