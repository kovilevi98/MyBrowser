package hu.bme.onlab.mybrowser.bookmarks__room

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.android.material.bottomnavigation.BottomNavigationView
import hu.bme.onlab.mybrowser.R
import kotlinx.android.synthetic.main.activity_bookmark.*


class BookMarkActivity : AppCompatActivity() {
    private var bottomNavigation: BottomNavigationView? = null
    val controller = SingleBookMakrController(this)
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
                    removeItems(getDeleted())
                    Toast.makeText(this,getDeleted().size.toString(),Toast.LENGTH_LONG).show()
                    true
                }
                else -> false
            }
        }

      //  initRecycler()

       recycler_view.setController(controller)
        //controller.bookItems=
        getBookMarks().observe(this, Observer {
           controller.bookItems = it.toMutableList()
        })
        //Log.e("a mostani meret",controller.bookItems.size.toString())

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

    fun getDeleted():List<BookMarkEntity>{
        var tmp :MutableList<BookMarkEntity> = mutableListOf()
        controller.bookItems.forEach{
            //Log.e("sad",it.isChecked.toString())
            if(it.isChecked)
                tmp.add(it)
        }
        return tmp
    }

    public fun removeItems(forDelete: List<BookMarkEntity>) {
        forDelete.forEach{
            controller.bookItems.remove(it)
            deleteBookMark(it)
        }
        controller.requestModelBuild()
    }


}
