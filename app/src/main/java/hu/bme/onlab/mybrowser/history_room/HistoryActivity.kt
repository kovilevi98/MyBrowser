package hu.bme.onlab.mybrowser.history_room

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import hu.bme.onlab.mybrowser.R
import kotlinx.android.synthetic.main.activity_history.*

class HistoryActivity : AppCompatActivity() {

    private var bottomNavigation: BottomNavigationView? = null
    val controller = SingleHistoryController(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        /*bottomNavigation = findViewById(R.id.bookmarknav)
        bookmarknav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.back -> {
                    finish()
                    super.onBackPressed()
                    this.finish()
                    true
                }
                R.id.alldelete -> {
                    //removeItems(ticked)
                    true
                }
                else -> false
            }
        }
*/
        recycler_view_history.setController(controller)
        controller.requestModelBuild()
        /*recycler_view.setController(controller)
        getBookMarks().observe(this, Observer {
            controller.bookItems = it.toMutableList()
        })*/
    }
}