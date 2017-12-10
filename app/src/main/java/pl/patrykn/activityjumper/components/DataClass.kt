package pl.patrykn.activityjumper.components

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable

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
    , val uid: Int ): Parcelable {
    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.let {
            it.writeString(label.toString())
            it.writeString(packageName)
            it.writeInt(category)
            it.writeInt(flags)
            it.writeInt(minSdkVersion)
            it.writeString(permission)
            it.writeString(processName)
            it.writeInt(targetSdkVersion)
            it.writeString(taskAffinity)
            it.writeInt(theme)
            it.writeInt(uid)
        }
    }

    override fun describeContents(): Int = 0

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt()) {
    }

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

    companion object CREATOR : Parcelable.Creator<AppItem> {
        override fun createFromParcel(parcel: Parcel): AppItem {
            return AppItem(parcel)
        }

        override fun newArray(size: Int): Array<AppItem?> {
            return arrayOfNulls(size)
        }
    }
}

fun PackageManager.GetAppItemList(flag: Int = 0): List<AppItem> {
    val list = ArrayList<AppItem>()
    for ( app in this.getInstalledApplications(flag) ) {
        val item =  AppItem(app, this.getApplicationLabel(app))
        if ( !item.packageName.startsWith("com.android.") && !item.packageName.startsWith("com.google.android.") ) {
            list.add(item);
        }
    }

    return list
}

fun PackageManager.getApplicationIcon(appItem: AppItem): Drawable {
    return this.getApplicationIcon(appItem.packageName)
}