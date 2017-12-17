package pl.patrykn.activityjumper.components

import android.annotation.SuppressLint
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.util.Log
import io.mironov.smuggler.AutoParcelable

/**
 * Created by patrykn on 30.11.17.
 */

@SuppressLint("ParcelCreator")
data class AppItem constructor
    ( val label: String
    , val packageName: String
    , val category: Int
    , val flags: Int
    , val minSdkVersion: Int
    , val permission: String?
    , val processName: String
    , val targetSdkVersion: Int
    , val taskAffinity: String?
    , val theme: Int
    , val uid: Int ): AutoParcelable {
    constructor(ai: ApplicationInfo, label: CharSequence): this
    ( label.toString()
    , ai.packageName
    , ai.category
    , ai.flags
    , ai.minSdkVersion
    , ai.permission
    , ai.processName
    , ai.targetSdkVersion
    , ai.taskAffinity
    , ai.theme
    , ai.uid )

    fun getApplicationIcon(packageManager: PackageManager): Drawable {
        return packageManager.getApplicationIcon(this)
    }
}

fun PackageManager.GetAppItemList(flag: Int = 0): List<AppItem> {
    val list = ArrayList<AppItem>()
    for ( pack in this.getInstalledPackages(flag) ) {
        Log.d("P0", "Pack ${pack.packageName}")
        pack.activities?.forEach {
            Log.d("P1", "Activity ${it.name}, parent ${it.parentActivityName?.toString() ?: "-"}")
        }
        pack.permissions?.forEach {
            Log.d("P2", "Permissions ${it.name}, descriptionRes ${it.descriptionRes}")
        }

        list.add(AppItem(pack.applicationInfo, this.getApplicationLabel(pack.applicationInfo)))
        //list.add(AppItem(pack, this.getApplicationLabel(pack)));
    }

    return list
}

fun PackageManager.getApplicationIcon(appItem: AppItem): Drawable {
    return this.getApplicationIcon(appItem.packageName)
}