package mindf.ddata.controllers

import mindf.ddata.models.DData
import mindf.ddata.models.Row
import mindf.ddata.models.TableModel
import mindf.utils.Tools.Companion.readInstanceProperty
import java.util.*
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.jvm.javaField

class QueryData {
    companion object {

        fun getTableFromModelClass(modelClass: Class<out Any>) : TableModel {
            val columns = ArrayList<Row>()
            val declaredFields = modelClass.newInstance().javaClass.declaredFields
            for (field in declaredFields) {
                for (annotation in field.annotations) {
                    when(annotation) {
                        is DData -> {
                            if (annotation.name.contains("[unassigned]")) {
                                columns.add(Row(field.type, field.name))
                            } else {
                                columns.add(Row(field.type, annotation.name))
                                //todo handle type
                            }
                        }
                    }
                }
             }
            return TableModel(modelClass.simpleName, columns)
        }

        fun getTableWithDataFromModel(model: Any) : TableModel {
            val columns: MutableList<String> = arrayListOf()
            val values: MutableList<String> = arrayListOf()
            val declaredFields = model::class.java.newInstance().javaClass.declaredFields
            for (field in declaredFields) {
                for (annotation in field.annotations) {
                    when(annotation) {
                        is DData -> {
                            var value = readInstanceProperty(model, field.name)
                            if (value != null) {
                                value = value.toString()
                                if (value.contains("'")) {
                                    value = value.replace("'", "''")
                                }
                                values.add("'$value'")
                                if (annotation.name.contains("[unassigned]")) {
                                    columns.add("`" + field.name + "`")
                                } else {
                                    columns.add("`" + annotation.name + "`")
                                }
                            }
                        }
                    }
                }
            }
            return TableModel(model.javaClass.simpleName, columns, values)
        }

        fun getTableWithProperties(model: Any, properties: List<KMutableProperty1<*, *>>): TableModel? {
            val columns: MutableList<String> = mutableListOf()
            val values: MutableList<String> = arrayListOf()
            properties.forEach { property1 ->
                var value = readInstanceProperty(model, property1.name).toString()
                if (value.contains("'")) {
                    value.replace("'", "''")
                }
                values.add("'$value'")
                for (annotation in property1.javaField!!.annotations) {
                    when (annotation) {
                        is DData -> {
                            if (annotation.name.contains("[unassigned]")) {
                                columns.add("`" + property1.name + "`")
                            } else {
                                columns.add("`" + annotation.name + "`")
                            }
                        }
                    }
                }
            }
            return TableModel(model.javaClass.simpleName, columns, values)
        }
    }
}
