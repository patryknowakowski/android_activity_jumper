package pl.patrykn.activityjumper.library

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*
import java.util.*

/**
 * Created by patrykn on 29.11.17.
 */
public infix fun <A, B, C, D> A.to(that: B): Pair<C, B> where A: Pair<C, D> = Pair(this.first, that)

open class TableSchema(val name: String, internal vararg val schema: Pair<String, SqlType>) {
}

internal fun SQLiteDatabase.createTable(table: TableSchema, ifNotExists: Boolean = true) = this.createTable(table.name, ifNotExists, *table.schema)
internal fun SQLiteDatabase.dropTable(table: TableSchema, ifExists: Boolean = true) = this.dropTable(table.name, ifExists)


fun ManagedSQLiteOpenHelper.recreate(schema: TableSchema) {
    transaction {
        clean(schema)
    }
}
fun ManagedSQLiteOpenHelper.clean(schema: TableSchema) {
    drop(schema)
    create(schema)
}
fun ManagedSQLiteOpenHelper.drop(schema: TableSchema) = this.writableDatabase.dropTable(schema)
fun ManagedSQLiteOpenHelper.create(schema: TableSchema) = this.writableDatabase.createTable(schema)
//fun ManagedSQLiteOpenHelper.select(schema: TableSchema) = this.readableDatabase.select(schema.name, )

fun SQLiteDatabase.select(schema: TableSchema) = select(schema.name)
//fun SQLiteDatabase.select(schema: TableSchema, vararg columns: Pair<String, SqlType>) //TODO
fun SQLiteDatabase.select(schema: TableSchema, vararg columns: String) = select(schema.name, *columns)

fun ManagedSQLiteOpenHelper.transaction(code: SQLiteDatabase.() -> Unit) = this.writableDatabase.transaction(code)

fun Cursor.getBoolean(pair: Pair<String, SqlType>) = getBoolean(pair.first)
fun Cursor.getString(pair: Pair<String, SqlType>) = getString(pair.first)
fun Cursor.getInt(pair: Pair<String, SqlType>) = getInt(pair.first)
fun Cursor.getShort(pair: Pair<String, SqlType>) = getShort(pair.first)
fun Cursor.getLong(pair: Pair<String, SqlType>) = getLong(pair.first)
fun Cursor.getFloat(pair: Pair<String, SqlType>) = getFloat(pair.first)
fun Cursor.getDouble(pair: Pair<String, SqlType>) = getDouble(pair.first)
fun Cursor.getBlob(pair: Pair<String, SqlType>) = getBlob(pair.first)

fun Cursor.getBoolean( columnName: String ) = getInt(columnName)?.let { it == 1 }
fun Cursor.getString( columnName: String ) = getIndexNotNull(columnName)?.let { getString(it) }
fun Cursor.getInt( columnName: String ) = getIndexNotNull(columnName)?.let { getInt(it) }
fun Cursor.getShort( columnName: String ) = getIndexNotNull(columnName)?.let { getShort(it) }
fun Cursor.getLong( columnName: String ) = getIndexNotNull(columnName)?.let { getLong(it) }
fun Cursor.getFloat( columnName: String ) = getIndexNotNull(columnName)?.let { getFloat(it) }
fun Cursor.getDouble( columnName: String ) = getIndexNotNull(columnName)?.let { getDouble(it) }
fun Cursor.getBlob( columnName: String ) = getIndexNotNull(columnName)?.let { getBlob(it) }

fun ContentValues.put(key: String, value: Boolean?) = put(key, value?.let { if ( it ) 1 else 0 } )

fun ContentValues.put(key: String, value: Date?) = put(key, value?.time)
fun Cursor.getDate( columnName: String ): Date? = getIndexNotNull(columnName)?.let { Date(getLong(it)) }

fun Cursor.tryClose() = { if (this.isOpen) this.close() }

val Cursor.isOpen
    get() = !this.isClosed

val Cursor.iterator: Iterable<Cursor>
    get() {
        return object : Iterable<Cursor> {
            private val c = this@iterator

            init {
                c.moveToPosition(-1)
            }

            override fun iterator(): Iterator<Cursor> {
                return object : Iterator<Cursor> {
                    override fun hasNext(): Boolean {
                        c.moveToNext()
                        if ( c.isAfterLast ) {
                            c.close()
                            return false
                        }
                        return true
                    }
                    override fun next(): Cursor {
                        return c
                    }
                }
            }
        }
    }

fun Cursor.getIndexNotNull( columnName: String ): Int? = getIndex(columnName)?.let { if (isNull(it)) null else it };
fun Cursor.getIndex( columnName: String ): Int? = getColumnIndex(columnName).let { if (it == -1) null else it }