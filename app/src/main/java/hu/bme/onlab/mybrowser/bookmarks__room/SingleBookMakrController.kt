package hu.bme.onlab.mybrowser.bookmarks__room

import android.content.Context
import com.airbnb.epoxy.AsyncEpoxyController


class SingleBookMakrController(context: Context) : AsyncEpoxyController() {
    var bookItems: MutableList<BookMarkEntity> = mutableListOf()
        set(value) {
            field = value
            requestModelBuild()
        }

    private var db: BookMarkDatabase

    init {
        db = BookMarkDatabase.getInstance(context)
    }

    override fun buildModels() {
        var i: Long = 0

        bookItems.forEach {
            singleBookMark {
                id(i++)
                bookmark(it)
            }
        }
    }

}