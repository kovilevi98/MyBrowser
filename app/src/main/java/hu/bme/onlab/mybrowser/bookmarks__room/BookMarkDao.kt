package hu.bme.onlab.mybrowser.bookmarks__room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BookMarkDao {

    @Query("Select * from bookmark")
    fun getBookMarkList(): LiveData<List<BookMarkEntity>>

    @Query("SELECT * FROM bookmark WHERE url = :name")
    fun getSpecificGrades(name: String): List<BookMarkEntity>

    @Query("SELECT * FROM bookmark WHERE title = :name")
    fun getFromTitle(name: String): List<BookMarkEntity>

    @Delete
    fun deleteBookMark(bookMark:BookMarkEntity)

    @Update
    fun updateBookMark(bookMark:BookMarkEntity)

    @Insert
    fun insertBookMark(vararg bookMark:BookMarkEntity)
}