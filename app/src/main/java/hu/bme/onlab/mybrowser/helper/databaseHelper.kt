package hu.bme.onlab.mybrowser.helper

import android.content.Context
import hu.bme.onlab.mybrowser.bookmarks__room.h_b_Entity
import hu.bme.onlab.mybrowser.history_room.HistoryDatabase

class databaseHelper(val context: Context) {
    fun sortAbc() {
        val list = HistoryDatabase.getInstance(context).historyDao().getHistory()
        val newList = list.sortedBy { it.title }
        list.forEach {
            HistoryDatabase.getInstance(context).historyDao().deleteBookMark(it)
        }
        newList.forEach {
            HistoryDatabase.getInstance(context).historyDao().insertBookMark(it)
        }
    }

    fun sortTime(list: List<h_b_Entity>): List<h_b_Entity> {
        return list.sortedBy { it.time }
    }
}