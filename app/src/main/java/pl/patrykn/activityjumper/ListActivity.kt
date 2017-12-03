package pl.patrykn.activityjumper
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_list.*
import pl.patrykn.activityjumper.components.AppItem
import pl.patrykn.activityjumper.components.GetAppItemList
import pl.patrykn.activityjumper.library.ListAdapter

class ListActivity : AppCompatActivity() {
    private val LOG_TAG = this::class.java!!.getSimpleName()

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
                    appName.setText(item.label)
                    packgeName.setText(item.packageName)
                    appIcon.setImageDrawable(item.getApplicationIcon(baseContext.packageManager))
                }

            }
        }
        adapter.onItemClickListener = { holder ->
            if ( holder.item != null ) {
                startActivity(baseContext.getPackageManager().getLaunchIntentForPackage(holder.item?.packageName))
            }
            true
        }
        list_activity__list.setAdapter(adapter)
    }
}