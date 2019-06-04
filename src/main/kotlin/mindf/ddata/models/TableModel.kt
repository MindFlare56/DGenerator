package mindf.ddata.models

class TableModel {

    var values: MutableList<String> = arrayListOf()
    var rows: MutableList<Row> = arrayListOf()
    var name: String
    var columns: MutableList<String> = arrayListOf()

    constructor(name: String, rows: MutableList<Row>) {
        this.name = name
        this.rows = rows as ArrayList<Row>
    }

    constructor(name: String, columns: MutableList<String>, values: MutableList<String>) {
        this.name = name
        this.columns = columns
        this.values = values
    }

    fun add(row: Row) {
        this.rows.add(row)
    }

    fun addAll(rows: List<Row>) {
        this.rows.addAll(rows)
    }
}
