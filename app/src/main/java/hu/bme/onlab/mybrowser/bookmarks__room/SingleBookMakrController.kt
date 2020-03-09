package hu.bme.onlab.mybrowser.bookmarks__room

import android.content.Context
import com.airbnb.epoxy.AsyncEpoxyController
import com.airbnb.epoxy.EpoxyController
import hu.bme.onlab.mybrowser.WebViewActivity
import kotlinx.android.synthetic.main.singlebookmark.view.*


class SingleBookMakrController(context : Context) : AsyncEpoxyController(){
    var bookItems : List<BookMarkEntity> = emptyList()
        set(value){
            field = value
            requestModelBuild()
        }

    private lateinit var db:BookMarkDatabase
    init {
        db= BookMarkDatabase.getInstance(context)
        //bookItems = db.bookMarkDao().getBookMarkList()
    }

    override fun buildModels() {
        var i:Long=0

        bookItems.forEach{
            singleBookMark {
                id(i++)
                bookmark(it)
            }
        }
    }

}