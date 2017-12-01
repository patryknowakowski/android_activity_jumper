package pl.patrykn.activityjumper.library

import android.content.Context
import android.database.Cursor
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class GenericAdapter<Item>: RecyclerView.Adapter<GenericAdapter.ViewHolder<Item>>() {
    var onItemClickListener: ((holder : ViewHolder<Item>) -> Boolean)? = null
    var onItemLongClickListener: ((holder : ViewHolder<Item>) -> Boolean)? = null

    abstract fun getItem(position: Int): Item?

    override fun onBindViewHolder(holder: ViewHolder<Item>, position: Int) {
        val item = getItem(position)
        if ( item != null ) {
            holder.setup(item)
        }
        holder.view.setOnClickListener {
            onItemClickListener?.invoke(holder) ?: false
        }
        holder.view.setOnLongClickListener {
            onItemLongClickListener?.invoke(holder) ?: false
        }
    }

    abstract class ViewHolder<TYPE>(val view: View) : RecyclerView.ViewHolder(view) {
        constructor(layoutId: Int, parent: ViewGroup) : this(GetLayoutInflaterView(layoutId, parent))

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

abstract class VariableAdapter<Item, Source>
  ( open var source: Source? = null )
  : GenericAdapter<Item>()

abstract class ListAdapter<Item>
  ( override var source: List<Item>? )
  : VariableAdapter<Item, List<Item>>(source) {
    override fun getItemCount(): Int {
        return source?.size ?: 0
    }

    override fun getItem(position: Int): Item? {
        return source?.get(position)
    }
}

abstract class CursorAdapter: GenericAdapter<Cursor?>() {
    var cursor: Cursor? = null
        private set

    override fun getItem(position: Int): Cursor? {
        if ( cursor?.moveToPosition(position) == true ) {
            return cursor
        } else {
            return null
        }
    }

    override fun getItemCount(): Int {
        return cursor?.count ?: 0
    }

    fun refresh() {
        val cursor = swapCursor()
        notifyDataSetChanged()
        close(cursor)
    }

    fun swapCursor(): Cursor? {
        return swapCursor(generateCursor());
    }

    fun swapCursor(cursor: Cursor?): Cursor? {
        val oldCursor = this.cursor
        this.cursor = cursor
        return oldCursor
    }

    abstract fun generateCursor(): Cursor?

    fun close(cursor: Cursor?) {
        if ( cursor?.isClosed == false ) {
            cursor.close()
        }
    }
}

fun GetLayoutInflaterView(layoutId: Int, parent: ViewGroup): View {
    return GetLayoutInflaterView(parent.context, layoutId, parent)
}

fun GetLayoutInflaterView(context: Context, layoutId: Int, parent: ViewGroup? = null): View {
    return LayoutInflater.from(context).inflate(layoutId, parent, false)
}