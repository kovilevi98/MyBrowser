package hu.bme.onlab.mybrowser.history_room

import android.content.Context
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import hu.bme.onlab.mybrowser.MyDatabase
import hu.bme.onlab.mybrowser.R

@EpoxyModelClass(layout = R.layout.singlehistory)
abstract class SingleHistoryModel(val context: Context, val activity: HistoryActivity) :
    EpoxyModelWithHolder<SingleHistoryModel.Holder>() {

    @EpoxyAttribute
    lateinit var historyItem: HistoryEntity


    override fun bind(holder: Holder) {
        super.bind(Holder())
        val controller = SingleHistoryController.tickedList

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
        holder.historyDelete.setOnClickListener {
            var newUrl: String

            with(historyItem) {
                newUrl = this.url
            }
            val dbThread = Thread {
                val bookmarkData =
                    MyDatabase.getInstanceHistory(context).historyDao().getSpecificGrades(newUrl)
                bookmarkData.forEach {
                    MyDatabase.getInstanceHistory(context).historyDao().deleteHistory(it)
                }
            }
            dbThread.start()
        }
        holder.urlView.setOnClickListener {
            var newUrl: String
            with(historyItem) {
                newUrl = this.url
            }
            (activity).finished(newUrl)
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
        lateinit var historyDelete: AppCompatImageButton
        lateinit var time: AppCompatTextView

        override fun bindView(itemView: View) {
            urlView = itemView.findViewById(R.id.historyurl)
            checked = itemView.findViewById(R.id.historykchecked)
            historyDelete = itemView.findViewById(R.id.histordelete)
            time = itemView.findViewById(R.id.historyTime)
        }
    }
}