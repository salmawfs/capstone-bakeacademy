package com.athar.bakeacademy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    var token: String? = ""
    var id: String? = ""

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null

        when (position) {
            0 -> fragment = NutrisiFragment()
            1 -> fragment = BahanFragment()
            2 -> fragment = LangkahFragment()
        }
        if (fragment != null) {
            fragment.arguments = Bundle().apply {
                putString(BahanFragment.TOKEN, token)
                putString(LangkahFragment.TOKEN, token)
                putString(BahanFragment.ID, id)
                putString(LangkahFragment.ID, id)
            }
        }
        return fragment as Fragment
    }

}