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
import hu.bme.onlab.mybrowser.bookmarks__room.EntityBookMark
import hu.bme.onlab.mybrowser.cookies.CookieActivity
import hu.bme.onlab.mybrowser.cookies.CookieDatabase
import hu.bme.onlab.mybrowser.cookies.CookieFields
import hu.bme.onlab.mybrowser.cookies.Cookie_Entity
import hu.bme.onlab.mybrowser.history_room.HistoryActivity
import hu.bme.onlab.mybrowser.history_room.HistoryEntity
import hu.bme.onlab.mybrowser.tabs.MyAdapter
import hu.bme.onlab.mybrowser.tabs.MyWebView
import kotlinx.android.synthetic.main.activity_web_view.*
import kotlinx.android.synthetic.main.toolbar.view.*
import net.gotev.cookiestore.removeAll
import java.net.CookieManager
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


class WebViewActivity : AppCompatActivity() {

    var cookieManager = CookieManager()
    lateinit var fullscreenView: View
    private var URL = "https://google.com"
    private var isAlreadyCreated = false
    private var bottomNavigation: BottomNavigationView? = null
    lateinit var db: MyDatabase
    lateinit var dbhistory: MyDatabase
    lateinit var dbCookies: CookieDatabase
    private var menu: Menu? = null
    private var filledStar = false
    private var list: List<EntityBookMark>? = null

    var tabLayout: TabLayout? = null
    var viewPager: ViewPager? = null
    private lateinit var adapter: MyAdapter
    var tabsCount = 1
    var tabs: MutableList<MyWebView> = mutableListOf()
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        val ss = intent.getStringExtra("newUrl")
        if (ss != null) {
            val temp = MyWebView()
            temp.setUrl(ss)
            tabs.add(temp)
            tabLayout!!.addTab(tabLayout!!.newTab().setText(temp.getTitle()))
        }

        tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        viewPager = findViewById<ViewPager>(R.id.viewPager)
        tabLayout!!.setupWithViewPager(viewPager)

        tabs.add(MyWebView())

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
        viewPager!!.offscreenPageLimit = tabs.size - 1

        db = MyDatabase.getInstance(this)
        dbhistory = MyDatabase.getInstanceHistory(this)
        dbCookies = CookieDatabase.getInstance(this)

        setSupportActionBar(findViewById(R.id.toolbar))
        bottomNavigation = findViewById(R.id.navigation)

        root_layout.visibility = View.VISIBLE
        fullscreen.visibility = View.GONE

        refresh()

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
                    EntityBookMark(
                        currenturl,
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
            tabs.add(MyWebView())
            adapter.notifyDataSetChanged()
            tabsCount++
            viewPager!!.offscreenPageLimit = tabs.size
            refreshTabs()
            tabLayout!!.getTabAt(tabs.size - 1)?.text = "Google"
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

            Toast.makeText(this, "delete special cookies", Toast.LENGTH_LONG).show()
            true
        }
        R.id.allcookiesdelete -> {
            adapter.tabs[viewPager!!.currentItem].deleteAllCookies()
            val all = getCookies()
            all.forEach {
                deleteCookie(it)
            }
            Toast.makeText(this, "delete cookies", Toast.LENGTH_LONG).show()
            true
        }
        R.id.openCookieActivity -> {
            val intent = Intent(this, CookieActivity::class.java)
            startActivityForResult(intent, 1)
            true
        }
        R.id.listMyCookies -> {
            adapter.tabs[viewPager!!.currentItem].testCookieList()
            true
        }
        R.id.close -> {
            if (viewPager!!.currentItem == 0) {
                Toast.makeText(
                    this,
                    "This is the main tab you can not close this",
                    Toast.LENGTH_LONG
                ).show()
            } else if (tabs.size > 1) {
                //tabLayout?.removeTabAt(tabLayout!!.selectedTabPosition)
                viewPager!!.setCurrentItem(0)
                val removeable = tabLayout!!.selectedTabPosition
                tabs.removeAt(removeable)
                adapter.notifyDataSetChanged()
                tabsCount--
                adapter.notifyDataSetChanged()
                viewPager!!.offscreenPageLimit = tabs.size - 1
                //viewPager!!.removeViewAt(1)
                adapter.notifyDataSetChanged()
                Log.e("tabok szama", tabs.size.toString())
                Toast.makeText(this, "Tabs closed", Toast.LENGTH_LONG).show()
            } else Toast.makeText(this, "This is the last tab", Toast.LENGTH_LONG).show()
            refreshTabs()
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


    private fun insertBookmark(bookmarkdata: EntityBookMark) {
        val dbThread = Thread {
            db.bookMarkDao().insertBookMark(bookmarkdata)
        }
        dbThread.start()
    }

    private fun insertHistory(historydata: HistoryEntity) {
        val dbThread = Thread {
            dbhistory.historyDao().insertHistory(historydata)
        }
        dbThread.start()
    }

    private fun getBookMarks(): LiveData<List<EntityBookMark>> {
        return MyDatabase.getInstance(this).bookMarkDao().getBookMarkList()
    }

    private fun getBookMark(name: String): List<EntityBookMark> {
        return MyDatabase.getInstance(this).bookMarkDao().getSpecificGrades(name)
    }

    private fun deleteBookMark(bookmarkdata: EntityBookMark) {
        MyDatabase.getInstance(this).bookMarkDao().deleteBookMark(bookmarkdata)
    }

    fun getCookies(): List<Cookie_Entity> {
        return CookieDatabase.getInstance(this).cookiedao().getCookie()
    }

    fun deleteCookie(Cookie: Cookie_Entity) {
        CookieDatabase.getInstance(this).cookiedao().deleteCookie(Cookie)
    }


    fun starCheck_LOCAL(list: List<EntityBookMark>) {
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
        adapter.tabs[viewPager!!.currentItem].deleteBackForwardList()
        val currentSize = currentList.size
        for (i in 0 until currentSize) {
            val item = currentList.getItemAtIndex(i)
            val sdf: SimpleDateFormat = SimpleDateFormat(getString(R.string.dateformat))
            val currentDate = sdf.format(Date())
            val tmp = HistoryEntity(item.url, currentDate, item.title)
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
        toolbar.url.setText(string)
    }

    fun refreshTabs() {
        (0 until tabs.size - 1).forEach {
            tabLayout!!.getTabAt(it)?.text = "init"
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val geturl = data!!.extras?.get("MESSAGE").toString()
            setCurrentUrl(geturl)
        }
        if (resultCode == Activity.RESULT_CANCELED) {

            val domainList = getCookies()
            val newCookieList: MutableList<CookieFields> = mutableListOf()
            domainList.forEach {
                if (android.webkit.CookieManager.getInstance().getCookie(it.domain) != null) {
                    val tmp = CookieFields(
                        it.domain,
                        android.webkit.CookieManager.getInstance().getCookie(it.domain)
                    )
                    newCookieList.add(tmp)
                }

            }
            android.webkit.CookieManager.getInstance().removeAll()
            newCookieList.forEach {
                android.webkit.CookieManager.getInstance().setCookie(it.domain_t, it.wholeCookie_t)
            }
        }
    }

    fun addCookie(uri: URL) {
        val tmp = Cookie_Entity(uri.host)
        var used = false
        CookieDatabase.getInstance(this).cookiedao().getCookie().forEach {
            if (it.domain == uri.host)
                used = true
        }
        if (!used)
            CookieDatabase.getInstance(this).cookiedao().insertCookie(tmp)


    }

}