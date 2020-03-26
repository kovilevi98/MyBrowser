package hu.bme.onlab.mybrowser.bookmarks__room

import android.content.Context
import com.airbnb.epoxy.AsyncEpoxyController

class SingleBookMakrController(val context: Context, val activity: BookMarkActivity) :
    AsyncEpoxyController() {

    var bookItems: MutableList<h_b_Entity> = mutableListOf()
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
            singleBookMark(context, activity) {
                id(i++)
                bookmark(it)
            }
        }
    }

    object ticked_list {
        var ticked: MutableList<h_b_Entity> = ArrayList()
    }
}