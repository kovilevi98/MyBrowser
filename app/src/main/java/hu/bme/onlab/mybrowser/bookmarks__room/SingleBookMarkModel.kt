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
        val controller = SingleBookMarkController.TickedList
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
        holder.bookmarkDelete.setOnClickListener {
            var newUrl: String
            with(bookmark) {
                newUrl = this.url
            }
            val dbThread = Thread {
                val bookmarkData =
                    MyDatabase.getInstance(context).bookMarkDao().getSpecificGrades(newUrl)
                bookmarkData.forEach {
                    MyDatabase.getInstance(context).bookMarkDao().deleteBookMark(it)
                }
            }
            dbThread.start()
        }
        holder.urlView.setOnClickListener {
            var newUrl: String
            with(bookmark) {
                newUrl = this.url
            }
            (activity).finished(newUrl)
        }
        with(bookmark) {
            holder.urlView.text = title
            holder.checked.isChecked = false
        }
    }

    inner class Holder : EpoxyHolder() {
        lateinit var urlView: AppCompatButton
        lateinit var checked: AppCompatCheckBox
        lateinit var bookmarkDelete: AppCompatImageButton

        override fun bindView(itemView: View) {
            urlView = itemView.findViewById(R.id.bookmarkurl)
            checked = itemView.findViewById(R.id.bookmarkchecked)
            bookmarkDelete = itemView.findViewById(R.id.bookmarkdelete)
        }
    }
}