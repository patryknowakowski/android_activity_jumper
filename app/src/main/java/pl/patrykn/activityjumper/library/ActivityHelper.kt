package pl.patrykn.activityjumper.library

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import kotlin.reflect.KClass
import kotlin.reflect.KDeclarationContainer
import kotlin.reflect.KProperty

/**
 * Created by patrykn on 09.12.17.
 */
fun Context.LogVerbose(message: String, tr: Throwable? = null) = Logger(Log.VERBOSE, message, tr)
fun Context.LogDebug(message: String, tr: Throwable? = null) = Logger(Log.DEBUG, message, tr)
fun Context.LogInfo(message: String, tr: Throwable? = null) = Logger(Log.INFO, message, tr)
fun Context.LogWarn(message: String, tr: Throwable? = null) = Logger(Log.WARN, message, tr)
fun Context.LogError(message: String, tr: Throwable? = null) = Logger(Log.ERROR, message, tr)
fun Context.Logger(priority:Int, msg: String, tr: Throwable? = null) = Log.println(priority, LOG_TAG, msg + (tr?.let { Log.getStackTraceString(it) } ?: "") )

val Context.LOG_TAG
        get() = this.javaClass.simpleName

class IntentExtraString(name: String? = null): IntentExtra(name) {
    operator fun getValue(intent: Intent, property: KProperty<*>): String? = intent.getStringExtra(property.extraName)

    operator fun setValue(intent: Intent, property: KProperty<*>, value: String?) {
        Log.d("T", "Value = ${value ?: "null"}")
        intent.putExtra(property.extraName, value)
    }
}

class IntentExtraInt(name: String? = null): IntentExtra(name) {
    operator fun getValue(intent: Intent, property: KProperty<*>): Int? = if (intent.hasExtra(property.extraName)) intent.getIntExtra(property.extraName, -1) else null

    operator fun setValue(intent: Intent, property: KProperty<*>, value: Int?) {
        if ( value != null ) {
            intent.putExtra(property.extraName, value)
        } else {
            intent.removeExtra(property.extraName)
        }
    }
}

abstract class IntentExtra(private val name: String?) {
    protected val KProperty<*>.extraName get() = this@IntentExtra.name ?: fallbackName
    private val KProperty<*>.fallbackName get() = ownerCanonicalName?.let { "$it::$name" } ?: name
    private val KProperty<*>.ownerCanonicalName: String? get() = if (this is KClass<*>) this. java.canonicalName else null
}