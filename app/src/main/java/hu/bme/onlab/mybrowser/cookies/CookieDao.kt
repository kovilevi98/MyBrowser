package hu.bme.onlab.mybrowser.cookies

import androidx.lifecycle.LiveData
import androidx.room.*
import hu.bme.onlab.mybrowser.cookies.entities.CookieEntity

@Dao
interface CookieDao {
    @Query("Select * from cookie")
    fun getCookieList(): LiveData<List<CookieEntity>>

    @Query("Select * from cookie")
    fun getCookie(): List<CookieEntity>

    @Query("SELECT * FROM cookie WHERE domain = :name")
    fun getSpecificGrades(name: String): List<CookieEntity>

    @Query("SELECT * FROM cookie WHERE id = :name")
    fun getSpecificGradesbyID(name: Int): List<CookieEntity>

    @Query("SELECT * FROM cookie WHERE domain = :name")
    fun getFromDomain(name: String): List<CookieEntity>

    @Delete
    fun deleteCookie(c: CookieEntity)

    @Update
    fun updateCookie(c: CookieEntity)

    @Insert
    fun insertCookie(vararg c: CookieEntity)

}