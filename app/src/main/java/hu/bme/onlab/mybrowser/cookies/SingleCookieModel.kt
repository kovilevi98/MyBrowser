package hu.bme.onlab.mybrowser.cookies

import android.content.Context
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
    lateinit var cookie: Cookie_Entity

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
            var new_id: Int
            with(cookie) {
                new_id = this.id
            }

            val dbThread = Thread {
                val cookieData =
                    CookieDatabase.getInstance(context).cookiedao().getSpecificGradesbyID(new_id)
                cookieData.forEach {
                    CookieDatabase.getInstance(context).cookiedao().deleteCookie(it)
                }
            }
            dbThread.start()
        }

        with(cookie) {
            holder.name.text = this.name
            holder.value.text = this.value
            holder.domain.text = this.domain
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