package de.app.ui.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import de.app.api.account.AccountInfo
import de.app.core.AccountManager
import de.app.core.successOrElse
import de.app.ui.user.LoginActivity
import java.util.*


fun Context.toast(text: String, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, text, duration).show()
}


fun Activity.runActivity(cls: Class<*>, bundle: Bundle? = null){
    val intent = Intent(this, cls)
    bundle?.let {
     intent.putExtras(bundle)
    }
    startActivity(intent)

    this.setResult(Activity.RESULT_OK)
    this.finish()
}

fun setLanguage(list: String){
    val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(list)
    AppCompatDelegate.setApplicationLocales(appLocale)
}

fun Context.geoDecode(location: Location): Result<List<Address>> {
    return Geocoder(this, Locale.GERMANY)
        .getFromLocation(location.latitude, location.longitude, 1)
        .successOrElse()
}


inline fun AccountManager.getCurrentAccountOrRequireLogin(activity: Activity, recovery: () -> (AccountInfo)): AccountInfo {
    return getCurrentAccount().getOrElseRequireLogin(activity, recovery)
}

inline fun Result<AccountInfo>.getOrElseRequireLogin(activity: Activity, recovery: () -> (AccountInfo)): AccountInfo {
    onFailure {
        activity.toast("Failed to get current account: "+it.message)
        activity.runActivity(LoginActivity::class.java)
    }
    return getOrDefault(recovery())
}