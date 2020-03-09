package hu.bme.onlab.mybrowser.bookmarks__room

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hu.bme.onlab.mybrowser.R
import kotlinx.android.synthetic.main.activity_bookmark.*


class BookMarkActivity : AppCompatActivity() {

    /*private val recycleView : RecyclerView by lazy {
        findViewById<RecyclerView>(R.id.recycler_view)
    }

    private val singleBookController : SingleBookMakrController by lazy {
        SingleBookMakrController(this)
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookmark)
      //  initRecycler()
        val controller = SingleBookMakrController(this)
       recycler_view.setController(controller)
        //controller.bookItems=
        getBookMarks().observe(this, Observer {
           controller.bookItems = it
        })
        Log.e("a mostani meret",controller.bookItems.size.toString())
        }

    private fun getBookMarks(): LiveData<List<BookMarkEntity>> {
        return BookMarkDatabase.getInstance(this).bookMarkDao().getBookMarkList()
    }

   /* private fun initRecycler() {
        val linearLayoutManager = LinearLayoutManager(this)

        recycleView.apply {
            layoutManager = linearLayoutManager
            setHasFixedSize(true)
            adapter = SingleBookMakrController.adapter
        }
    }*/
}
