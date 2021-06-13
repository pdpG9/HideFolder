package com.example.hidefolder.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hidefolder.R
import com.example.hidefolder.adapters.OnAdapterDelete
import com.example.hidefolder.adapters.OnAdapterItemEdit
import com.example.hidefolder.adapters.ToDoAdapter
import com.example.hidefolder.dialog.ShowItemDialog
import com.example.hidefolder.models.ToDoModel
import com.example.hidefolder.ui.login.show
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class MainActivity : AppCompatActivity(), OnAdapterDelete, OnAdapterItemEdit {
    private lateinit var viewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTitle("TUIT ToDo")

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        rvListFolder.layoutManager = LinearLayoutManager(this)
        viewModel.listToDo.observe(this, Observer {
            rvListFolder.adapter = ToDoAdapter(it, this, this)
        })
        btAdd.setOnClickListener {
            viewModel.setOnClickAdd(this)
        }
        viewModel.error.observe(this, Observer {
            it.show(this)
        })
    }

    @Subscribe
    fun onClickItem(toDoModel: ToDoModel) {
        val dialog = ShowItemDialog(this, toDoModel)
        dialog.show()

    }


    override fun onDelete(position: Int) {
        viewModel.deleteItem(position)
    }

    override fun onEdite(position: Int) {

    }

    override fun onStop() {
        super.onStop()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }
}