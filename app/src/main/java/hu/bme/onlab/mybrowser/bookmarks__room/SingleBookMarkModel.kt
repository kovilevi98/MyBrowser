package hu.bme.onlab.mybrowser.bookmarks__room

import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import hu.bme.onlab.mybrowser.R
import kotlinx.android.synthetic.main.singlebookmark.view.*

@EpoxyModelClass(layout = R.layout.singlebookmark)
abstract class SingleBookMarkModel : EpoxyModelWithHolder<SingleBookMarkModel.Holder>(){

        @EpoxyAttribute
        lateinit var bookmark:BookMarkEntity


    override fun bind(holder: SingleBookMarkModel.Holder) {
        super.bind(Holder())
        with(bookmark){
            holder.urlView.text = url
           // holder.timeView.text = time
        }
    }

    inner class Holder : EpoxyHolder() {
        lateinit var urlView: AppCompatTextView
//        lateinit var timeView: AppCompatTextView

        override fun bindView(itemView: View) {
            urlView = itemView.findViewById(R.id.bookmarkurl)
//            timeView = itemView.findViewById(R.id.time)
        }

    }

}