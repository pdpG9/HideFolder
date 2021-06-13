package com.example.hidefolder.dialog

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.example.hidefolder.R
import com.example.hidefolder.encryption.CiperHelper
import com.example.hidefolder.models.ToDoModel
import com.example.hidefolder.ui.login.log
import com.example.hidefolder.ui.login.show
import kotlinx.android.synthetic.main.add_dialog.view.*
import kotlinx.android.synthetic.main.item_mode_layout.view.*

class ShowItemDialog(context: Context,toDo: ToDoModel) {
    private val view = LayoutInflater.from(context).inflate(R.layout.item_mode_layout,null,false)
    private val dialog = AlertDialog.Builder(context).setView(view).create()

    init {
        view.apply {
            if (toDo.pin.isNullOrEmpty()){
                cardViewDialog.visibility = View.VISIBLE
                cardPassDialog.visibility = View.GONE
            }else{
                cardViewDialog.visibility = View.GONE
                cardPassDialog.visibility = View.VISIBLE
            }
            val isL = toDo.isLocal
            val isG = toDo.isGlobal
            if (isL){
                tvlocalDialog.text = "local"
            }else{
                tvlocalDialog.text = ""
            }
            if (isG){
                tvGlobalDialog.text = " global"
            }else{
                tvGlobalDialog.text = ""
            }
            tvTitleDialog.text = toDo.title
            tvAuthorDialog.text = toDo.author
            "shifr matn".log()
            toDo.desc?.log()
           ///дешифрование
            tvDescriptionDialog.text = toDo.desc.toString()

            btCancelDialog.setOnClickListener {
                dialog.dismiss()
            }
            btOkDialog.setOnClickListener {
                val pss = inputPassDialog.text.toString()
                if (pss.isNullOrEmpty()){
                    inputPassDialog.error = "password is empty!"
                }
                if (pss.equals(toDo.pin)){
                    "Successful".show(context)
                    cardViewDialog.visibility = View.VISIBLE
                    cardPassDialog.visibility = View.GONE
                }else{
                    inputPassDialog.error = "password is incorrect"
                    inputPassDialog.setText("")
                }
            }

        }
    }
    fun show(){
        dialog.show()
    }

}