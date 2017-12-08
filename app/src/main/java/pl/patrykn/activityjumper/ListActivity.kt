package pl.patrykn.activityjumper
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_list.*
import pl.patrykn.activityjumper.components.AppItem
import pl.patrykn.activityjumper.components.GetAppItemList
import pl.patrykn.activityjumper.library.AsyncDialogProgress
import pl.patrykn.activityjumper.library.ListAdapter


class ListActivity : AppCompatActivity() {
    private val LOG_TAG = this::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        list_activity__list.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        val adapter = object: ListAdapter<AppItem>(baseContext.packageManager.GetAppItemList()) {
            override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder<AppItem> {
                return Holder(R.layout.activity_list_item, parent)
            }

            inner class Holder(layoutId: Int, parent: ViewGroup?): ViewHolder<AppItem>(baseContext, layoutId, parent) {
                val appName: TextView = findViewById(R.id.list_activity_item__name)
                val appIcon: ImageView = findViewById(R.id.list_activity_item__icon)
                val packgeName: TextView = findViewById(R.id.list_activity_item__package)

                override fun set(item: AppItem) {
                    appName.text = item.label
                    packgeName.text = item.packageName
                    appIcon.setImageDrawable(item.getApplicationIcon(baseContext.packageManager))
                }

            }
        }
        adapter.onItemClickListener = {
            it.item?.let { item ->
                startActivity(baseContext.packageManager.getLaunchIntentForPackage(item.packageName))
                true
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
                        try {
                            for ( i in 1..5) {
                                Thread.sleep(1000)
                                publishProgress("Test $i")
                            }
                        } catch (e: InterruptedException) {
                        }

                        return Unit
                    }

                }.execute()
                return true
            }
            else -> return super.onContextItemSelected(item)
        }
    }
}