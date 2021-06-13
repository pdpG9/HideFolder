package com.example.hidefolder.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hidefolder.R
import com.example.hidefolder.models.ToDoModel
import com.example.hidefolder.ui.login.log
import com.example.hidefolder.untils.INDEX
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.item_todo.view.*
import org.greenrobot.eventbus.EventBus

class ToDoAdapter(
    val items: List<ToDoModel>,
    val delteListener: OnAdapterDelete,
    val editListener: OnAdapterItemEdit
) :
    RecyclerView.Adapter<ToDoAdapter.ItemHolder>() {

    inner class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name = itemView.findViewById<TextView>(R.id.tvTitile)
        val author = itemView.tvAuthorName
        val checkLocal = itemView.itemLocalCkeck
        val checkGlobal = itemView.itemGlobalCkeck

        fun bind(position: Int) {
            itemView.setOnClickListener {
                EventBus.getDefault().post(items[position])
            }
            itemView.btMore.setOnClickListener {
                val popup = PopupMenu(this.itemView.context, itemView.btMore)
                popup.menuInflater.inflate(R.menu.popup_menu, popup.menu)
                popup.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.btDeleteMenu -> {
                            "delete".log()
                            delteListener.onDelete(position)
                        }
                        R.id.btEditeMenu -> {
                            "edite".log()
                            editListener.onEdite(position)
                        }
                        else -> {
                        }
                    }
                    return@setOnMenuItemClickListener true
                }
                popup.show()
            }
            val index = Hawk.get<Int>(INDEX,0)
            if (index< items[position].id!!){
                Hawk.put(INDEX, items[position].id?.plus(1))
            }
            val item = items[position]
            name.text = item.title
            author.text = item.author
            if (item.isLocal == null) {
                checkLocal.isChecked = false
            } else {
                checkLocal.isChecked = item.isLocal!!
            }
            if (item.isLocal == null) {
                checkGlobal.isChecked = false
            } else {
                checkGlobal.isChecked = item.isGlobal!!
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return items.count()
    }
}

interface OnAdapterItemEdit {
    fun onEdite(position: Int)
}

interface OnAdapterDelete {
    fun onDelete(position: Int)
}