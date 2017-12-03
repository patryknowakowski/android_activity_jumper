package pl.patrykn.activityjumper.components

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable

/**
 * Created by patrykn on 30.11.17.
 */

data class AppItem private constructor
    ( val label: CharSequence
    , val packageName: String
    , val category: Int
    , val flags: Int
    , val minSdkVersion: Int
    , val permission: String?
    , val processName: String
    , val targetSdkVersion: Int
    , val taskAffinity: String?
    , val theme: Int
    , val uid: Int ) {
    constructor(ai: ApplicationInfo, label: CharSequence): this
    ( label
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
    for ( app in this.getInstalledApplications(flag) ) {
        list.add(AppItem(app, this.getApplicationLabel(app)));
    }

    return list
}

fun PackageManager.getApplicationIcon(appItem: AppItem): Drawable {
    return this.getApplicationIcon(appItem.packageName)
}