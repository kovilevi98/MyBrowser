package hu.bme.onlab.mybrowser

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebBackForwardList
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import hu.bme.onlab.mybrowser.bookmarks__room.BookMarkActivity
import hu.bme.onlab.mybrowser.bookmarks__room.BookMarkDatabase
import hu.bme.onlab.mybrowser.bookmarks__room.h_b_Entity
import hu.bme.onlab.mybrowser.history_room.HistoryActivity
import hu.bme.onlab.mybrowser.history_room.HistoryDatabase
import hu.bme.onlab.mybrowser.tabs.MyAdapter
import hu.bme.onlab.mybrowser.tabs.MyWebView_
import kotlinx.android.synthetic.main.activity_web_view.*
import kotlinx.android.synthetic.main.toolbar.view.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*


class WebViewActivity : AppCompatActivity() {


    lateinit var fullscreenView: View
    private var URL = "https://google.com"
    private var isAlreadyCreated = false
    private var startPage = "https://www.google.com/"
    private var bottomNavigation: BottomNavigationView? = null
    lateinit var db: BookMarkDatabase
    lateinit var dbhistory: HistoryDatabase
    private var menu: Menu? = null
    private var filledStar = false
    private var list: List<h_b_Entity>? = null

    var tabLayout: TabLayout? = null
    var viewPager: ViewPager? = null
    lateinit var adapter: MyAdapter
    var tabsCount = 3
    var tabs: MutableList<MyWebView_> = mutableListOf()
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        val ss = intent.getStringExtra("newUrl")
        if (ss != null) {
            val temp = MyWebView_()
            temp.setUrl(ss)
            tabs.add(temp)

            tabLayout!!.addTab(tabLayout!!.newTab().setText(temp.getTitle()))
        }


        tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        viewPager = findViewById<ViewPager>(R.id.viewPager)
        //viewPager.offscreenPageLimit
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Google"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Google"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Google"))
        tabs.add(MyWebView_())
        tabs.add(MyWebView_())
        tabs.add(MyWebView_())


        adapter = MyAdapter(this, supportFragmentManager, tabsCount, tabs)
        viewPager!!.adapter = adapter



        viewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager!!.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
        viewPager!!.offscreenPageLimit = tabs.size

        db = BookMarkDatabase.getInstance(this)
        dbhistory = HistoryDatabase.getInstance(this)

        //db.bookMarkDao().insertBookMark(BookMarkEntity("asd","sad"))*/

        setSupportActionBar(findViewById(R.id.toolbar))
        bottomNavigation = findViewById(R.id.navigation)


        // startLoaderAnimate()
        root_layout.visibility = View.VISIBLE
        fullscreen.visibility = View.GONE

        refresh()
        //initWebView()

        //webView.loadUrl(startPage)
        toolbar.searchbutton.setOnClickListener {
            val url = toolbar.url.text.toString()
            createurl(url)
            adapter.tabs[viewPager!!.currentItem].setUrl(URL)
            toolbar.url.setText(URL)
        }

        navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.back -> {
                    if (adapter.tabs[viewPager!!.currentItem].canGoBack())
                        adapter.tabs[viewPager!!.currentItem].goBack()
                    URL = adapter.tabs[viewPager!!.currentItem].getUrl()
                    toolbar.url.setText(URL)
                    true
                }
                R.id.forward -> {
                    if (adapter.tabs[viewPager!!.currentItem].canGoForward())
                        adapter.tabs[viewPager!!.currentItem].goForward()
                    URL = adapter.tabs[viewPager!!.currentItem].getUrl()
                    toolbar.url.setText(URL)
                    true
                }
                R.id.refresh -> {
                    adapter.tabs[viewPager!!.currentItem].reload()
                    toolbar.url.setText(URL)
                    URL = adapter.tabs[viewPager!!.currentItem].getUrl()
                    true
                }
                else -> false
            }
        }

        toolbar.url.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                val url = toolbar.url.text.toString()
                createurl(url)
                adapter.tabs[viewPager!!.currentItem].loadUrl(URL)
                toolbar.url.setText(URL)
                return@OnKeyListener true
            }
            false
        })

        getBookMarks().observe(this, Observer {
            starCheck_LOCAL(it)
            list = it
            adapter.tabs[viewPager!!.currentItem].setList(list)
        })
    }

    private fun refresh() {
        swipeRefreshLayout.setOnRefreshListener {
            adapter.tabs[viewPager!!.currentItem].reload()
            swipeRefreshLayout.setRefreshing(false)
        }
    }


    private fun createurl(url: String) {
        if (url.take(3) == "www" || url.take(3) == "WWW")
            URL = ("https://" + url)
        else {
            if (url.contains('.') && url.take(12) != "https://www.")
                URL = ("https://www." + url)
            else {
                if (url.take(12) == "https://www.")
                    URL = url
                else URL = ("https://www.google.com/search?q=" + url)
            }
        }
    }

    //setting menu in action bar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.my_menu, menu)
        this.menu = menu
        return super.onCreateOptionsMenu(menu)
    }

    // actions on click menu items
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.star -> {
            val currenturl = adapter.tabs[viewPager!!.currentItem].getUrl()
            if (!filledStar) {
                insertBookmark(
                    h_b_Entity(
                        currenturl,
                        LocalDateTime.now().toString(),
                        adapter.tabs[viewPager!!.currentItem].getTitle()
                    )
                )
                starChanger(1,R.drawable.ic_action_star_10)
            } else {
                val list = getBookMark(currenturl)
                starChanger(1,R.drawable.ic_star_0)
                for (i in list)
                    deleteBookMark(i)
            }
            true
        }
        R.id.tabs -> {
            tabLayout!!.addTab(tabLayout!!.newTab().setText("Google"))
            tabs.add(MyWebView_())
            adapter.notifyDataSetChanged()
            tabsCount++
            viewPager!!.offscreenPageLimit = tabs.size
            Toast.makeText(this, "new tab", Toast.LENGTH_LONG).show()
            true
        }

        R.id.Bookmark -> {
            val intent = Intent(this, BookMarkActivity::class.java)
            startActivityForResult(intent, 1)
            true
        }
        R.id.history -> {
            getBackForwardList()
            val intent = Intent(this, HistoryActivity::class.java)
            startActivityForResult(intent, 1)
            true
        }
        R.id.Cookie -> {
            tabLayout!!.getTabAt(0)?.let { tabLayout!!.removeTab(it) }
            Toast.makeText(this, "tab removed", Toast.LENGTH_LONG).show()
            true
        }
        R.id.close -> {
            /* tabLayout?.removeTabAt(tabLayout!!.selectedTabPosition)
             tabs.removeAt(viewPager!!.currentItem)
             adapter.notifyDataSetChanged()
             tabsCount--*/
            /* tabs.removeAt(viewPager!!.currentItem)
             adapter.notifyDataSetChanged();*/
            Log.e("tab", viewPager!!.currentItem.toString())
            true
        }
        android.R.id.home -> {
            Toast.makeText(this, "Home action", Toast.LENGTH_LONG).show()
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        if (isAlreadyCreated && !isNetworkAvailable()) {
            isAlreadyCreated = false
            showErrorDialog(
                "Error", "No internet connection. Please check your connection.",
                this@WebViewActivity
            )
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectionManager =
            this@WebViewActivity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectionManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnectedOrConnecting
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && adapter.tabs[viewPager!!.currentItem].canGoBack()) {
            adapter.tabs[viewPager!!.currentItem].goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun showErrorDialog(title: String, message: String, context: Context) {
        val dialog = AlertDialog.Builder(context)
        dialog.setTitle(title)
        dialog.setMessage(message)
        dialog.setNegativeButton("Cancel", { _, _ ->
            this@WebViewActivity.finish()
        })

        dialog.setPositiveButton("Retry", { _, _ ->
            this@WebViewActivity.recreate()
        })
        dialog.create().show()
    }


    private fun insertBookmark(bookmarkdata: h_b_Entity) {
        val dbThread = Thread {
            db.bookMarkDao().insertBookMark(bookmarkdata)
        }
        dbThread.start()
    }

    private fun insertHistory(historydata: h_b_Entity) {
        val dbThread = Thread {
            dbhistory.historyDao().insertBookMark(historydata)
        }
        dbThread.start()
    }

    private fun getBookMarks(): LiveData<List<h_b_Entity>> {
        return BookMarkDatabase.getInstance(this).bookMarkDao().getBookMarkList()
    }

    private fun getBookMark(name: String): List<h_b_Entity> {
        return BookMarkDatabase.getInstance(this).bookMarkDao().getSpecificGrades(name)
    }

    private fun deleteBookMark(bookmarkdata: h_b_Entity) {
        BookMarkDatabase.getInstance(this).bookMarkDao().deleteBookMark(bookmarkdata)
    }


    fun starCheck_LOCAL(list: List<h_b_Entity>) {
        val currenturl = adapter.tabs[viewPager!!.currentItem].getUrl()
        var new = true
        for (i in list) {
            if (i.url == currenturl)
                new = false
        }
        if (!new) {
            starChanger(1,R.drawable.ic_action_star_10)
            filledStar = true
        } else {
            starChanger(1,R.drawable.ic_star_0)
            filledStar = false
        }
    }
    private fun starChanger(index:Int,@DrawableRes id : Int){
        menu?.getItem(index)?.icon = ContextCompat.getDrawable(this, id)
    }

    @SuppressLint("SimpleDateFormat")
    fun getBackForwardList() {
        val currentList: WebBackForwardList =
            adapter.tabs[viewPager!!.currentItem].copyBackForwardList()
        val currentSize = currentList.size
        for (i in 0 until currentSize) {
            val item = currentList.getItemAtIndex(i)
            val sdf: SimpleDateFormat
            sdf = SimpleDateFormat(getString(R.string.dateformat))
            val currentDate = sdf.format(Date())
            val tmp = h_b_Entity(item.url, currentDate, item.title)
            insertHistory(tmp)
        }
    }

    fun setText(url: String) {
        toolbar.url.setText(url)
    }

    fun setVideo(view: View) {
        fullscreenView = view
        fullscreen.addView(fullscreenView)
        fullscreen.visibility = View.VISIBLE
        root_layout.visibility = View.GONE
        navigation.visibility = View.INVISIBLE
    }

    fun removeVideo() {
        fullscreen.removeView(fullscreenView)
        fullscreen.visibility = View.GONE
        root_layout.visibility = View.VISIBLE
        navigation.visibility = View.VISIBLE
    }

    fun setTabText(title: String) {
        tabLayout!!.getTabAt(viewPager!!.currentItem)?.text = title
    }

    fun setScroll(b: Boolean) {
        swipeRefreshLayout.isEnabled = b
    }

    fun setCurrentUrl(string: String) {
        adapter.tabs[viewPager!!.currentItem].setUrl(string)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val geturl = data!!.extras?.get("MESSAGE").toString()
            setCurrentUrl(geturl)
        }

    }
}