package hu.bme.onlab.mybrowser.bookmarks__room

import android.content.Context
import com.airbnb.epoxy.AsyncEpoxyController
import hu.bme.onlab.mybrowser.MyDatabase

class SingleBookMarkController(val context: Context, val activity: BookMarkActivity) :
    AsyncEpoxyController() {

    var bookItems: MutableList<EntityBookMark> = mutableListOf()
        set(value) {
            field = value
            requestModelBuild()
        }

    private var db: MyDatabase = MyDatabase.getInstance(context)

    override fun buildModels() {
        var i: Long = 0
        bookItems.forEach {
            singleBookMark(context, activity) {
                id(i++)
                bookmark(it)
            }
        }
    }

    object TickedList {
        var ticked: MutableList<EntityBookMark> = ArrayList()
    }
}