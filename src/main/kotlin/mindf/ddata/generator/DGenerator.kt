package mindf.ddata.generator

import mindf.utils.Bat
import mindf.utils.Tools
import java.io.File

class DGenerator(databaseScript: File, directory: File, onEnd: (() -> Unit)?) {
    constructor(databaseScript: File, directory: File) : this(databaseScript, directory, null)

    private val INT = "INT"
    private val DOUBLE = "DOUBLE"
    private val DATE = "DATE"
    private val TIMESTAMP = "TIMESTAMP"
    private val DATETIME = "DATETIME"
    private val FLOAT = "FLOAT"
    private val BOOLEAN = "BOOLEAN"
    private val BOOL = "BOOL"
    private val int = "Int"
    private val double = "Double"
    private val string = "String"
    private val float = "Float"
    private val boolean = "Boolean"
    private val TAB = "    "
    private val NL = "\n"
    private val DDATA = "$TAB@DData"
    private val DOUBLE_DOT = ": "
    private val VAR = "$DDATA var "
    private val EQUAL_NULL = "? = null"
    private val LATE_INIT = "$DDATA lateinit var "
    private val modelContent = "package models\n\nimport utils.dbrokers.models.ddata\nimport utils.dbrokers.models.DModel\n"
    private var controllerContent = "package controllers.controllers\n\nimport org.json.JSONObject\n\nclass "
    private var facadeContent = "package controllers\n\nimport controllers.controllers.*\nimport models.*\n\nclass Facade {\n\n"
    private var brokerContent = "package controllers.broker\n\n"
    private val listTable = mutableMapOf<String, MutableList<Pair<String, String>>>()
    private var path = directory.path + "\\generateDData"

    init {
        createOrReplaceMainDirectory()
        createSubDirectories()
        generateDDataFromScript(databaseScript)
        createFiles()
        onEnd?.invoke()
    }

    private fun createSubDirectories() {
        File("$path\\controllers").mkdir()
        File("$path\\controllers\\controllers").mkdir()
        File("$path\\controllers\\brokers").mkdir()
        File("$path\\views").mkdir()
        File("$path\\models").mkdir()
    }

    private fun createOrReplaceMainDirectory() {
        var dir = File(path)
        if (dir.listFiles() != null) {
            if (dir.listFiles().isNotEmpty()) {
                dir.deleteRecursively()
                dir = File(path)
            }
        }
        dir.mkdir()
        while(!dir.exists()) {}
    }

    private fun generateDDataFromScript(databaseScript: File) {
        var tableName = ""
        databaseScript.forEachLine { line ->
            if (line.contains("CREATE TABLE")) {
                tableName = ""
                tableName = getModelsTableName(line)
                listTable[tableName] = mutableListOf()
            } else if (line.startsWith("	`")) {
                addModelsColumn(line, tableName)
            }
        }
    }

    private fun createFiles() {
        createModelsContent()
        createControllersContent()
        createBrokerContent()
        createFacadeContent()
    }

    private fun createFacadeContent() {
        listTable.forEach { element ->
            val key = element.key
            val tab = "    "
            facadeContent += tab + "internal val " + key.toLowerCase() +"Controller = " + key + "Controller(" + key + "::class::java)\n"
        }
        facadeContent += "\n    init {\n        \n    }\n}"
        Bat.createFile("$path\\controllers\\Facade.kt", facadeContent)
    }

    private fun createControllersContent() {
        listTable.forEach { element ->
            val key = element.key
            var content = controllerContent
            content += "import controllers.brokers." + key + "Broker\n"
            content += "import models.$key\n"
            content += "import utils.views.Tools.Companion.jsonToModel\n" //todo refactor to jitpack
            content += "import mindf.ddata.controllers.Facade\n\n" //todo refactor to jitpack
            content += "class " + key + "Controllers(override val modelClass: Class<" + key + ">) : Facade() {\n\n"
            content += "    internal val broker = $key(this)\n"
            content += "    internal val list: MutableList<$key> = mutableListOf()\n\n"
            content += "    override fun initList(jsonObjects: MutableList<JSONObject) {\n"
            content += "        list = jsonToModel(jsonObjects, modelClass) as MutableList<$key>\n    }\n}"
            Bat.createFile(path + "\\controllers\\controllers\\" + element.key + "Controller.kt", content)
        }
    }

    private fun createBrokerContent() {
        listTable.forEach { element ->
            val key = element.key
            var content = brokerContent
            content += "import controllers.controllers." + key + "Controller\n"
            content += "import mindf.ddata.controllers.Broker\n\n" //todo change this from jitpack version
            content += "class " + key +"Broker(override val controller: " + key + "Controller) : Broker()"
            Bat.createFile(path + "\\controllers\\brokers\\" + element.key + "Broker.kt", content)
        }
    }

    private fun getModelsTableName(line: String): String {
        var tableName = ""
        var condition = false
        for (char in line) {
            if (char == '`') {
                condition = Tools.xor(condition)
                continue
            }
            if (condition) {
                tableName += char
            }
        }
        return tableName
    }

    private fun addModelsColumn(line: String, tableName: String) {
        var condition = false
        var columnName = ""
        var typeName = ""
        for (char in line) {
            if (char == '`') {
                condition = Tools.xor(condition)
            } else if (condition) {
                columnName += char
            } else if (!condition && columnName != "" && char != ' ') {
                typeName += char
            } else if (!condition && typeName != "" && char == ' ') {
                break
            }
        }
        typeName = convertType(typeName)
        listTable[tableName]?.add(Pair(columnName, typeName))
    }

    private fun createModelsContent() {
        listTable.forEach { element ->
            var content = modelContent
            content += element.key + " {\n\n"
            element.value.forEach { value ->
                val values = value.first + DOUBLE_DOT + value.second
                content += when (value.second) {
                    string -> LATE_INIT + values + NL
                    else -> VAR + values + EQUAL_NULL + NL
                }
            }
            Bat.createFile(path + "\\models\\" + element.key + ".kt", "$content}")
        }
    }

    private fun convertType(typeName: String): String {
        typeName.toUpperCase()
        when (typeName) {
            INT -> return int
            DOUBLE -> return double
            DATE, TIMESTAMP, DATETIME -> return string
            FLOAT -> return float
            BOOL, BOOLEAN -> return boolean
        }
        return string
    }
}
