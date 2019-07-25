package com.oxygen.validatio.util

import android.text.Editable
import android.widget.TextView

/**
 * @author Yamushev Igor
 * @since  2019-07-24
 */
fun TextView.doAfterTextChanged(block: (s: String) -> Unit) {
  this.addTextChangedListener(object : SimpleTextWatcher() {
    override fun afterTextChanged(s: Editable) {
      block.invoke(s.toString())
    }
  })
}