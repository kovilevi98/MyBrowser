package hu.bme.onlab.mybrowser.history_room

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.android.material.bottomnavigation.BottomNavigationView
import hu.bme.onlab.mybrowser.R
import hu.bme.onlab.mybrowser.bookmarks__room.h_b_Entity
import kotlinx.android.synthetic.main.activity_history.*

class HistoryActivity : AppCompatActivity() {

    private var bottomNavigation: BottomNavigationView? = null
    val controller = SingleHistoryController(this)
    val ticked = SingleHistoryController.ticked_list.ticked

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        bottomNavigation = findViewById(R.id.bookmarknav_history)
        bookmarknav_history.setOnNavigationItemSelectedListener {
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

        recycler_view_history.setController(controller)
        controller.requestModelBuild()

        getBookMarks().observe(this, Observer {
            controller.historyItems = it.toMutableList()
        })
    }

    private fun getBookMarks(): LiveData<List<h_b_Entity>> {
        return HistoryDatabase.getInstance(this).historyDao().getBookMarkList()
    }


    private fun getItems(): List<h_b_Entity> {
        return HistoryDatabase.getInstance(this).historyDao().getHistory()
    }

    private fun deleteBookMark(bookmarkdata: h_b_Entity) {
        HistoryDatabase.getInstance(this).historyDao().deleteBookMark(bookmarkdata)

    }

    fun removeItems(forDelete: List<h_b_Entity>) {
        forDelete.forEach {
            controller.historyItems.remove(it)
            deleteBookMark(it)
        }
        controller.requestModelBuild()
    }

}