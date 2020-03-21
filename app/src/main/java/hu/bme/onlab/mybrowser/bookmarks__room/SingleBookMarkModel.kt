package hu.bme.onlab.mybrowser.bookmarks__room

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatImageButton
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import hu.bme.onlab.mybrowser.R
import hu.bme.onlab.mybrowser.WebViewActivity


@EpoxyModelClass(layout = R.layout.singlebookmark)
abstract class SingleBookMarkModel(val context: Context) :
    EpoxyModelWithHolder<SingleBookMarkModel.Holder>() {

    @EpoxyAttribute
    lateinit var bookmark: BookMarkEntity

    override fun bind(holder: SingleBookMarkModel.Holder) {
        super.bind(Holder())
        val controller = SingleBookMakrController.ticked_list

        holder.checked.setOnCheckedChangeListener { buttonView, isChecked ->
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
                var bookmarkdata =
                    BookMarkDatabase.getInstance(context).bookMarkDao().getSpecificGrades(new_url)
                bookmarkdata.forEach {
                    BookMarkDatabase.getInstance(context).bookMarkDao().deleteBookMark(it)
                }
            }
            dbThread.start()
        }
        holder.urlView.setOnClickListener() {
            var new_url: String
            with(bookmark) {
                new_url = this.url
            }
            val intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra("newUrl", new_url)
            context.startActivity(intent)
        }
        with(bookmark) {
            holder.urlView.text = title
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