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

fun Cursor.getBoolean( columnName: String ): Boolean? = getInt(columnName)?.let { it == 1 }
fun Cursor.getString( columnName: String ): String? = getIndexNotNull(columnName)?.let { getString(it) }
fun Cursor.getInt( columnName: String ): Int? = getIndexNotNull(columnName)?.let { getInt(it) }
fun Cursor.getShort( columnName: String ): Short? = getIndexNotNull(columnName)?.let { getShort(it) }
fun Cursor.getLong( columnName: String ): Long? = getIndexNotNull(columnName)?.let { getLong(it) }
fun Cursor.getFloat( columnName: String ): Float? = getIndexNotNull(columnName)?.let { getFloat(it) }
fun Cursor.getDouble( columnName: String ): Double? = getIndexNotNull(columnName)?.let { getDouble(it) }
fun Cursor.getBlob( columnName: String ): ByteArray? = getIndexNotNull(columnName)?.let { getBlob(it) }

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