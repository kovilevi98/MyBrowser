package hu.bme.onlab.mybrowser.history_room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import hu.bme.onlab.mybrowser.bookmarks__room.h_b_Dao
import hu.bme.onlab.mybrowser.bookmarks__room.h_b_Entity


private val DATABASE = "history01.db"

@Database(entities = arrayOf(h_b_Entity::class), version = 1)
abstract class HistoryDatabase() : RoomDatabase() {

    abstract fun historyDao(): h_b_Dao

    companion object {

        private var instance: HistoryDatabase? = null

        fun getInstance(context: Context): HistoryDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    HistoryDatabase::class.java,
                    DATABASE
                ).allowMainThreadQueries().build()
            }
            return instance!!
        }
    }
}