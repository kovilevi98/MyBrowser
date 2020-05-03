package hu.bme.onlab.mybrowser.cookies.special

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import hu.bme.onlab.mybrowser.R
import hu.bme.onlab.mybrowser.cookies.CookieFields
import kotlinx.android.synthetic.main.advancecookielayout.*
import kotlinx.android.synthetic.main.list.recyc

class MyDialog(ctx: Context, cookiet: CookieFields) : Dialog(ctx) {
    private var cookie = cookiet
    private val specialCookieList: MutableList<SpecialEntity> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.setCancelable(false)
        this.setContentView(R.layout.advancecookielayout)
        this.setTitle("SpecialCookies")
        val cookieList = cookie.wholeCookie_t.split(";")

        cookieList.forEach {
            val tmp = it.split("=")
            if (tmp.size == 2) {
                // Log.e("name:",tmp.get(0))
                //Log.e("value",tmp.get(1))
                specialCookieList.add(SpecialEntity(tmp[0], tmp[1]))
            }

        }
        okButton.setOnClickListener {
            this.dismiss()
        }


        val adapter = Adapter(cookie)
        adapter.addAll(specialCookieList)
        recyc.adapter = adapter
        super.onCreate(savedInstanceState)
    }

}