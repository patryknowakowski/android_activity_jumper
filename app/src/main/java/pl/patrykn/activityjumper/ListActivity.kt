package pl.patrykn.activityjumper

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_list.*
import pl.patrykn.activityjumper.library.ListAdapter

class ListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        list_activity__list.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        var adapter = object: ListAdapter<String>(null) {
            override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder<String> {
                return Holder(R.layout.activity_list_item, parent)
            }

            inner class Holder(layoutId: Int, parent: ViewGroup?): ViewHolder<String>(baseContext, layoutId, parent) {
                val appName: TextView = findViewById(R.id.list_activity_item__name)

                override fun set(item: String) {
                    appName.setText(item)
                }

            }
        }
        list_activity__list.setAdapter(adapter)
    }
}