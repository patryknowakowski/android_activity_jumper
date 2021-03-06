package pl.patrykn.activityjumper.library

import android.content.Context
import android.database.Cursor
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

typealias ItemClickListener<Item> = (adapter: GenericAdapter<Item>, holder : GenericAdapter.ViewHolder<Item>, item: Item?) -> Boolean

abstract class GenericAdapter<Item>: RecyclerView.Adapter<GenericAdapter.ViewHolder<Item>>() {
    var onItemClickListener: ItemClickListener<Item>? = null
    var onItemLongClickListener: ItemClickListener<Item>? = null

    abstract fun getItem(position: Int): Item?

    override fun onBindViewHolder(holder: ViewHolder<Item>, position: Int) {
        //holder.item = getItem(position)
        holder.decor(getItem(position))
        holder.view.setOnClickListener { onItemClickListener?.invoke(this, holder, getItem(position)) ?: false }
        holder.view.setOnLongClickListener { onItemLongClickListener?.invoke(this, holder, getItem(position)) ?: false }
    }

    abstract class ViewHolder<TYPE>(val view: View) : RecyclerView.ViewHolder(view) {
        constructor(context: Context, layoutId: Int, parent: ViewGroup?) : this(GetLayoutInflaterView(context, layoutId, parent))
        constructor(layoutId: Int, parent: ViewGroup) : this(GetLayoutInflaterView(parent.context, layoutId, parent))

        /*var item: TYPE? = null
            decor(value) {
                field = value
                if ( value != null ) {
                    decor(value)
                }
            }*/

        abstract fun decor(item: TYPE?)

        fun <T : View> findViewById(id: Int): T {
            return view.findViewById(id)
        }
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

abstract class CursorAdapter: GenericAdapter<Cursor>() {
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
        return swapCursor(generateCursor())
    }

    fun swapCursor(cursor: Cursor?): Cursor? {
        val oldCursor = this.cursor
        this.cursor = cursor
        return oldCursor
    }

    abstract fun generateCursor(): Cursor?

    fun close() {
        close(cursor)
    }

    fun close(cursor: Cursor?) {
        if ( cursor?.isClosed == false ) {
            cursor.close()
        }
    }

    abstract class ViewHolder(view: View) : GenericAdapter.ViewHolder<Cursor>(view) {
        constructor(context: Context, layoutId: Int, parent: ViewGroup?) : this(GetLayoutInflaterView(context, layoutId, parent))
        constructor(layoutId: Int, parent: ViewGroup) : this(GetLayoutInflaterView(parent.context, layoutId, parent))
    }
}

fun GetLayoutInflaterView(layoutId: Int, parent: ViewGroup): View {
    return GetLayoutInflaterView(parent.context, layoutId, parent)
}

fun GetLayoutInflaterView(context: Context, layoutId: Int, parent: ViewGroup?): View {
    return LayoutInflater.from(context).inflate(layoutId, parent, false)
}