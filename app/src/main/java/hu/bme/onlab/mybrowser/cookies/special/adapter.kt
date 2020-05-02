package hu.bme.onlab.mybrowser.cookies.special

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hu.bme.onlab.mybrowser.R
import hu.bme.onlab.mybrowser.cookies.CookieFields
import kotlinx.android.synthetic.main.row_item.view.*
import kotlinx.android.synthetic.main.singlecookie.view.CookieValue


class adapter(cooki: CookieFields) : RecyclerView.Adapter<adapter.ViewHolder>() {

    private val list = mutableListOf<specialEntity>()
    private val wholeCookie = cooki


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cookie = list[position]

        holder.cookie = cookie

        holder.tvName.text = cookie.name
        holder.tvValue.text = cookie.value
    }


    fun addItem(cookie: specialEntity) {
        val size = list.size
        list.add(cookie)
        notifyItemInserted(size)
    }

    fun addAll(cookies: List<specialEntity>) {
        val size = list.size
        list += cookies
        notifyItemRangeInserted(size, list.size)
    }

    fun deleteRow(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount() = list.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.CookieNAme
        val tvValue: TextView = itemView.CookieValue
        val ibDeleteCookie: ImageButton = itemView.specialCookieDelete

        init {
            ibDeleteCookie.setOnClickListener {
                //itt lehet majd törölni
                //list[adapterPosition]
                //deleteRow(adapterPosition)
                deleteSpecial(list[adapterPosition])
            }
        }

        var cookie: specialEntity? = null
        fun deleteSpecial(cookie: specialEntity) {
            var newList: MutableList<specialEntity> = mutableListOf()
            deleteRow(position)
            val list = wholeCookie.wholeCookie_t.split(";")
            list.forEach {
                val value = it.split("=")
                val tmp = specialEntity(value[0], value[1])
                if (value[0] != cookie.name)
                    newList.add(tmp)
            }
            var newCookieString: String = ""
            newList.forEach {
                newCookieString += it.name + "=" + it.value + ";"
            }
            CookieManager.getInstance().setCookie(wholeCookie.domain_t, newCookieString)
        }
    }
}