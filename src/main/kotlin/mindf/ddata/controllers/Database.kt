package mindf.ddata.controllers

import org.json.JSONObject
import mindf.ddata.models.Row
import mindf.utils.RunnableTask
import java.io.IOException
import java.sql.*
import java.util.*

class Database

internal constructor() {

    private val properties: Properties
    private var resultSet: ResultSet? = null

    companion object {
        const val INT = "java.lang.Integer"
        const val STRING = "java.lang.String"
        const val DATE = "java.lang.Date"
        const val DOUBLE = "java.lang.Double"
        const val FLOAT = "java.lang.Float"
        const val BOOLEAN = "java.lang.Boolean"
        const val CHAR = "java.lang.Char"
    }

    init {
        properties = loadDatabaseProperties()
        DataGenerator.build(this)
    }

    internal fun executeSelect(query: Query) {
        val runnableTask: RunnableTask
        var preparedStatement: PreparedStatement? = null
        var connection: Connection? = null
        var queryResult: MutableList<JSONObject>? = null
        val tableModel = query.tableModel
        try {
            runnableTask = object : RunnableTask(displayProgress = false) {
                override fun run() {
                    connection = createConnection()
                    preparedStatement = sqlQuery(connection, query.string)
                    try {
                        resultSet = preparedStatement?.executeQuery()
                        val rows = tableModel.rows
                        queryResult = getRowData(rows)
                    } catch (e: SQLException) {
                        e.printStackTrace()
                    } finally {
                        close(resultSet)
                    }
                }
                override fun progress() {
                    progressAfter(!queryResult.isNullOrEmpty(), "fetching " + tableModel.name, 100)
                }
                override fun end() {
                    query.onEnd(queryResult!!)
                }
            }
        } finally {
            close(preparedStatement)
            close(connection)
        }
        runnableTask.start(500)
    }

    internal fun executeInsert(query: Query) {
        val resultSet: ResultSet?
        var preparedStatement: PreparedStatement? = null
        var connection: Connection? = null
        try {
            connection = createConnection()
            preparedStatement = sqlQuery(connection, query.string, true)
            try {
                preparedStatement?.executeUpdate()
                resultSet = preparedStatement?.generatedKeys //todo check why it doesn't go in
                if(resultSet!!.next()){
                    query.onEnd(resultSet)
                }
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        } finally {
            close(preparedStatement)
            close(connection)
        }
    }

    internal fun executeUpdate(query: String) {
        var preparedStatement: PreparedStatement? = null
        var connection: Connection? = null
        try {
            connection = createConnection()
            preparedStatement = sqlQuery(connection, query)
            try {
                preparedStatement?.executeUpdate()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        } finally {
            close(preparedStatement)
            close(connection)
        }
    }

    private fun replaceInvalidCharacters(query: String) {

    }

    @Throws(SQLException::class)
    private fun getRowData(rows: MutableList<Row>): ArrayList<JSONObject> {
        val jsonObjects = arrayListOf<JSONObject>()
        var i = 0
        while (resultSet!!.next()) {
            val jsonObject = JSONObject()
            for (row in rows) { //todo refactor this for something better with full range type detection + better way to do this
                val type = row.type.name
                when { //todo setController with a date
                    type.equals(STRING, ignoreCase = true) -> jsonObject.put(row.name, resultSet!!.getString(row.name))
                    type.equals(INT, ignoreCase = true) -> jsonObject.put(row.name, resultSet!!.getInt(row.name))
                    type.equals(DOUBLE, ignoreCase = true) -> jsonObject.put(row.name, resultSet!!.getDouble(row.name))
                    type.equals(BOOLEAN, ignoreCase = true) -> jsonObject.put(row.name, resultSet!!.getBoolean(row.name))
                    type.equals(CHAR, ignoreCase = true) -> jsonObject.put(row.name, resultSet!!.getString(row.name))
                    type.equals(DATE, ignoreCase = true) -> jsonObject.put(row.name, resultSet!!.getBoolean(row.name))
                    type.equals(FLOAT, ignoreCase = true) -> jsonObject.put(row.name, resultSet!!.getFloat(row.name))
                }
            }
            jsonObjects.add(jsonObject)
            ++i
        }
        return jsonObjects
    }

    private fun loadDatabaseProperties(): Properties {
        val classLoader = Thread.currentThread().contextClassLoader
        val properties = Properties()
        try {
            classLoader.getResourceAsStream("database.properties")!!.use { resourceStream ->
                properties.load(
                    resourceStream
                )
            }
        } catch (e: IOException) {
            e.printStackTrace()
            println("database.properties not found!")
            System.exit(1)
        }

        return properties
    }

    private fun createConnection(): Connection? {
        var connection: Connection? = null
        try {
            connection = DriverManager.getConnection(
                properties.getProperty("db.connection.url"),
                properties.getProperty("db.user"),
                properties.getProperty("db.password")
            )
        } catch (e: SQLException) {
            e.printStackTrace()
        }

        return connection
    }

    private fun sqlQuery(connection: Connection?, query: String, withGeneratedKey: Boolean = false): PreparedStatement? {
        var preparedStatement: PreparedStatement? = null
        try {
            preparedStatement = if (withGeneratedKey) {
                connection!!.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
            } else {
                connection!!.prepareStatement(query)
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return preparedStatement
    }

    private fun close(closeable: AutoCloseable?) {
        try {
            closeable?.close()
        } catch (ignored: Exception) {
        }

    }

    private fun logProperties(properties: Properties) {
        println("Connection URL: " + properties.getProperty("db.connection.url"))
        println("User: " + properties.getProperty("db.user"))
        //System.out.println("Password: " + properties.getProperty("db.password"));
    }
}
