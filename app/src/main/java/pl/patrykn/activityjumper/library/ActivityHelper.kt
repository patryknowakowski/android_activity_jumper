package pl.patrykn.activityjumper.library

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import java.io.Serializable
import java.util.ArrayList
import kotlin.reflect.KClass
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

abstract class IntentCompanion<out IntentOptions>(val intentOptions: IntentOptions, kclass: KClass<out Activity> ) {
    val javaClass = kclass.java

    inline fun startActivity(context: Context, configureIntent: IntentOptions.(Intent) -> Unit) =
            context.startActivity(instance(context, configureIntent))

    inline fun startActivity(context: Context) = context.startActivity(instance(context))

    inline fun instance(context: Context, configureIntent: IntentOptions.(Intent) -> Unit): Intent =
            instance(context).apply { configureIntent(intentOptions, this) }

    inline fun instance(context: Context): Intent = Intent(context, javaClass)

    inline fun <T> params(activity: Activity, block: IntentOptions.(Intent) -> T): T = block(intentOptions, activity.intent)
}

open class IntentExtra {
    protected class ExtraString(name: String? = null): Extra<String>(name) {
        override fun Intent.put(name: String, value: String) = putExtra(name, value)
        override fun Intent.get(name: String) = getStringExtra(name)
    }

    protected class ExtraInt(name: String? = null, val defaultValue: Int = 0): Extra<Int>(name) {
        constructor(defaultValue: Int = 0): this(null, defaultValue)
        override fun Intent.put(name: String, value: Int) = putExtra(name, value)
        override fun Intent.get(name: String) = getIntExtra(name, defaultValue)
    }

    protected class ExtraBoolean(name: String? = null, val defaultValue: Boolean = false): Extra<Boolean>(name) {
        constructor(defaultValue: Boolean = false): this(null, defaultValue)
        override fun Intent.put(name: String, value: Boolean) = putExtra(name, value)
        override fun Intent.get(name: String) = getBooleanExtra(name, defaultValue)
    }

    protected class ExtraByte(name: String? = null, val defaultValue: Byte = 0): Extra<Byte>(name) {
        constructor(defaultValue: Byte = 0): this(null, defaultValue)
        override fun Intent.put(name: String, value: Byte) = putExtra(name, value)
        override fun Intent.get(name: String) = getByteExtra(name, defaultValue)
    }

    protected class ExtraShort(name: String? = null, val defaultValue: Short = 0): Extra<Short>(name) {
        constructor(defaultValue: Short = 0): this(null, defaultValue)
        override fun Intent.put(name: String, value: Short) = putExtra(name, value)
        override fun Intent.get(name: String) = getShortExtra(name, defaultValue)
    }

    protected class ExtraChar(name: String? = null, val defaultValue: Char = ' '): Extra<Char>(name) {
        constructor(defaultValue: Char = ' '): this(null, defaultValue)
        override fun Intent.put(name: String, value: Char) = putExtra(name, value)
        override fun Intent.get(name: String) = getCharExtra(name, defaultValue)
    }

    protected class ExtraLong(name: String? = null, val defaultValue: Long = 0): Extra<Long>(name) {
        constructor(defaultValue: Long = 0): this(null, defaultValue)
        override fun Intent.put(name: String, value: Long) = putExtra(name, value)
        override fun Intent.get(name: String) = getLongExtra(name, defaultValue)
    }

    protected class ExtraFloat(name: String? = null, val defaultValue: Float = 0f): Extra<Float>(name) {
        constructor(defaultValue: Float = 0f): this(null, defaultValue)
        override fun Intent.put(name: String, value: Float) = putExtra(name, value)
        override fun Intent.get(name: String) = getFloatExtra(name, defaultValue)
    }

    protected class ExtraDouble(name: String? = null, val defaultValue: Double = 0.0): Extra<Double>(name) {
        constructor(defaultValue: Double = 0.0): this(null, defaultValue)
        override fun Intent.put(name: String, value: Double) = putExtra(name, value)
        override fun Intent.get(name: String) = getDoubleExtra(name, defaultValue)
    }

    protected class ExtraCharSequence(name: String? = null): Extra<CharSequence>(name) {
        override fun Intent.put(name: String, value: CharSequence) = putExtra(name, value)
        override fun Intent.get(name: String) = getCharSequenceExtra(name)
    }

    protected class ExtraParcelableArray(name: String? = null): Extra<Array<Parcelable>>(name) {
        override fun Intent.put(name: String, value: Array<Parcelable>) = putExtra(name, value)
        override fun Intent.get(name: String) = getParcelableArrayExtra(name)
    }

    protected class ExtraSerializable(name: String? = null): Extra<Serializable>(name) {
        override fun Intent.put(name: String, value: Serializable) = putExtra(name, value)
        override fun Intent.get(name: String) = getSerializableExtra(name)
    }

