package pl.patrykn.activityjumper.components

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*
import pl.patrykn.activityjumper.library.*

// Access property for Context
val Context.database: DataSQLiteOpenHelper
    get() = DataSQLiteOpenHelper.getInstance(getApplicationContext())

class DataSQLiteOpenHelper(context: Context) : ManagedSQLiteOpenHelper(context, DatabaseName, null, DatabaseVersion) {
    companion object {
        private var instance: DataSQLiteOpenHelper? = null
        val DatabaseName = "AppCache"
        val DatabaseVersion = 6

        @Synchronized
        fun getInstance(ctx: Context): DataSQLiteOpenHelper {
            if (instance == null) {
                instance = DataSQLiteOpenHelper(ctx.getApplicationContext())
            }
            return instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(TableApplications);
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable(TableApplications)
        onCreate(db)
    }
}

object TableApplications : TableSchema("APPLICATIONS"
        , column.ID
        , column.LABEL
        , column.PACKAGE_NAME
        , column.CATEGORY
        , column.FLAGS
        , column.MIN_SDK_VERSION
        , column.PERMISSION
        , column.PROCESS_NAME
        , column.TARGET_SDK_VERSION
        , column.TASK_AFFINITY
        , column.THEME
        , column.UID ) {
    object column {
        val ID = "APP_ID" to INTEGER + PRIMARY_KEY
        val LABEL = "APP_LABEL" to TEXT + NOT_NULL
        val PACKAGE_NAME = "APP_PACKAGE_NAME" to TEXT + NOT_NULL + UNIQUE
        val CATEGORY = "APP_CATEGORY" to INTEGER + NOT_NULL
        val FLAGS = "APP_FLAGS" to INTEGER + NOT_NULL
        val MIN_SDK_VERSION = "APP_MIN_SDK_VERSION" to INTEGER + NOT_NULL
        val PERMISSION = "APP_PERMISSION" to TEXT
        val PROCESS_NAME = "APP_PROCESS_NAME" to TEXT + NOT_NULL
        val TARGET_SDK_VERSION = "APP_TARGET_SDK_VERSION" to INTEGER + NOT_NULL
        val TASK_AFFINITY = "APP_TASK_AFFINITY" to TEXT
        val THEME = "APP_THEME" to INTEGER + NOT_NULL
        val UID = "UID" to INTEGER + NOT_NULL
    }
}

fun SQLiteDatabase.insert(app: AppItem): Long = with(TableApplications.column) {
        insert(TableApplications.name,
            LABEL to app.label
            , PACKAGE_NAME to app.packageName
            , CATEGORY to app.category
            , FLAGS to app.flags
            , MIN_SDK_VERSION to app.minSdkVersion
            , PERMISSION to app.permission
            , PROCESS_NAME to app.processName
            , TARGET_SDK_VERSION to app.targetSdkVersion
            , TASK_AFFINITY to app.taskAffinity
            , THEME to app.theme
            , UID to app.uid
        )
    }

fun AppItem(cursor: Cursor) = with(cursor) {
    with(TableApplications.column) {
        AppItem(
            getString(LABEL) ?: "",
            getString(PACKAGE_NAME) ?: "",
            getInt(CATEGORY) ?: 0,
            getInt(FLAGS) ?: 0,
            getInt(MIN_SDK_VERSION) ?: 0,
            getString(PERMISSION),
            getString(PROCESS_NAME) ?: "",
            getInt(TARGET_SDK_VERSION) ?: 0,
            getString(TASK_AFFINITY),
            getInt(THEME) ?: 0,
            getInt(UID) ?: 0
        )
   }
}