package pl.patrykn.activityjumper.components

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo

/**
 * Created by patrykn on 30.11.17.
 */

data class AppItem(val appName: String, val packageName: String) {
    constructor(packageManager: PackageManager, resolveInfo: ResolveInfo): this(packageManager.getLabel(resolveInfo), resolveInfo.getPackageName())

    companion object {
        fun PackageManager.getLabel(resolveInfo: ResolveInfo): String {
            return resolveInfo.loadLabel(this).toString()
        }

        fun ResolveInfo.getPackageName(): String {
            return this.activityInfo.packageName
        }

        fun PackageManager.getInstalledPackages(): List<PackageInfo> {
            return this.getInstalledPackages(0) ?: ArrayList<PackageInfo>()
        }
    }
}