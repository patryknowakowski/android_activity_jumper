package pl.patrykn.activityjumper
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_list.*
import pl.patrykn.activityjumper.components.*
import pl.patrykn.activityjumper.library.*

class ListActivity : AppCompatActivity() {
    var adapter: CursorAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        list_activity__list.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        adapter = object: CursorAdapter() {
            override fun generateCursor(): Cursor? = database.readableDatabase.query(TableApplications.name, null, null, null, null, null, TableApplications.column.PACKAGE_NAME.first)
            override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): GenericAdapter.ViewHolder<Cursor> {
                return Holder(R.layout.activity_list_item, parent)
            }

            inner class Holder(layoutId: Int, parent: ViewGroup?): ViewHolder(baseContext, layoutId, parent) {
                val appName: TextView = findViewById(R.id.list_activity_item__name)
                val appIcon: ImageView = findViewById(R.id.list_activity_item__icon)
                val packgeName: TextView = findViewById(R.id.list_activity_item__package)

                override fun decor(cursor: Cursor?) {
                    cursor?.let {
                        val item = AppItem(it)
                        appName.text = item.label
                        packgeName.text = item.packageName
                        appIcon.setImageDrawable(item.getApplicationIcon(baseContext.packageManager))
                    }
                }
            }
        }
        adapter?.onItemClickListener = { adapter, holder, item ->
            item?.let { cursor ->
                AppActivity.intentCompanion.startActivity(this) {
                    it.app = AppItem(cursor)
                }
            }
            false
        }
        list_activity__list.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_list_activity, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.list_activity__menu_refresh -> {
                object: AsyncDialogProgress<Unit, Unit>(this) {
                    override fun doInBackground(vararg params: Unit?) {
                        with(database) {
                            transaction {
                                clean(TableApplications)
                                    for (a in baseContext.packageManager.GetAppItemList(PackageManager.GET_ACTIVITIES or PackageManager.GET_RECEIVERS or PackageManager.GET_SERVICES or PackageManager.GET_INSTRUMENTATION or PackageManager.GET_INTENT_FILTERS or PackageManager.GET_SIGNATURES or PackageManager.GET_RESOLVED_FILTER or PackageManager.GET_META_DATA or PackageManager.GET_GIDS or PackageManager.GET_DISABLED_COMPONENTS or PackageManager.GET_PERMISSIONS or PackageManager.GET_CONFIGURATIONS)) {
                                        insert(a)
                                    }
                            }
                        }

                        return Unit
                    }

                    override fun onPostExecute(result: Unit) {
                        super.onPostExecute(result)
                        refreshList()
                    }
                }.execute()
                return true
            }
            else -> return super.onContextItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        refreshList()
    }

    fun refreshList() {
        adapter?.let {
            object : AsyncDialog<Void, Void, Cursor?>(this) {
                override fun doInBackground(vararg params: Void?) = it.swapCursor()
                override fun onPostExecute(result: Cursor?) {
                    super.onPostExecute(result)
                    it.notifyDataSetChanged()
                    result?.close()
                }

            }.execute()
        }
    }

    override fun onDestroy() {
        adapter?.close()
        super.onDestroy()
    }
}