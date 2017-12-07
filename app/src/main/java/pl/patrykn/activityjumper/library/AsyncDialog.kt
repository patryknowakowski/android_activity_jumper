package pl.patrykn.activityjumper.library

import android.app.Activity
import android.app.ProgressDialog
import android.os.AsyncTask
import android.util.Log


/**
 * Created by patrykn on 07.12.17.
 */
abstract class AsyncDialogProgressHorizontal<Params, Result>(context: Activity, message: String?, title: String?) : AsyncDialogProgress<Params, Result>(context, message, title) {
    constructor(context: Activity, message: String? = null, titleId: Int) : this(context, message, context.getString(titleId))
    constructor(context: Activity, messageId: Int, title: String? = null) : this(context, context.getString(messageId), title)

    init {
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
    }

    private fun ProgressDialog.set(value: TaskProgress?) {
        this.setMessage(value?.message)
        this.progress = value?.value ?: 0
        this.max = value?.max ?: this.progress
    }

    class TaskProgress(internal val value: Int = 0, internal val max: Int = value, message: String? = null): AsyncDialogProgress.TaskProgress(message)

    fun publishProgress(message: String? = null, value: Int = 0, max: Int = value) {
        publishProgress(value, max, message)
    }

    fun publishProgress(value: Int = 0, message: String? = null, max: Int = value) {
        publishProgress(value, max, message)
    }

    fun publishProgress(value: Int = 0, max: Int = value, message: String? = null) {
        publishProgress(TaskProgress(value, max, message))
    }
}

abstract class AsyncDialogProgress<Params, Result>(context: Activity, message: String? = null, title: String? = null) : AsyncDialog<Params, AsyncDialogProgress.TaskProgress, Result>(context, message, title) {
    constructor(context: Activity, message: String? = null, titleId: Int): this(context, message, context.getString(titleId))
    constructor(context: Activity, messageId: Int, title: String? = null): this(context, context.getString(messageId), title)

    override fun onProgressUpdate(vararg values: TaskProgress?) {
        super.onProgressUpdate(*values)
        dialog.set(values[values.size - 1])
    }

    private fun ProgressDialog.set(value: TaskProgress?) {
        this.setMessage(value?.message)
    }

    open class TaskProgress(internal val message: String? = null)

    fun publishProgress(message: String? = null) {
        publishProgress(TaskProgress(message))
    }
}

abstract class AsyncDialog<Params, Progress, Result>(context: Activity, message:String? = null, title: String? = null): AsyncTask<Params, Progress, Result>() {
    private val LOG_TAG = this::class.java.simpleName
    val dialog = ProgressDialog(context)

    constructor(context: Activity, message: String? = null, titleId: Int): this(context, message, context.getString(titleId))
    constructor(context: Activity, messageId: Int, title: String? = null): this(context, context.getString(messageId), title)

    init {
        dialog.setTitle(title)
        dialog.setMessage(message)
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
    }

    override fun onPreExecute() {
        super.onPreExecute()
        if ( dialog.isShowing == false ) {
            dialog.show();
        }
    }

    override fun onPostExecute(result: Result) {
        super.onPostExecute(result)
        if ( dialog.isShowing ) {
            try {
                dialog.dismiss()
            } catch (e: IllegalArgumentException) {
                Log.e(LOG_TAG, "Wyjątek przy zamykaniu dialogu", e)
            }

        }
    }
}