package hu.bme.onlab.mybrowser.history_room

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.android.material.bottomnavigation.BottomNavigationView
import hu.bme.onlab.mybrowser.MyDatabase
import hu.bme.onlab.mybrowser.R
import kotlinx.android.synthetic.main.activity_history.*

class HistoryActivity : AppCompatActivity() {

    private var bottomNavigation: BottomNavigationView? = null
    private val controller = SingleHistoryController(this, this)
    val ticked = SingleHistoryController.tickedList.ticked

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        bottomNavigation = findViewById(R.id.bookmarknav_history)
        bookmarknav_history.setOnNavigationItemSelectedListener {
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

        recycler_view_history.setController(controller)
        controller.requestModelBuild()

        getHistories().observe(this, Observer {
            controller.historyItems = it.toMutableList()
        })
    }

    private fun getHistories(): LiveData<List<HistoryEntity>> {
        return MyDatabase.getInstanceHistory(this).historyDao().getHistoryList()
    }


    private fun getItems(): List<HistoryEntity> {
        return MyDatabase.getInstanceHistory(this).historyDao().getHistory()
    }

    private fun deleteBookMark(historyData: HistoryEntity) {
        MyDatabase.getInstanceHistory(this).historyDao().deleteHistory(historyData)

    }

    private fun removeItems(forDelete: List<HistoryEntity>) {
        forDelete.forEach {
            controller.historyItems.remove(it)
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