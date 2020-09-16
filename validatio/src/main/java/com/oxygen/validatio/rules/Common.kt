package com.oxygen.validatio.rules

import android.util.Patterns
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.oxygen.validatio.util.doAfterTextChanged

/**
 * @author Iamushev Igor
 * @since  2019-07-23
 */
fun TextView.isNotEmpty(
  onError: ((Boolean) -> Unit)? = null,
  onSuccess: ((String) -> Unit)? = null
): LiveData<Boolean> {
  val subject = MutableLiveData<Boolean>().apply { value = false }
  this.doAfterTextChanged {
    subject.value = it.isNotEmpty()
    if (it.isNotEmpty()) onSuccess?.invoke(it)
  }
  return subject
}

fun TextView.isValidEmail(
  onError: ((Boolean) -> Unit)? = null,
  onSuccess: ((String) -> Unit)? = null
): LiveData<Boolean> {
  val subject = MutableLiveData<Boolean>().apply { value = false }
  this.doAfterTextChanged {
    subject.value = it.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(it).matches()
    if (it.isNotEmpty()) onSuccess?.invoke(it)
  }
  return subject
}

fun TextView.textDiffersFromDefault(
  defaultString: String,
  onError: (() -> Unit)? = null,
  onSuccess: ((String) -> Unit)? = null
): LiveData<Boolean> {
  val subject = MutableLiveData<Boolean>().apply { value = false }
  this.doAfterTextChanged {
    val differsFromDefault = it.trim() != defaultString
    subject.value = differsFromDefault
    if (differsFromDefault) onSuccess?.invoke(it) else onError?.invoke()
  }
  return subject
}