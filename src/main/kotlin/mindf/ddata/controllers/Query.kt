package mindf.ddata.controllers

import org.json.JSONObject
import mindf.ddata.models.TableModel
import java.sql.ResultSet
import kotlin.reflect.KMutableProperty1

abstract class Query {

    private var database = Database()
    internal lateinit var tableModel: TableModel
    internal lateinit var modelClass: Class<out Any>
    internal lateinit var model: Any
    internal lateinit var properties: List<KMutableProperty1<*, *>>
    internal var string = ""
    internal var where = "1"

    abstract fun init()
    internal open fun onEnd(jsonObjects: MutableList<JSONObject>) {}

    internal open fun onEnd(resultSet: ResultSet) {}

    internal fun selectAll() {
        init()
        tableModel = QueryData.getTableFromModelClass(this.modelClass)
        string = ("SELECT * FROM " + this.modelClass.simpleName + " WHERE " + where)
        database.executeSelect(this)
    }

    internal fun insert() {
        init()
        tableModel = QueryData.getTableWithDataFromModel(this.model)
        string = "INSERT INTO " + tableModel.name + " (" + tableModel.columns.joinToString(",") + ") VALUES (" +
                tableModel.values.joinToString(",") + ")"
        database.executeInsert(this)
    }

    internal fun delete() {
        init()
        string = "DELETE FROM " + modelClass.simpleName + " WHERE " + where
        database.executeUpdate(string)
    }

    fun update() {
        init()
        tableModel = QueryData.getTableWithProperties(this.model, this.properties)!!
        string = "UPDATE " + model.javaClass.simpleName + " SET " + getUpdateSet(tableModel.columns, tableModel.values) + " WHERE " + where
        database.executeUpdate(string)
    }

    private fun getUpdateSet(columns: MutableList<String>, values: MutableList<String>): String {
        var result = ""
        for (i in 0 until columns.size) {
            result += columns[i] + " = " + values[i]
        }
        return result.substring(0, result.length)
    }
}