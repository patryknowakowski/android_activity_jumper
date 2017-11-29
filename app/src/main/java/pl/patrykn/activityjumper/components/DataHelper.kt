package pl.patrykn.activityjumper.components

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*
import pl.patrykn.activityjumper.library.TableSchema
import pl.patrykn.activityjumper.library.createTable
import pl.patrykn.activityjumper.library.dropTable

private val DatabaseName = "MyDatabase"
private val DatabaseVersion = 1

object Location: TableSchema("LOCATION"
        , column.ID to INTEGER + PRIMARY_KEY
        , column.NAME to TEXT + NOT_NULL) {

    object column {
        val ID = "LOC_ID"
        val NAME = "LOC_NAME"
    }
}

object Dane: TableSchema("DATA"
        , column.ID to INTEGER + PRIMARY_KEY
        , column.CODE to TEXT + NOT_NULL
        , column.VALUE to TEXT + NOT_NULL) {

    object column {
        val ID = "DATA_ID";
        val CODE = "DATA_CODE";
        val VALUE = "DATA_VALUE";
    }
}

class DataHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, DatabaseName, null, DatabaseVersion) {
    companion object {
        private var instance: DataHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): DataHelper {
            if (instance == null) {
                instance = DataHelper(ctx.getApplicationContext())
            }
            return instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(Location);
        db.createTable(Dane);
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable(Location)
        db.dropTable(Dane)
    }
}

// Access property for Context
val Context.database: DataHelper
    get() = DataHelper.getInstance(getApplicationContext())