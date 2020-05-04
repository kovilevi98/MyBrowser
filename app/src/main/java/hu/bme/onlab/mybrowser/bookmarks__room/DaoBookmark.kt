package hu.bme.onlab.mybrowser.bookmarks__room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DaoBookmark {

    @Query("Select * from bookmark")
    fun getBookMarkList(): LiveData<List<EntityBookMark>>

    @Query("Select * from bookmark")
    fun getHistory(): List<EntityBookMark>

    @Query("SELECT * FROM bookmark WHERE url = :name")
    fun getSpecificGrades(name: String): List<EntityBookMark>

    @Query("SELECT * FROM bookmark WHERE title = :name")
    fun getFromTitle(name: String): List<EntityBookMark>

    @Delete
    fun deleteBookMark(hb: EntityBookMark)

    @Update
    fun updateBookMark(hb: EntityBookMark)

    @Insert
    fun insertBookMark(vararg hb: EntityBookMark)
}