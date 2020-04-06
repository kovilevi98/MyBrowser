package hu.bme.onlab.mybrowser.cookies

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cookie")
data class Cookie_Entity(val name_t: String, val domain_t: String, val value_t: String) {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @ColumnInfo(name = "name")
    var name: String = ""

    @ColumnInfo(name = "domain")
    var domain: String = ""

    @ColumnInfo(name = "value")
    var value: String = ""


    init {
        name = name_t
        domain = domain_t
        value = value_t
    }
}