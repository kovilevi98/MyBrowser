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
                    MyDatabase.getInstanceHistory(context).historyDao().getSpecificGrades(new_url)
                bookmarkdata.forEach {
                    MyDatabase.getInstanceHistory(context).historyDao().deleteHistory(it)
                }
            }
            dbThread.start()
        }
        holder.urlView.setOnClickListener() {
            var new_url: String
            with(historyItem) {
                new_url = this.url
            }
            (activity).finished(new_url)
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