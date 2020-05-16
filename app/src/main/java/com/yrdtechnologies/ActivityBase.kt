package com.yrdtechnologies

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Point
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import java.util.ArrayList

/**
 * Created by kartikshah on 28/06/15.
 */
@Suppress("DEPRECATION")
abstract class ActivityBase : AppCompatActivity() {
    //PS somehow getting resources as instance variable is creating memory issues,and app to crash
    //AlertDialog ad;
    private var loading: ProgressDialog? = null

    val isConnected: Boolean
        get() {
            val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo?.isConnected ?: false
        }

    fun showSoftInput(view: View?) {
        val mgrs = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        mgrs.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    fun showCustomMessage(message: String?) {
        if (loading != null) loading!!.dismiss()
        val adb = AlertDialog.Builder(this)
        adb.setTitle(this.resources.getString(R.string.app_name))
        adb.setMessage(message)
        adb.setPositiveButton("OK") { dialog, which -> dialog.dismiss() }
        adb.create().show()
    }

    fun showToast(message: String?) {
        val toast = Toast.makeText(this, message, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    fun resolveActivity(intent: Intent): Boolean {
        val info = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
        return info != null
    }

    fun putBoolean(key: String?, value: Boolean) {
        val preferencesName = this.resources.getString(R.string.app_name)
        val sharedPreferences = getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBoolean(key: String?, defaultValue: Boolean): Boolean {
        val preferencesName = this.resources.getString(R.string.app_name)
        val sharedPreferences = getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    fun putInt(key: String?, value: Int) {
        val preferencesName = this.resources.getString(R.string.app_name)
        val sharedPreferences = getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun getInt(key: String?, defaultValue: Int): Int {
        val preferencesName = this.resources.getString(R.string.app_name)
        val sharedPreferences = getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
        return sharedPreferences.getInt(key, defaultValue)
    }

    fun getString(key: String?, defaultValue: String?): String? {
        val preferencesName = this.resources.getString(R.string.app_name)
        val sharedPreferences = getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
        return sharedPreferences.getString(key, defaultValue)
    }

    operator fun contains(key: String?): Boolean {
        val preferencesName = this.resources.getString(R.string.app_name)
        val sharedPreferences = getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
        return sharedPreferences.contains(key)
    }

    protected fun log(message: String) {
        Log.e(this.javaClass.simpleName, "" + message)
    }
}