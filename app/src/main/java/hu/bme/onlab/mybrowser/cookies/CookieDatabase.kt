package hu.bme.onlab.mybrowser.cookies

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


private val DATABASE = "cookies01.db"

@Database(entities = arrayOf(Cookie_Entity::class), version = 1)
abstract class CookieDatabase() : RoomDatabase() {

    abstract fun cookiedao(): CookieDao

    companion object {

        private var instance: CookieDatabase? = null

        fun getInstance(context: Context): CookieDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    CookieDatabase::class.java,
                    DATABASE
                ).allowMainThreadQueries().build()
            }
            return instance!!
        }
    }

}