package com.stenleone.stanleysfilm.util.extencial

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import com.stenleone.stanleysfilm.R
import com.stenleone.stanleysfilm.ui.activity.base.BaseActivity

fun Activity.copyToClipBoard(copyText: String?) {
    val clipboard = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(BaseActivity.COPY_LABEL, copyText)
    clipboard.setPrimaryClip(clip)
    Toast.makeText(this, getString(R.string.content_copy_to_clip_board), Toast.LENGTH_SHORT).show()
}