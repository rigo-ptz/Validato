package com.oxygen.validatio.rules

import android.widget.TextView
import com.oxygen.validatio.util.doAfterTextChanged
import io.reactivex.subjects.BehaviorSubject

/**
 * @author Yamushev Igor
 * @since  2019-07-23
 */
fun TextView.isNotEmpty(
  onError: ((Boolean) -> Unit)? = null,
  onSuccess: ((String) -> Unit)? = null
): BehaviorSubject<Boolean> {
  val subject = BehaviorSubject.createDefault(false)
  this.doAfterTextChanged {
    subject.onNext(it.isNotEmpty())
    if (it.isNotEmpty()) onSuccess?.invoke(it)
  }
  return subject
}

fun TextView.textDiffersFromDefault(
  defaultString: String,
  onError: (() -> Unit)? = null,
  onSuccess: ((String) -> Unit)? = null
): BehaviorSubject<Boolean> {
  val subject = BehaviorSubject.createDefault(false)
  this.doAfterTextChanged {
    val differsFromDefault = it.trim() != defaultString
    subject.onNext(differsFromDefault)
    if (differsFromDefault) onSuccess?.invoke(it) else onError?.invoke()
  }
  return subject
}