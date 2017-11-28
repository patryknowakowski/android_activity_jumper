package pl.patrykn.activityjumper.components

import android.database.Cursor
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class GenericAdapter<Item>: RecyclerView.Adapter<GenericAdapter.ViewHolder<Item>>() {
    private val LOG_TAG = GenericAdapter::class.java.simpleName
    var onItemClickListener: ((holder : ViewHolder<Item>) -> Boolean)? = null
    var onItemLongClickListener: ((holder : ViewHolder<Item>) -> Boolean)? = null

    fun GetLayoutInflaterView(parent: ViewGroup, layoutId: Int): View {
        return LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
    }

    abstract fun getItem(position: Int): Item

    override fun onBindViewHolder(holder: ViewHolder<Item>, position: Int) {
        fun onClickWrapper(onItemClickListener: ((holder : ViewHolder<Item>) -> Boolean)?, holder: ViewHolder<Item>): Boolean {
            if ( onItemClickListener != null ) {
                return onItemClickListener(holder)
            }
            return false;
        }

        val item = getItem(position)
        holder.setup(item)
        holder.view.setOnClickListener {
            onClickWrapper(onItemClickListener, holder)
        }
        holder.view.setOnLongClickListener {
            onClickWrapper(onItemLongClickListener, holder)
        }
    }

    abstract class ViewHolder<TYPE>(val view: View) : RecyclerView.ViewHolder(view) {
        var item: TYPE? = null
            private set

        fun setup(item: TYPE) {
            this.item = item
            set(item)
        }

        abstract fun set(item: TYPE)
    }

    protected fun getAdapter(): GenericAdapter<Item> {
        return this
    }
}

abstract class CursorAdapter: GenericAdapter<Cursor?>() {
    private val LOG_TAG = CursorAdapter::class.java.simpleName
    var cursor: Cursor? = null
        private set

    fun refresh() {
        val cursor = swapCursor()
        notifyDataSetChanged()
        close(cursor)
    }

    fun swapCursor(): Cursor? {
        return swapCursor(getCreateCursor())
    }

    fun swapCursor(cursor: Cursor): Cursor? {
        val oldCursor = this.cursor
        this.cursor = cursor
        return oldCursor
    }

    abstract fun getCreateCursor(): Cursor

    override fun getItem(position: Int): Cursor? {
        if ( cursor?.moveToPosition(position) == true ) {
            return cursor
        } else {
            return null
        }
    }

    override fun getItemCount(): Int {
        return cursor?.getCount() ?: 0
    }

    fun close(cursor: Cursor?) {
        if ( cursor?.isClosed == false ) {
            cursor.close()
        }
    }
}

abstract class ListAdapter<TYPE>(var list: List<TYPE?>?) : GenericAdapter<TYPE?>() {
    private val LOG_TAG = ListAdapter::class.java.simpleName

    constructor(): this(null) {
    }

    override fun getItem(position: Int): TYPE? {
        return list?.get(position)
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }
}