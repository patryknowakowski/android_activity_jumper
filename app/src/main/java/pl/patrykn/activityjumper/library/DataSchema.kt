package pl.patrykn.activityjumper.library

import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.SqlType
import org.jetbrains.anko.db.createTable
import org.jetbrains.anko.db.dropTable

/**
 * Created by patrykn on 29.11.17.
 */
open class TableSchema(val name: String, vararg val columns: Pair<String, SqlType>)

internal fun SQLiteDatabase.createTable(table: TableSchema, ifNotExists: Boolean = true) {
    this.createTable(table.name, ifNotExists, *table.columns)
}

internal fun SQLiteDatabase.dropTable(table: TableSchema, ifExists: Boolean = true) {
    this.dropTable(table.name, ifExists)
}