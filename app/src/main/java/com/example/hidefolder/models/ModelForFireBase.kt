package com.example.hidefolder.models

data class ModelForFireBase constructor(
    val title:String,
    val desc:String,
    val isLocal:String,
    val isGlobal:String,
    val pin:String,
    val author:String
)
class Massage {
    var title:String? = null
    var desc:String? = null
    var isLocal:String? = null
    var isGlobal:String? = null
    var pin:String? = null
    var author:String? = null

    constructor(title: String?,desc: String?, isLocal: String?,isGlobal: String?, pin: String?, author: String?) {
        this.title = title
        this.desc = desc
        this.isLocal = isLocal
        this.isGlobal = isGlobal
        this.pin = pin
        this.author = author

    }
    constructor()

}
