package pl.patrykn.activityjumper

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_app.*
import kotlinx.android.synthetic.main.content_app.*
import pl.patrykn.activityjumper.components.AppItem
import pl.patrykn.activityjumper.library.IntentCompanion
import pl.patrykn.activityjumper.library.IntentExtra

class AppActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app)
        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onResume() {
        super.onResume()
        intentCompanion.params(this) {
            setTitle(it.app?.label)
            content_app.setText(it.app?.toString())
        }
    }

    object intentCompanion: IntentCompanion<intentExtra>(AppActivity::class, intentExtra)

    object intentExtra : IntentExtra() {
        var Intent.app by ExtraParcelable<AppItem>()
    }
}
