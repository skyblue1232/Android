package com.example.flo

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class BannerVPAdapter (fragment: Fragment) :FragmentStateAdapter(fragment){ //인자를 써줘야 함.

    // 외부에서 도용되는 걸 방지하기 위해서 private 써줌.
    val fragmentlist : ArrayList<Fragment> = ArrayList() //프래그먼트 담을 리스트

    //데이터를 몇 개 전달할 것이냐를 써주는 함수, 함수를 바로 초기화 가능.
    override fun getItemCount(): Int = fragmentlist.size

    override fun createFragment(position: Int): Fragment = fragmentlist[position]//0,1,2,3

    fun addFragment(fragment: Fragment) {
        fragmentlist.add(fragment)
        notifyItemInserted(fragmentlist.size-1) // 새로운 값이 리스트에 추가되게.
    }
}
