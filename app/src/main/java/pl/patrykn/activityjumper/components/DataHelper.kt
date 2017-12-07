package pl.patrykn.activityjumper.components

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*
import pl.patrykn.activityjumper.library.TableSchema
import pl.patrykn.activityjumper.library.createTable
import pl.patrykn.activityjumper.library.dropTable

private val DatabaseName = "AppCache"
private val DatabaseVersion = 2


object TableApplications : TableSchema("APPLICATIONS"
        , column.ID to INTEGER + PRIMARY_KEY
        , column.LABEL to TEXT + NOT_NULL
        , column.PACKAGE_NAME to TEXT + NOT_NULL
        , column.CATEGORY to INTEGER + NOT_NULL
        , column.FLAGS to INTEGER + NOT_NULL
        , column.MIN_SDK_VERSION to INTEGER + NOT_NULL
        , column.PERMISSION to TEXT
        , column.PROCESS_NAME to TEXT + NOT_NULL
        , column.TARGET_SDK_VERSION to INTEGER + NOT_NULL
        , column.TASK_AFFINITY to TEXT
        , column.THEME to INTEGER + NOT_NULL
        , column.UID to INTEGER + NOT_NULL ) {
    object column {
        val ID = "APP_ID"
        val LABEL = "APP_LABEL"
        val PACKAGE_NAME = "APP_PACKAGE_NAME"
        val CATEGORY = "APP_CATEGORY"
        val FLAGS = "APP_FLAGS"
        val MIN_SDK_VERSION = "APP_MIN_SDK_VERSION"
        val PERMISSION = "APP_PERMISSION"
        val PROCESS_NAME = "APP_PROCESS_NAME"
        val TARGET_SDK_VERSION = "APP_TARGET_SDK_VERSION"
        val TASK_AFFINITY = "APP_TASK_AFFINITY"
        val THEME = "APP_THEME"
        val UID = "UID"
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
        db.createTable(TableApplications);
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable(TableApplications)
    }

    /*fun insert(appItem: AppItem): Int {
        return
    }*/
}

// Access property for Context
val Context.database: DataHelper
    get() = DataHelper.getInstance(getApplicationContext())