package com.oxygen.validatio.rules

import android.util.Patterns
import android.view.View
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.oxygen.validatio.util.doAfterTextChanged

/**
 * @author Iamushev Igor
 * @since  2019-07-23
 */
fun TextView.isNotEmpty(
  onSuccess: ((String, View) -> Unit)? = null,
  onError: ((String, View) -> Unit)? = null
): LiveData<Boolean> {
  val subject = MutableLiveData<Boolean>().apply { value = false }
  this.doAfterTextChanged {
    subject.value = it.isNotEmpty()
    if (it.isNotEmpty()) onSuccess?.invoke(it, this) else onError?.invoke(it, this)
  }
  return subject
}

fun TextView.isValidEmail(
  onSuccess: ((String, View) -> Unit)? = null,
  onError: ((String, View) -> Unit)? = null
): LiveData<Boolean> {
  val subject = MutableLiveData<Boolean>().apply { value = false }
  this.doAfterTextChanged {
    val isValidEmail = it.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(it).matches()
    subject.value = isValidEmail
    if (isValidEmail) onSuccess?.invoke(it, this) else onError?.invoke(it, this)
  }
  return subject
}

fun TextView.textDiffersFromDefault(
  defaultString: String,
  onSuccess: ((String, View) -> Unit)? = null,
  onError: ((String, View) -> Unit)? = null
): LiveData<Boolean> {
  val subject = MutableLiveData<Boolean>().apply { value = false }
  this.doAfterTextChanged {
    val differsFromDefault = it.trim() != defaultString
    subject.value = differsFromDefault
    if (differsFromDefault) onSuccess?.invoke(it, this) else onError?.invoke(it, this)
  }
  return subject
}