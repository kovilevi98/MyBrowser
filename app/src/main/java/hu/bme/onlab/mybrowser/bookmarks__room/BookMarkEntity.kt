package hu.bme.onlab.mybrowser.bookmarks__room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmark")
data class BookMarkEntity (val url_t:String, val time_t:String, val title_t:String) {

    @PrimaryKey(autoGenerate = true)
    var id:Int =0

    @ColumnInfo(name = "url")
    var url:String=""

    @ColumnInfo(name = "title")
    var title:String=""

    @ColumnInfo(name = "time")
    var time:String=""
        get() = field

    var isChecked = false



    init {
        url=url_t
        time=time_t
        title=title_t
    }


}