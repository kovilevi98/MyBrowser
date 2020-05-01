package hu.bme.onlab.mybrowser.cookies

import android.content.Context
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import hu.bme.onlab.mybrowser.R


@EpoxyModelClass(layout = R.layout.singlecookie)
abstract class SingleCookieModel(val context: Context, val activity: CookieActivity) :
    EpoxyModelWithHolder<SingleCookieModel.Holder>() {

    @EpoxyAttribute
    lateinit var cookie: CookieFields

    override fun bind(holder: Holder) {
        super.bind(Holder())
        val controller = SingleCookieController.ticked_list
        holder.checked.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                with(cookie) {
                    controller.ticked.add(this)
                }
            } else {
                with(cookie) {
                    controller.ticked.remove(this)
                }
            }
        }
        holder.Cookiekdelete.setOnClickListener {
            Log.e("delete", "delete")
            var cookieData: List<Cookie_Entity>
            val dbThread = Thread {
                cookieData =
                    CookieDatabase.getInstance(context).cookiedao()
                        .getSpecificGrades(cookie.domain_t)
                cookieData.forEach {
                    //Log.e("frfesfe",it.domain.toString())
                    CookieDatabase.getInstance(context).cookiedao().deleteCookie(it)
                }
            }
            dbThread.start()
            /*cookieData.forEach {
                Log.e("frfesfe",it.domain.toString())
                CookieDatabase.getInstance(context).cookiedao().deleteCookie(it)
            }*/
        }
        holder.name.setOnClickListener() {
            //
        }


        with(cookie) {

            holder.name.text = this.domain_t
            holder.value.text = ""
            holder.domain.text = this.wholeCookie_t
            holder.checked.isChecked = false
        }
    }

    inner class Holder : EpoxyHolder() {
        lateinit var name: AppCompatTextView
        lateinit var value: AppCompatTextView
        lateinit var domain: AppCompatTextView
        lateinit var checked: AppCompatCheckBox
        lateinit var Cookiekdelete: AppCompatImageButton

        override fun bindView(itemView: View) {
            name = itemView.findViewById(R.id.CookieName)
            checked = itemView.findViewById(R.id.cookieChecked)
            value = itemView.findViewById(R.id.CookieValue)
            domain = itemView.findViewById(R.id.CookieDomain)
            Cookiekdelete = itemView.findViewById(R.id.CookieDelete)
        }
    }
}