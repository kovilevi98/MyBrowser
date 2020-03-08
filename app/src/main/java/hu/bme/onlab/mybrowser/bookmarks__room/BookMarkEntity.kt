package hu.bme.onlab.mybrowser.bookmarks__room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmark")
data class BookMarkEntity (val url_t:String, val time_t:String) {

    @PrimaryKey(autoGenerate = true)
    public var id:Int =0


    @ColumnInfo(name = "url")
    public var url:String=""

    @ColumnInfo(name = "time")
    var time:String=""
        get() = field

    init {
        url=url_t
        time=time_t
    }


}