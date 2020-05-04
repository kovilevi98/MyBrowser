package hu.bme.onlab.mybrowser.history_room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DaoHistory {

    @Query("Select * from history")
    fun getHistoryList(): LiveData<List<HistoryEntity>>

    @Query("Select * from history")
    fun getHistory(): List<HistoryEntity>

    @Query("SELECT * FROM history WHERE url = :name")
    fun getSpecificGrades(name: String): List<HistoryEntity>

    @Query("SELECT * FROM history WHERE title = :name")
    fun getFromTitle(name: String): List<HistoryEntity>

    @Delete
    fun deleteHistory(hb: HistoryEntity)

    @Update
    fun updateHistory(hb: HistoryEntity)

    @Insert
    fun insertHistory(vararg hb: HistoryEntity)
}