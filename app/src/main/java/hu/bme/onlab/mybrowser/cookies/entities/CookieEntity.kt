package hu.bme.onlab.mybrowser.cookies.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cookie")
data class CookieEntity(val domain_t: String) {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @ColumnInfo(name = "domain")
    var domain: String = ""

    init {
        domain = domain_t
    }
}