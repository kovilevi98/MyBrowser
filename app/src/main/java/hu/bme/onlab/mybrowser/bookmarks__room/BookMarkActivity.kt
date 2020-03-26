package hu.bme.onlab.mybrowser.bookmarks__room

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.android.material.bottomnavigation.BottomNavigationView
import hu.bme.onlab.mybrowser.R
import kotlinx.android.synthetic.main.activity_bookmark.*


class BookMarkActivity : AppCompatActivity() {
    private var bottomNavigation: BottomNavigationView? = null
    val controller = SingleBookMakrController(this, this)
    val ticked = SingleBookMakrController.ticked_list.ticked
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookmark)
        bottomNavigation = findViewById(R.id.bookmarknav)
        bookmarknav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.back2 -> {
                    finish()
                    true
                }
                R.id.alldelete -> {
                    removeItems(ticked)
                    true
                }
                else -> false
            }
        }

        recycler_view.setController(controller)
        getBookMarks().observe(this, Observer {
            controller.bookItems = it.toMutableList()
        })

    }

    private fun getBookMarks(): LiveData<List<h_b_Entity>> {
        return BookMarkDatabase.getInstance(this).bookMarkDao().getBookMarkList()
    }

    private fun deleteBookMark(bookmarkdata: h_b_Entity) {
        val dbThread = Thread {
            BookMarkDatabase.getInstance(this).bookMarkDao().deleteBookMark(bookmarkdata)
        }
        dbThread.start()
    }

    fun removeItems(forDelete: List<h_b_Entity>) {
        forDelete.forEach {
            controller.bookItems.remove(it)
            deleteBookMark(it)
        }
        controller.requestModelBuild()
    }

    fun finished(s: String) {
        val intent = Intent()
        intent.putExtra("MESSAGE", s)
        setResult(Activity.RESULT_OK, intent)
        finish() //finishing activity
    }
}
