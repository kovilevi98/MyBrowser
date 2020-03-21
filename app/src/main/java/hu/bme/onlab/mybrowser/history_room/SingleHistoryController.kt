package hu.bme.onlab.mybrowser.history_room

import android.content.Context
import com.airbnb.epoxy.AsyncEpoxyController
import hu.bme.onlab.mybrowser.bookmarks__room.h_b_Entity

class SingleHistoryController(val context: Context) : AsyncEpoxyController() {

    var historyItems: MutableList<h_b_Entity> = mutableListOf()
        set(value) {
            field = value
            requestModelBuild()
        }

    private var db: HistoryDatabase

    init {
        db = HistoryDatabase.getInstance(context)
        historyItems = db.historyDao().getHistory().toMutableList()
    }

    override fun buildModels() {
        var i: Long = 0
        historyItems.forEach {
            singleHistory(context) {
                id(i++)
                historyItem(it)
            }
        }
    }

    object ticked_list {
        var ticked: MutableList<h_b_Entity> = ArrayList()
    }
}