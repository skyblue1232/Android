package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentLookBinding
import com.google.android.material.tabs.TabLayout

class LookFragment : Fragment() {

    private var _binding: FragmentLookBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLookBinding.inflate(inflater, container, false)

        initTabLayout()

        return binding.root
    }

    private fun initTabLayout(){
        with(binding){
            lookContentTb.addTab(lookContentTb.newTab().setText("차트"))
            lookContentTb.addTab(lookContentTb.newTab().setText("영상"))
            lookContentTb.addTab(lookContentTb.newTab().setText("장르"))
            lookContentTb.addTab(lookContentTb.newTab().setText("상황"))
            lookContentTb.addTab(lookContentTb.newTab().setText("분위기"))

            lookContentTb.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when (tab?.position) {
                        0 -> {
                            // Tab 1 Selected
                        }
                        1 -> {
                            // Tab 2 Selected
                        }
                        2 -> {
                            // Tab 3 Selected
                        }
                        3 -> {

                        }

                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {

                }

                override fun onTabReselected(tab: TabLayout.Tab?) {

                }
            })

            setTabItemMargin(binding.lookContentTb, 25)
        }
    }

    private fun setTabItemMargin(tabLayout: TabLayout, marginEnd: Int = 20) {
        for(i in 0 until 3) {
            val tabs = tabLayout.getChildAt(0) as ViewGroup
            for(i in 0 until tabs.childCount) {
                val tab = tabs.getChildAt(i)
                val lp: LinearLayout.LayoutParams = tab.layoutParams as LinearLayout.LayoutParams
                lp.marginEnd = marginEnd
                tab.layoutParams = lp
                tab.layoutParams= lp
                tabLayout.requestLayout()
            }
        }
    }
}