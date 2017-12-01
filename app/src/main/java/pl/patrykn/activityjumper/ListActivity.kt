package pl.patrykn.activityjumper

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_list.*
import pl.patrykn.activityjumper.library.ListAdapter


class ListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        list_activity__list.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        var adapter = object: ListAdapter<String>(null) {
            override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder<String> {
                return Holder(R.layout.activity_list_item, parent!!)
            }

            inner class Holder(view: View) : ViewHolder<String>(view) {
                constructor(layoutId: Int, parent: ViewGroup): super(layoutId, parent)

                override fun set(item: String) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

            }
        }
        list_activity__list.setAdapter(adapter)
    }
}
