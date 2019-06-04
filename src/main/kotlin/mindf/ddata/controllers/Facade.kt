package mindf.ddata.controllers

import org.json.JSONObject

abstract class Facade {

    abstract val modelClass: Class<*>
    abstract fun initList(jsonObjects: MutableList<JSONObject>)
}