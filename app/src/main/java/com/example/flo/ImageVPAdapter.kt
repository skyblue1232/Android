package com.example.flo

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ImageVPAdapter (fragment: Fragment) : FragmentStateAdapter(fragment) {

    val _fragmentlist : ArrayList<Fragment> = ArrayList()

    override fun getItemCount(): Int = _fragmentlist.size

    override fun createFragment(position: Int): Fragment = _fragmentlist[position]//0,1,2,3

    fun addFragment(fragment: Fragment) {
        _fragmentlist.add(fragment)
        notifyItemInserted(_fragmentlist.size-1) // 새로운 값이 리스트에 추가되게.
    }
}