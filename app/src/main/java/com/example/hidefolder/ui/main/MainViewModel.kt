package com.example.hidefolder.ui.main

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hidefolder.dialog.AddToDoDialog
import com.example.hidefolder.dialog.SetOnAddClickListener
import com.example.hidefolder.encryption.CiperHelper
import com.example.hidefolder.models.ToDoModel
import com.example.hidefolder.ui.login.log
import com.example.hidefolder.untils.USER
import com.orhanobut.hawk.Hawk

class MainViewModel : ViewModel() {
    val listToDo = MutableLiveData<List<ToDoModel>>()
    private val repository = MainRepository(listToDo)
    val error = MutableLiveData<String>()
    private var auhthor = Hawk.get<String>(USER)

    init {
        "init: loadtAllData ViewModel".log()
        auhthor.log()
        repository.lodaAllData()
    }

    fun setOnClickAdd(context: Context) {
        val temp = ArrayList<ToDoModel>()
        temp.addAll(listToDo.value ?: emptyList())
        AddToDoDialog(context, object : SetOnAddClickListener {
            override fun onAdd(todo: ToDoModel) {
                //шифрование
              //  todo.desc = CiperHelper.encrByAsimmetric(todo.desc.toString())
                todo.author = auhthor
                temp.add(todo)
                if (todo.isLocal) {
                    repository.addLocalPref(todo)
                }
                if (todo.isGlobal) {
                    repository.addGlobal(todo)
                }
                listToDo.value = emptyList()
                "siz after empty listToDo".log()
                listToDo.value!!.size.toString().log()
                listToDo.value = temp
            }

        }).show()
        "Author?".log()
        auhthor?.log()
    }

    fun deleteItem(id: Int) {


    }

}