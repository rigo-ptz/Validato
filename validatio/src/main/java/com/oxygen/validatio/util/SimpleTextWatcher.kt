package com.oxygen.validatio.util

import android.text.Editable
import android.text.TextWatcher

/**
 * @author Yamushev Igor
 * @since  2019-07-24
 */
abstract class SimpleTextWatcher : TextWatcher {

  override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) { }

  override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) { }

  override fun afterTextChanged(s: Editable) { }

}