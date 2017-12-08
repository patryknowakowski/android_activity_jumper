package pl.patrykn.activityjumper.library

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.SqlType
import org.jetbrains.anko.db.createTable
import org.jetbrains.anko.db.dropTable
import java.util.*

/**
 * Created by patrykn on 29.11.17.
 */
open class TableSchema(val name: String, vararg val columns: Pair<String, SqlType>)

internal fun SQLiteDatabase.createTable(table: TableSchema, ifNotExists: Boolean = true) = this.createTable(table.name, ifNotExists, *table.columns)
internal fun SQLiteDatabase.dropTable(table: TableSchema, ifExists: Boolean = true) = this.dropTable(table.name, ifExists)

fun Cursor.getBoolean( columnName: String ): Boolean? = this.getIndex(columnName)?.let { this.getInt(it)?.let { if ( it == 1 ) true else false } }
fun Cursor.getString( columnName: String ): String? = this.getIndex(columnName)?.let { this.getString(it) }
fun Cursor.getInt( columnName: String ): Int? = this.getIndex(columnName)?.let { this.getInt(it) }
fun Cursor.getLong( columnName: String ): Long? = this.getIndex(columnName)?.let { this.getLong(it) }
fun Cursor.getFloat( columnName: String ): Float? = this.getIndex(columnName)?.let { this.getFloat(it) }
fun Cursor.getDouble( columnName: String ): Double? = this.getIndex(columnName)?.let { this.getDouble(it) }
fun Cursor.getBlob( columnName: String ): ByteArray? = this.getIndex(columnName)?.let { this.getBlob(it) }

fun ContentValues.put(key: String, value: Boolean?) = this.put(key, value?.let { if ( it ) 1 else 0 } )

fun ContentValues.put(key: String, value: Date?) = this.put(key, value?.time)
fun Cursor.getDate( columnName: String ): Date? = this.getIndex(columnName)?.let { Date(this.getLong(it)) }

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


fun Cursor.getIndex( columnName: String ): Int? = this.getColumnIndex(columnName).let { if (it == -1) null else it }