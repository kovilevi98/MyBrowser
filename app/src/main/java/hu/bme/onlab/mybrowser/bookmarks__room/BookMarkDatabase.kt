package hu.bme.onlab.mybrowser.bookmarks__room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

private val DATABASE= "bookmarks.db"

@Database(entities = arrayOf(BookMarkEntity::class), version = 1)
abstract class BookMarkDatabase() : RoomDatabase() {

        abstract fun bookMarkDao(): BookMarkDao

        companion object{

            private var instance: BookMarkDatabase? =null

            fun getInstance(context: Context) : BookMarkDatabase{
                if(instance == null){
                    instance = Room.databaseBuilder(context.getApplicationContext(),BookMarkDatabase::class.java, DATABASE).allowMainThreadQueries().build()
                }
                return instance!!
            }


            private fun buildDatabase(context: Context): BookMarkDatabase {
                return Room.databaseBuilder(context, BookMarkDatabase::class.java, DATABASE).allowMainThreadQueries().build()
            }
        }
}