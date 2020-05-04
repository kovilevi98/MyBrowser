package hu.bme.onlab.mybrowser.history_room

import android.content.Context
import com.airbnb.epoxy.AsyncEpoxyController
import hu.bme.onlab.mybrowser.MyDatabase

class SingleHistoryController(val context: Context, val activity: HistoryActivity) :
    AsyncEpoxyController() {

    var historyItems: MutableList<HistoryEntity> = mutableListOf()
        set(value) {
            field = value
            requestModelBuild()
        }

    private var db: MyDatabase = MyDatabase.getInstanceHistory(context)
    init {
        historyItems = db.historyDao().getHistory().toMutableList()
    }

    override fun buildModels() {
        var i: Long = 0
        historyItems.forEach {
            singleHistory(context, activity = activity) {
                id(i++)
                historyItem(it)
            }
        }
    }

    object tickedList {
        var ticked: MutableList<HistoryEntity> = ArrayList()
    }
}