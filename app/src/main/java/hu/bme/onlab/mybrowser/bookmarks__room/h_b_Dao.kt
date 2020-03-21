package hu.bme.onlab.mybrowser.bookmarks__room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface h_b_Dao {

    @Query("Select * from bookmark")
    fun getBookMarkList(): LiveData<List<h_b_Entity>>

    @Query("Select * from bookmark")
    fun getHistory(): List<h_b_Entity>

    @Query("SELECT * FROM bookmark WHERE url = :name")
    fun getSpecificGrades(name: String): List<h_b_Entity>

    @Query("SELECT * FROM bookmark WHERE title = :name")
    fun getFromTitle(name: String): List<h_b_Entity>

    @Delete
    fun deleteBookMark(hb: h_b_Entity)

    @Update
    fun updateBookMark(hb: h_b_Entity)

    @Insert
    fun insertBookMark(vararg hb: h_b_Entity)
}