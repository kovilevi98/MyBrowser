package hu.bme.onlab.mybrowser.cookies

import android.content.Context
import android.view.View
import android.webkit.CookieManager
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import hu.bme.onlab.mybrowser.MyDatabase
import hu.bme.onlab.mybrowser.R
import hu.bme.onlab.mybrowser.cookies.entities.CookieEntity
import hu.bme.onlab.mybrowser.cookies.entities.CookieFields
import hu.bme.onlab.mybrowser.cookies.special.MyDialog


@EpoxyModelClass(layout = R.layout.singlecookie)
abstract class SingleCookieModel(val context: Context, val activity: CookieActivity) :
    EpoxyModelWithHolder<SingleCookieModel.Holder>() {

    @EpoxyAttribute
    lateinit var cookie: CookieFields

    override fun bind(holder: Holder) {
        super.bind(Holder())
        val controller = SingleCookieController.tickedList
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
        holder.cookieDelete.setOnClickListener {
            var cookieData: List<CookieEntity>
            CookieManager.getInstance().setCookie(cookie.domain_t, "")
            val dbThread = Thread {
                cookieData =
                    MyDatabase.getInstanceCookie(context).cookiedao()
                        .getSpecificGrades(cookie.domain_t)
                cookieData.forEach {
                    MyDatabase.getInstanceCookie(context).cookiedao().deleteCookie(it)
                }
            }
            dbThread.start()

        }
        holder.name.setOnClickListener {
            buildDialog()
        }


        with(cookie) {

            holder.name.text = this.domain_t
            holder.value.text = ""
            holder.domain.text = this.wholeCookie_t
            holder.checked.isChecked = false
        }
    }

    private fun buildDialog() {
        val dialog = MyDialog(context, cookie)
        dialog.show()
    }

    inner class Holder : EpoxyHolder() {
        lateinit var name: AppCompatTextView
        lateinit var value: AppCompatTextView
        lateinit var domain: AppCompatTextView
        lateinit var checked: AppCompatCheckBox
        lateinit var cookieDelete: AppCompatImageButton

        override fun bindView(itemView: View) {
            name = itemView.findViewById(R.id.CookieName)
            checked = itemView.findViewById(R.id.cookieChecked)
            value = itemView.findViewById(R.id.CookieValue)
            domain = itemView.findViewById(R.id.CookieDomain)
            cookieDelete = itemView.findViewById(R.id.CookieDelete)
        }
    }
}