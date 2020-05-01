package hu.bme.onlab.mybrowser.cookies

import android.content.Context
import com.airbnb.epoxy.AsyncEpoxyController


class SingleCookieController(val context: Context, val activity: CookieActivity) :
    AsyncEpoxyController() {

    var cookieItems: MutableList<CookieFields> = mutableListOf()
        set(value) {
            field = value
            requestModelBuild()
        }

    init {
        val list = CookieDatabase.getInstance(context).cookiedao().getCookie()
        var result: MutableList<CookieFields> = mutableListOf()
        list?.forEach {
            val cookieStr = android.webkit.CookieManager.getInstance()
                .getCookie(it.domain)
            //Log.e("az url", it)
            if (cookieStr != null) {
                var temp = CookieFields(it.domain, cookieStr)
                result.add(temp)
            }

        }
        cookieItems = result
    }

    fun refresh() {
        val list = CookieDatabase.getInstance(context).cookiedao().getCookie()
        var result: MutableList<CookieFields> = mutableListOf()
        list?.forEach {
            val cookieStr = android.webkit.CookieManager.getInstance()
                .getCookie(it.domain)
            //Log.e("az url", it)
            if (cookieStr != null) {
                var temp = CookieFields(it.domain, cookieStr)
                result.add(temp)
            }

        }
        cookieItems = result
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
        var ticked: MutableList<CookieFields> = ArrayList()
    }
}