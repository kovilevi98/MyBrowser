package hu.bme.onlab.mybrowser.bookmarks__room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmark")
data class EntityBookMark(val url_t: String, val title_t: String) {

    @PrimaryKey(autoGenerate = true)
    var id:Int =0

    @ColumnInfo(name = "url")
    var url:String=""

    @ColumnInfo(name = "title")
    var title:String=""

    init {
        url=url_t
        title=title_t
    }


}