    protected class ExtraIntegerArrayList(name: String? = null): Extra<ArrayList<Int>>(name) {
        override fun Intent.put(name: String, value: ArrayList<Int>) = putExtra(name, value)
        override fun Intent.get(name: String) = getIntegerArrayListExtra(name)
    }

    protected class ExtraStringArrayList(name: String? = null): Extra<ArrayList<String>>(name) {
        override fun Intent.put(name: String, value: ArrayList<String>) = putExtra(name, value)
        override fun Intent.get(name: String) = getStringArrayListExtra(name)
    }

    protected class ExtraCharSequenceArrayList(name: String? = null): Extra<ArrayList<CharSequence>>(name) {
        override fun Intent.put(name: String, value: ArrayList<CharSequence>) = putExtra(name, value)
        override fun Intent.get(name: String) = getCharSequenceArrayListExtra(name)
    }

    protected class ExtraBooleanArray(name: String? = null): Extra<BooleanArray>(name) {
        override fun Intent.put(name: String, value: BooleanArray) = putExtra(name, value)
        override fun Intent.get(name: String) = getBooleanArrayExtra(name)
    }

    protected class ExtraByteArray(name: String? = null): Extra<ByteArray>(name) {
        override fun Intent.put(name: String, value: ByteArray) = putExtra(name, value)
        override fun Intent.get(name: String) = getByteArrayExtra(name)
    }

    protected class ExtraShortArray(name: String? = null): Extra<ShortArray>(name) {
        override fun Intent.put(name: String, value: ShortArray) = putExtra(name, value)
        override fun Intent.get(name: String) = getShortArrayExtra(name)
    }

    protected class ExtraCharArray(name: String? = null): Extra<CharArray>(name) {
        override fun Intent.put(name: String, value: CharArray) = putExtra(name, value)
        override fun Intent.get(name: String) = getCharArrayExtra(name)
    }

    protected class ExtraIntArray(name: String? = null): Extra<IntArray>(name) {
        override fun Intent.put(name: String, value: IntArray) = putExtra(name, value)
        override fun Intent.get(name: String) = getIntArrayExtra(name)
    }

    protected class ExtraLongArray(name: String? = null): Extra<LongArray>(name) {
        override fun Intent.put(name: String, value: LongArray) = putExtra(name, value)
        override fun Intent.get(name: String) = getLongArrayExtra(name)
    }

    protected class ExtraFloatArray(name: String? = null): Extra<FloatArray>(name) {
        override fun Intent.put(name: String, value: FloatArray) = putExtra(name, value)
        override fun Intent.get(name: String) = getFloatArrayExtra(name)
    }

    protected class ExtraDoubleArray(name: String? = null): Extra<DoubleArray>(name) {
        override fun Intent.put(name: String, value: DoubleArray) = putExtra(name, value)
        override fun Intent.get(name: String) = getDoubleArrayExtra(name)
    }

    protected class ExtraStringArray(name: String? = null): Extra<Array<String>>(name) {
        override fun Intent.put(name: String, value: Array<String>) = putExtra(name, value)
        override fun Intent.get(name: String) = getStringArrayExtra(name)
    }

    protected class ExtraCharSequenceArray(name: String? = null): Extra<Array<CharSequence>>(name) {
        override fun Intent.put(name: String, value: Array<CharSequence>) = putExtra(name, value)
        override fun Intent.get(name: String) = getCharSequenceArrayExtra(name)
    }

    protected class ExtraBundle(name: String? = null): Extra<Bundle>(name) {
        override fun Intent.put(name: String, value: Bundle) = putExtra(name, value)
        override fun Intent.get(name: String) = getBundleExtra(name)
    }

    protected class ExtraParcelable<T : Parcelable>(name: String? = null): Extra<T>(name) {
        override fun Intent.put(name: String, value: T) = putExtra(name, value)
        override fun Intent.get(name: String) = getParcelableExtra<T>(name)
    }

    protected class ExtraParcelableArrayList<T : Parcelable>(name: String? = null): Extra<ArrayList<T>>(name) {
        override fun Intent.put(name: String, value: ArrayList<T>) = putExtra(name, value)
        override fun Intent.get(name: String) = getParcelableArrayListExtra<T>(name)
    }

    protected abstract class Extra<TYPE>(private val name: String?) {
        private val KProperty<*>.extraName get() = this@Extra.name ?: fallbackName
        private val KProperty<*>.fallbackName get() = ownerCanonicalName?.let { "$it::$name" } ?: name
        private val KProperty<*>.ownerCanonicalName: String? get() = if (this is KClass<*>) this.java.canonicalName else null

        operator fun getValue(intent: Intent, property: KProperty<*>): TYPE? = if (intent.hasExtra(property.extraName)) intent.get(property.extraName) else null
        operator fun setValue(intent: Intent, property: KProperty<*>, value: TYPE?) {
            if ( value != null ) {
                intent.put(property.extraName, value)
            } else {
                intent.removeExtra(property.extraName)
            }
        }

        abstract fun Intent.put(name: String, value: TYPE): Intent
        abstract fun Intent.get(name: String): TYPE
    }
}
