package hu.bme.onlab.mybrowser.history_room

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import hu.bme.onlab.mybrowser.R
import hu.bme.onlab.mybrowser.WebViewActivity
import hu.bme.onlab.mybrowser.bookmarks__room.h_b_Entity

@EpoxyModelClass(layout = R.layout.singlehistory)
abstract class SingleHistoryModel(val context: Context) :
    EpoxyModelWithHolder<SingleHistoryModel.Holder>() {

    @EpoxyAttribute
    lateinit var historyItem: h_b_Entity


    override fun bind(holder: Holder) {
        super.bind(Holder())
        val controller = SingleHistoryController.ticked_list

        holder.checked.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                with(historyItem) {
                    controller.ticked.add(this)
                }
            } else {
                with(historyItem) {
                    controller.ticked.remove(this)
                }
            }
        }
        holder.historydelete.setOnClickListener {
            var new_url: String

            with(historyItem) {
                new_url = this.url
            }
            val dbThread = Thread {
                val bookmarkdata =
                    HistoryDatabase.getInstance(context).historyDao().getSpecificGrades(new_url)
                bookmarkdata.forEach {
                    HistoryDatabase.getInstance(context).historyDao().deleteBookMark(it)
                }
            }
            dbThread.start()
        }
        holder.urlView.setOnClickListener() {
            var new_url: String
            with(historyItem) {
                new_url = this.url
            }
            val intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra("newUrl", new_url)
            context.startActivity(intent)
        }
        with(historyItem) {
            holder.urlView.text = title
            holder.time.text = time
            holder.checked.isChecked = false
        }
    }

    inner class Holder : EpoxyHolder() {
        lateinit var urlView: AppCompatButton
        lateinit var checked: AppCompatCheckBox
        lateinit var historydelete: AppCompatImageButton
        lateinit var time: AppCompatTextView

        override fun bindView(itemView: View) {
            urlView = itemView.findViewById(R.id.historyurl)
            checked = itemView.findViewById(R.id.historykchecked)
            historydelete = itemView.findViewById(R.id.histordelete)
            time = itemView.findViewById(R.id.historyTime)
        }
    }
}