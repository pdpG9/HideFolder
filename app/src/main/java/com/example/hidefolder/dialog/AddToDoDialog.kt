package com.example.hidefolder.dialog

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.example.hidefolder.R
import com.example.hidefolder.models.ToDoModel
import com.example.hidefolder.untils.USER
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.add_dialog.view.*

class AddToDoDialog(context: Context, listener: SetOnAddClickListener) {
    private val view = LayoutInflater.from(context).inflate(R.layout.add_dialog, null, false)
    private val dialog = AlertDialog.Builder(context).setView(view).create()

    init {
        view.apply {
            var havePIN = false
            checkPin.setOnCheckedChangeListener { buttonView, isChecked ->
                havePIN = isChecked
                if (isChecked) {
                    inputPassword.visibility = View.VISIBLE
                } else {
                    inputPassword.visibility = View.INVISIBLE
                }
            }
            btAddInfo.setOnClickListener {
                val title = inputTitle.text.toString()
                val des = inputDescription.text.toString()
                val isLocal = checkLocal.isChecked
                val isGlobal = checkGlobal.isChecked
                var password = ""
                if (havePIN){
                   password =  inputPassword.text.toString()
                }
                if (havePIN&&!password.isEmpty()){
                listener.onAdd(ToDoModel( 0,title, des,isLocal,isGlobal,password,"USER"))
                dissmiss()
                }
                    if (havePIN&&password.isEmpty()){
                        inputPassword.error = "Password is empty!"
                    }
                if (!havePIN){
                    listener.onAdd(ToDoModel( 0,title, des,isLocal,isGlobal,password,"USER"))

                    dissmiss()
                }

            }
            btCancel.setOnClickListener {
                dissmiss()
            }
        }

    }

    fun show() {
        dialog.show()
    }

    fun dissmiss() {
        dialog.dismiss()
    }
}

interface SetOnAddClickListener {
    fun onAdd(todo: ToDoModel)
}