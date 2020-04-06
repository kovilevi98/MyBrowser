package hu.bme.onlab.mybrowser.cookies

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CookieDao {
    @Query("Select * from cookie")
    fun getCookieList(): LiveData<List<Cookie_Entity>>

    @Query("Select * from cookie")
    fun getCookie(): List<Cookie_Entity>

    @Query("SELECT * FROM cookie WHERE name = :name")
    fun getSpecificGrades(name: String): List<Cookie_Entity>

    @Query("SELECT * FROM cookie WHERE id = :name")
    fun getSpecificGradesbyID(name: Int): List<Cookie_Entity>

    @Query("SELECT * FROM cookie WHERE domain = :name")
    fun getFromDomain(name: String): List<Cookie_Entity>

    @Delete
    fun deleteCookie(c: Cookie_Entity)

    @Update
    fun updateCookie(c: Cookie_Entity)

    @Insert
    fun insertCookie(vararg c: Cookie_Entity)

}