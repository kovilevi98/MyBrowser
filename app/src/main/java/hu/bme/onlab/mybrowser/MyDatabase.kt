package hu.bme.onlab.mybrowser

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import hu.bme.onlab.mybrowser.bookmarks__room.DaoBookmark
import hu.bme.onlab.mybrowser.bookmarks__room.EntityBookMark
import hu.bme.onlab.mybrowser.history_room.DaoHistory
import hu.bme.onlab.mybrowser.history_room.HistoryEntity

private val DATABASE = "bookmarks_04.db"
private val DATABASEHISTORY = "historys_02.db"

@Database(entities = arrayOf(EntityBookMark::class, HistoryEntity::class), version = 3)

abstract class MyDatabase() : RoomDatabase() {


    abstract fun bookMarkDao(): DaoBookmark
    abstract fun historyDao(): DaoHistory

    companion object {

        private var instance: MyDatabase? = null
        private var historyInstance: MyDatabase? = null
        fun getInstance(context: Context): MyDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyDatabase::class.java,
                    DATABASE
                ).allowMainThreadQueries().build()
            }
            return instance!!
        }

        fun getInstanceHistory(context: Context): MyDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyDatabase::class.java,
                    DATABASEHISTORY
                ).allowMainThreadQueries().build()
            }
            return instance!!
        }
    }

}