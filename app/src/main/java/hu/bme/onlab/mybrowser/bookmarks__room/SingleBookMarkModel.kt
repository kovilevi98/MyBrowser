package hu.bme.onlab.mybrowser.bookmarks__room

import android.content.Context
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatImageButton
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import hu.bme.onlab.mybrowser.MyDatabase
import hu.bme.onlab.mybrowser.R


@EpoxyModelClass(layout = R.layout.singlebookmark)
abstract class SingleBookMarkModel(val context: Context, val activity: BookMarkActivity) :
    EpoxyModelWithHolder<SingleBookMarkModel.Holder>() {

    @EpoxyAttribute
    lateinit var bookmark: EntityBookMark

    override fun bind(holder: Holder) {
        super.bind(Holder())
        val controller = SingleBookMakrController.TickedList
        holder.checked.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                with(bookmark) {
                    controller.ticked.add(this)
                }
            } else {
                with(bookmark) {
                    controller.ticked.remove(this)
                }
            }
        }
        holder.bookmarkdelete.setOnClickListener {
            var new_url: String
            with(bookmark) {
                new_url = this.url
            }
            val dbThread = Thread {
                val bookmarkdata =
                    MyDatabase.getInstance(context).bookMarkDao().getSpecificGrades(new_url)
                bookmarkdata.forEach {
                    MyDatabase.getInstance(context).bookMarkDao().deleteBookMark(it)
                }
            }
            dbThread.start()
        }
        holder.urlView.setOnClickListener() {
            var new_url: String
            with(bookmark) {
                new_url = this.url
            }
            (activity).finished(new_url)
        }
        with(bookmark) {
            holder.urlView.text = title
            holder.checked.isChecked = false
        }
    }

    inner class Holder : EpoxyHolder() {
        lateinit var urlView: AppCompatButton
        lateinit var checked: AppCompatCheckBox
        lateinit var bookmarkdelete: AppCompatImageButton

        override fun bindView(itemView: View) {
            urlView = itemView.findViewById(R.id.bookmarkurl)
            checked = itemView.findViewById(R.id.bookmarkchecked)
            bookmarkdelete = itemView.findViewById(R.id.bookmarkdelete)
        }
    }
}