package hu.bme.onlab.mybrowser.bookmarks__room

import android.content.Context
import android.content.Intent
import android.util.Log
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
            }
        }
        holder.bookmarkdelete.setOnClickListener{
            Log.e("ki lett", "valasztva")
            var new_url: String
            with(bookmark) {
                //Log.e("fd",this.url)
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
                //Log.e("fd",this.url)
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
//        lateinit var timeView: AppCompatTextView

        override fun bindView(itemView: View) {
            urlView = itemView.findViewById(R.id.bookmarkurl)
            checked = itemView.findViewById(R.id.bookmarkchecked)
            bookmarkdelete = itemView.findViewById(R.id.bookmarkdelete)
//            timeView = itemView.findViewById(R.id.time)
        }


    }

}