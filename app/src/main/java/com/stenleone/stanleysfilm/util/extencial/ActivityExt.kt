package com.stenleone.stanleysfilm.util.extencial

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.stenleone.stanleysfilm.R
import com.stenleone.stanleysfilm.ui.activity.base.BaseActivity

fun Activity.copyToClipBoard(copyText: String?) {
    val clipboard = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(BaseActivity.COPY_LABEL, copyText)
    clipboard.setPrimaryClip(clip)
    Toast.makeText(this, getString(R.string.content_copy_to_clip_board), Toast.LENGTH_SHORT).show()
}

fun Activity.getOrientation(): Int {
    return if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
        Configuration.ORIENTATION_PORTRAIT
    } else {
        Configuration.ORIENTATION_LANDSCAPE
    }
}

fun Activity.hideStatusBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.insetsController?.hide(WindowInsets.Type.statusBars())
    } else {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }
}

fun Activity.showStatusBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.setDecorFitsSystemWindows(false)
    } else {
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    }
}

fun Activity.hideSystemButtons() {
    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
}

fun Activity.showSystemButtons() {
    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
}

fun Activity.hideKeyboard() {
    val imm: InputMethodManager = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    var view: View? = this.getCurrentFocus()
    if (view == null) {
        view = View(this)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}