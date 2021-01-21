package com.yhh.library_demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

// 페이저를 이용하여 프래그먼트 슬라이드 조작
class MainPagerActivity : AppCompatActivity() {

    lateinit var mainTabLayout: TabLayout
    lateinit var mainViewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_pager)

        initView()

        val pagerAdapter = MainPagerAdapter(this@MainPagerActivity)
        mainViewPager.adapter = pagerAdapter

        // 탭 레이아웃 설정 (페이저와 붙이는 작업)
        TabLayoutMediator(mainTabLayout, mainViewPager){ tab, position ->
            when(position){
                0 -> tab.text = "도서관"
                1 -> tab.text = "게시판"
                2 -> tab.text = "설정"
                else -> tab.text = ""
            }
        }.attach()
    }

    fun initView(){
        mainTabLayout = findViewById(R.id.mainTabLayout)
        mainViewPager = findViewById(R.id.mainViewPager)
    }

    // 페이저 어댑터
    inner class MainPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa){
        override fun getItemCount(): Int {
            return 3
        }

        override fun createFragment(position: Int): Fragment {
            return when(position){
                0 -> LibraryInfoFragment()
                1 -> BbsFragment()
                2 -> SettingFragment()
                else -> LibraryInfoFragment()
            }
        }
    }
}