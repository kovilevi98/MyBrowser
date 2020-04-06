package hu.bme.onlab.mybrowser.bookmarks__room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

private val DATABASE = "bookmarks_03.db"

@Database(entities = arrayOf(h_b_Entity::class), version = 1)
abstract class BookMarkDatabase() : RoomDatabase() {

    abstract fun bookMarkDao(): h_b_Dao

        companion object{

            private var instance: BookMarkDatabase? =null

            fun getInstance(context: Context) : BookMarkDatabase{
                if(instance == null){
                    instance = Room.databaseBuilder(context.getApplicationContext(),BookMarkDatabase::class.java, DATABASE).allowMainThreadQueries().build()
                }
                return instance!!
            }
        }

}