package hu.bme.onlab.mybrowser.cookies

import android.content.Context
import com.airbnb.epoxy.AsyncEpoxyController


class SingleCookieController(val context: Context, val activity: CookieActivity) :
    AsyncEpoxyController() {

    var cookieItems: MutableList<Cookie_Entity> = mutableListOf()
        set(value) {
            field = value
            requestModelBuild()
        }

    private var db: CookieDatabase

    init {
        db = CookieDatabase.getInstance(context)
        cookieItems = db.cookiedao().getCookie().toMutableList()
    }

    override fun buildModels() {
        var i: Long = 0
        cookieItems.forEach {
            singleCookie(context, activity) {
                id(i++)
                cookie(it)
            }
        }
    }

    object ticked_list {
        var ticked: MutableList<Cookie_Entity> = ArrayList()
    }
}