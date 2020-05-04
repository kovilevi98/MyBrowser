package hu.bme.onlab.mybrowser

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import hu.bme.onlab.mybrowser.bookmarks__room.DaoBookmark
import hu.bme.onlab.mybrowser.bookmarks__room.EntityBookMark
import hu.bme.onlab.mybrowser.cookies.CookieDao
import hu.bme.onlab.mybrowser.cookies.entities.CookieEntity
import hu.bme.onlab.mybrowser.history_room.DaoHistory
import hu.bme.onlab.mybrowser.history_room.HistoryEntity

private val DATABASE = "bookmarks_04.db"
private val DATABASEHISTORY = "historys_03.db"
private val DATABASECOOKIE = "cookies03.db"

@Database(
    entities = arrayOf(EntityBookMark::class, HistoryEntity::class, CookieEntity::class),
    version = 1
)

abstract class MyDatabase() : RoomDatabase() {


    abstract fun bookMarkDao(): DaoBookmark
    abstract fun historyDao(): DaoHistory
    abstract fun cookiedao(): CookieDao

    companion object {

        private var instance: MyDatabase? = null
        private var historyInstance: MyDatabase? = null
        private var cookieInstance: MyDatabase? = null
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
            if (historyInstance == null) {
                historyInstance = Room.databaseBuilder(
                    context.applicationContext,
                    MyDatabase::class.java,
                    DATABASEHISTORY
                ).allowMainThreadQueries().build()
            }
            return historyInstance!!
        }

        fun getInstanceCookie(context: Context): MyDatabase {
            if (cookieInstance == null) {
                cookieInstance = Room.databaseBuilder(
                    context.applicationContext,
                    MyDatabase::class.java,
                    DATABASECOOKIE
                ).allowMainThreadQueries().build()
            }
            return cookieInstance!!
        }

    }

}