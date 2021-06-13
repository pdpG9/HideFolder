package com.example.hidefolder.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.hidefolder.models.ToDoModel
import com.example.hidefolder.ui.login.log
import com.example.hidefolder.untils.INDEX
import com.example.hidefolder.untils.TODO_LIST
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.orhanobut.hawk.Hawk
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MainRepository(val succest: MutableLiveData<List<ToDoModel>>) {
    private val db = FirebaseDatabase.getInstance()
    private val root = db.getReference("ToDo")
    private val key = root.key
    private var index = Hawk.get<Int>(INDEX, 0)

    fun addLocalPref(toDoModel: ToDoModel) {
        val list1 = ArrayList<ToDoModel>()
        index = Hawk.get(INDEX, 0)
        index++
        list1.addAll(Hawk.get<List<ToDoModel>>(TODO_LIST, ArrayList<ToDoModel>()))
        toDoModel.id = index
        list1.add(toDoModel)
        "addLocalPref".log()
        list1.size.toString().log()
        clearLocal()
        Hawk.put(TODO_LIST, list1)
    }

    private fun loadGlobal() {
        "getGlobal".log()
        val list1 = ArrayList<ToDoModel>()
        db.getReference("ToDo").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val children = snapshot.children
                for (child in children) {
                    val value = child.getValue(ToDoModel::class.java)
                    if (value != null) {
                      val t =    getLocalPref()
                        if (t.filter { it.id == value.id }.isNotEmpty()) {
                            value.isLocal = true
//                            value.isGlobal = true
                        } else {
                            value.isGlobal = true
                            list1.add(value)
                        }
                    }
                }
                if (list1.size > 0) {

                    succest.value = list1
                    getLocalPref()///
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }


    fun addGlobal(toDoModel: ToDoModel) {
        val item = HashMap<String, Any>()
        index = Hawk.get(INDEX, 0)
        index++
        item.put("author", toDoModel.author!!)
        item.put("id", index)
        item.put("desc", toDoModel.desc!!)
        item.put("isLocal", toDoModel.isLocal)
        item.put("isGlobal", toDoModel.isGlobal)
        item.put("pin", toDoModel.pin!!)
        item.put("title", toDoModel.title!!)
        index.toString().log()
        root.child("$key - " + "$index").setValue(item)
        Hawk.put(INDEX, index)

    }

    fun lodaAllData() {
        loadGlobal()
            // getLocalPref()
    }

    private fun getLocalPref(): List<ToDoModel> {
        "getLocal".log()
        val list1 = ArrayList<ToDoModel>()
        list1.addAll(Hawk.get<List<ToDoModel>>(TODO_LIST, ArrayList<ToDoModel>()))
        list1.forEach { it.isLocal = true }
        if (succest.value != null) {
           list1.addAll(succest.value!!)
        }
        succest.value = emptyList()
        succest.value = list1
        list1.size.toString().log()
        return list1
    }

    fun clearLocal() {
        Hawk.delete(TODO_LIST)
    }

}