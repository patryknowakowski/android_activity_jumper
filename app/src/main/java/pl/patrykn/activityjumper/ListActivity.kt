package pl.patrykn.activityjumper
import android.content.Intent
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
import pl.patrykn.activityjumper.library.IntentExtraString
import pl.patrykn.activityjumper.library.ListAdapter
import pl.patrykn.activityjumper.library.LogDebug


class ListActivity : AppCompatActivity() {
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

    override fun onResume() {
        super.onResume()
        LogDebug("Message = ${intent.message}")
        LogDebug("Message2 = ${intent.message2}")
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        var intent = Intent(this, ListActivity::class.java)
        intent.message = "test"
        intent.message2 = "test2"
        startActivity(intent)
        return true

        /*when(item?.itemId) {
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
        }*/
    }

    companion object IntentOptions {
        var Intent.message by IntentExtraString("message-fix")
        var Intent.message2 by IntentExtraString()
    }
}