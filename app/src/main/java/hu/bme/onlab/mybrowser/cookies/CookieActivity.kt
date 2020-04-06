package hu.bme.onlab.mybrowser.cookies

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
                    finish()
                    true
                }
                R.id.alldelete -> {
                    //removeItems(ticked)
                    true
                }
                else -> false
            }
        }

        recycler_view_cookie.setController(controller)
    }

}