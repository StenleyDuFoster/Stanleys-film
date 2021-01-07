package com.stenleone.stanleysfilm.ui.adapter.binding

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.stenleone.stanleysfilm.R

@BindingAdapter("app:budget")
fun budget(view: TextView, text: String?) {

    text?.let {
        var newText = String()
        val reversText = it.reversed()
        var timerTo = 1

        reversText.forEachIndexed { index, char ->
            newText += char
            if (timerTo > 2 && index != reversText.length -1) {
                newText += "."
                timerTo = 0
            }

            timerTo++
        }
        view.text = if (newText == "0") {
            view.context.getString(R.string.no_info)
        } else {
            newText.reversed()
        }
    }
}
