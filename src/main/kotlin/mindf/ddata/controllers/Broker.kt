package mindf.ddata.controllers

import org.json.JSONObject
import mindf.ddata.models.DData
import mindf.utils.Tools.Companion.readInstanceProperty
import java.sql.ResultSet
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.jvm.javaField

abstract class Broker {

    abstract val controller: Facade

    fun fetchAll() {
        object : Query() {
            override fun init() {
                modelClass = controller.modelClass
            }
            override fun onEnd(jsonObjects: MutableList<JSONObject>) {
                controller.initList(jsonObjects)
            }
        }.selectAll()
    }

    fun add(model: Any, function: (key: Any) -> Unit) {
        object : Query(){
            override fun init() {
                this.model = model
            }
            override fun onEnd(resultSet: ResultSet) {
                function(resultSet)
            }
        }.insert()
    }

    fun add(model: Any) {
        object : Query(){
            override fun init() {
                this.model = model
            }
        }.insert()
    }

    fun delete(key: String, value: Any) {
        object : Query(){
            override fun init() {
                modelClass = controller.modelClass
                where = "`$key` = '$value'"
            }
        }.delete()
    }

    fun update(model: Any, vararg properties: KMutableProperty1<*, *>, condition: String) {
        object : Query() {
            override fun init() {
                this.model = model
                this.properties = properties.toList()
                this.where = condition
            }
        }.update()
    }

    fun update(model: Any, properties: List<KMutableProperty1<*, *>>, condition: String) {
        object : Query() {
            override fun init() {
                this.model = model
                this.properties = properties
                this.where = condition
            }
        }.update()
    }

    fun update(model: Any, properties: List<KMutableProperty1<*, *>>, property1: KMutableProperty1<*, *>, value: Any) {
        object : Query() {
            override fun init() {
                this.model = model
                this.properties = properties
                where = "`" + findAnnotation(property1)?.name + "`" + " = '" + value + "'"
            }
        }.update()
    }

    fun update(model: Any, properties: List<KMutableProperty1<*, *>>, property1: KMutableProperty1<*, *>) {
        object : Query() {
            override fun init() {
                this.model = model
                this.properties = properties
                where = if (findAnnotation(property1)?.name?.contains("[unassigned")!!) {
                    "`" + property1.name + "`" + " = '" + readInstanceProperty(model, property1.name) + "'"
                } else {
                    "`" + findAnnotation(property1)?.name + "`" + " = '" + readInstanceProperty(model, property1.name) + "'"
                }
            }
        }.update()
    }

    private fun findAnnotation(kProperty1: KMutableProperty1<*, *>): DData? {
        val annotations = kProperty1.javaField!!.annotations
        for (annotation in annotations) {
            when (annotation) {
                is DData -> {
                    return annotation
                }
            }
        }
        return null
    }
}