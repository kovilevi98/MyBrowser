package hu.bme.onlab.mybrowser.cookies

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import com.google.android.material.bottomnavigation.BottomNavigationView
import hu.bme.onlab.mybrowser.MyDatabase
import hu.bme.onlab.mybrowser.R
import hu.bme.onlab.mybrowser.cookies.entities.CookieEntity
import hu.bme.onlab.mybrowser.cookies.entities.CookieFields
import kotlinx.android.synthetic.main.activity_bookmark.bookmarknav
import kotlinx.android.synthetic.main.activity_cookie.*

class CookieActivity : AppCompatActivity() {
    private var bottomNavigation: BottomNavigationView? = null
    private val controller = SingleCookieController(this, this)
    val ticked = SingleCookieController.tickedList.ticked
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
            controller.refresh()
        })
    }

    private fun getCookies(): LiveData<List<CookieEntity>> {
        return MyDatabase.getInstanceCookie(this).cookiedao().getCookieList()
    }

    private fun removeItems(forDelete: List<CookieFields>) {
        val deleteList = mutableListOf<CookieEntity>()
        forDelete.forEach {
            val temp =
                CookieEntity(it.domain_t)
            deleteList.add(temp)
        }
        deleteList.forEach {
            var cookieData: List<CookieEntity>
            val dbThread = Thread {
                cookieData =
                    MyDatabase.getInstanceCookie(this).cookiedao().getSpecificGrades(it.domain_t)
                cookieData.forEach {
                    MyDatabase.getInstanceCookie(this).cookiedao().deleteCookie(it)
                }
            }
            dbThread.start()
        }

        controller.requestModelBuild()
    }

    private fun finished() {
        val intent = Intent()
        intent.putExtra("deleted cookies", "good")
        setResult(Activity.RESULT_CANCELED, intent)
        finish() //finishing activity
    }

}