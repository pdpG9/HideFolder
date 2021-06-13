package com.example.hidefolder.models

class ToDoModel{
    var id:Int? = 0
    var title:String? = null
    var desc:String? = null
    var isLocal:Boolean = false
    var isGlobal:Boolean = false
    var pin:String? = null
    var author:String? = null

    constructor(
        id: Int?,
        title: String?,
        desc: String?,
        isLocal: Boolean,
        isGlobal: Boolean,
        pin: String?,
        author: String?
    ) {
        this.id = id
        this.title = title
        this.desc = desc
        this.isLocal = isLocal
        this.isGlobal = isGlobal
        this.pin = pin
        this.author = author
    }

    constructor()

}

