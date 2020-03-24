package hu.bme.onlab.mybrowser.tabs

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


class MyAdapter(
    private val myContext: Context, fm: FragmentManager, private var totalTabs: Int,
    var tabs: MutableList<MyWebView_>
) : FragmentPagerAdapter(fm) {


    // this is for fragment tabs
    override fun getItem(position: Int): Fragment {
        val tmp = MyWebView_()
        tabs.add(tmp)
        return tmp
    }

    // this counts total number of tabs
    override fun getCount(): Int {
        return totalTabs
    }
}