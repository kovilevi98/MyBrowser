package hu.bme.onlab.mybrowser.cookies

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import com.google.android.material.bottomnavigation.BottomNavigationView
import hu.bme.onlab.mybrowser.R
import kotlinx.android.synthetic.main.activity_bookmark.bookmarknav
import kotlinx.android.synthetic.main.activity_cookie.*

class CookieActivity : AppCompatActivity() {
    private var bottomNavigation: BottomNavigationView? = null
    val controller = SingleCookieController(this, this)
    val ticked = SingleCookieController.ticked_list.ticked
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cookie)
        bottomNavigation = findViewById(R.id.bookmarknav)
        bookmarknav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.back2 -> {
                    finished()
                    true
                }
                R.id.alldelete -> {
                    removeItems(ticked)
                    true
                }
                else -> false
            }
        }

        recycler_view_cookie.setController(controller)
        getCookies().observe(this, androidx.lifecycle.Observer {
            controller.cookieItems = it.toMutableList()
        })
    }

    private fun getCookies(): LiveData<List<Cookie_Entity>> {
        return CookieDatabase.getInstance(this).cookiedao().getCookieList()
    }

    private fun deleteCookie(Cookie: Cookie_Entity) {
        val dbThread = Thread {
            CookieDatabase.getInstance(this).cookiedao().deleteCookie(Cookie)
        }
        dbThread.start()
    }

    fun removeItems(forDelete: List<Cookie_Entity>) {
        forDelete.forEach {
            controller.cookieItems.remove(it)
            deleteCookie(it)
        }
        controller.requestModelBuild()
    }

    fun finished() {
        val intent = Intent()
        intent.putExtra("deleted cookies", "good")
        setResult(Activity.RESULT_CANCELED, intent)
        finish() //finishing activity
    }

}