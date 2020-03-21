package hu.bme.onlab.mybrowser.bookmarks__room

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.android.material.bottomnavigation.BottomNavigationView
import hu.bme.onlab.mybrowser.R
import kotlinx.android.synthetic.main.activity_bookmark.*

class BookMarkActivity : AppCompatActivity() {
    private var bottomNavigation: BottomNavigationView? = null
    val controller = SingleBookMakrController(this)
    val ticked = SingleBookMakrController.ticked_list.ticked
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookmark)
        bottomNavigation = findViewById(R.id.bookmarknav)
        bookmarknav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.back -> {
                    finish()
                    super.onBackPressed()
                    this.finish()
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

    private fun getBookMarks(): LiveData<List<BookMarkEntity>> {
        return BookMarkDatabase.getInstance(this).bookMarkDao().getBookMarkList()
    }

    private fun deleteBookMark(bookmarkdata: BookMarkEntity) {
        val dbThread = Thread {
            BookMarkDatabase.getInstance(this).bookMarkDao().deleteBookMark(bookmarkdata)
        }
        dbThread.start()
    }

    fun removeItems(forDelete: List<BookMarkEntity>) {
        var valami: String
        var valami2: Int
        forDelete.forEach {
            controller.bookItems.remove(it)
            deleteBookMark(it)
        }
        controller.requestModelBuild()
    }
}
