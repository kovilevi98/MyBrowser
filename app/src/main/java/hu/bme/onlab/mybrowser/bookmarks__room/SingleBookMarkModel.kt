package hu.bme.onlab.mybrowser.bookmarks__room

import android.util.Log
import android.view.View
import android.widget.CompoundButton
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import com.airbnb.epoxy.*
import hu.bme.onlab.mybrowser.R


@EpoxyModelClass(layout = R.layout.singlebookmark)
abstract class SingleBookMarkModel : EpoxyModelWithHolder<SingleBookMarkModel.Holder>() {

    @EpoxyAttribute
    lateinit var bookmark: BookMarkEntity

    @EpoxyAttribute
    var onCheckedAction:Boolean=false



    override fun bind(holder: SingleBookMarkModel.Holder) {
        super.bind(Holder())
        //if(onCheckedAction.isChecked)
        holder.checked.setOnCheckedChangeListener {
                buttonView, isChecked ->
            onCheckedAction=isChecked
            with(bookmark){
                this.isChecked=onCheckedAction
                if(this.isChecked)
                    Log.e("ki lett","valasztva")
            }
        }
        holder.bookmarkdelete.setOnClickListener{
            //Todo deletee this item from the model
        }
        with(bookmark) {
            holder.urlView.text = title
        }
    }

    inner class Holder : EpoxyHolder() {
        lateinit var urlView: AppCompatTextView
        lateinit var checked: AppCompatCheckBox
        lateinit var bookmarkdelete: AppCompatImageButton
//        lateinit var timeView: AppCompatTextView

        override fun bindView(itemView: View) {
            urlView = itemView.findViewById(R.id.bookmarkurl)
            checked = itemView.findViewById(R.id.bookmarkchecked)
            bookmarkdelete = itemView.findViewById(R.id.bookmarkdelete)
//            timeView = itemView.findViewById(R.id.time)
        }


    }

}