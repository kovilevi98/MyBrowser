package hu.bme.onlab.mybrowser.bookmarks__room

import android.content.Context
import com.airbnb.epoxy.AsyncEpoxyController


class SingleBookMakrController(val context: Context) : AsyncEpoxyController() {

    var bookItems: MutableList<BookMarkEntity> = mutableListOf()
        set(value) {
            field = value
            requestModelBuild()
        }

    private var db: BookMarkDatabase

    init {
        db = BookMarkDatabase.getInstance(context)
    }

    fun setData(tmp: MutableList<BookMarkEntity>) {
        bookItems = tmp
    }

    override fun buildModels() {
        var i: Long = 0

        bookItems.forEach {
            singleBookMark(context) {
                id(i++)
                bookmark(it)
            }
        }
    }
}