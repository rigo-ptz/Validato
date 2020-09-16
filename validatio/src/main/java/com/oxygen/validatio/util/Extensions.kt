package com.oxygen.validatio.util

import android.text.Editable
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

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

fun <T, R> List<LiveData<T>>.combineLatest(skipFirstEvents: Boolean = false, combiner: (List<T?>) -> R): LiveData<R> {
  val result = this@combineLatest.map { it.value }.toMutableList()
  val skippedFirstEvents = result.map { false }.toMutableList()
  var addedSources = 0

  val mediator = MediatorLiveData<R>()

  forEachIndexed { index, item ->
    mediator.addSource(item) {
      result[index] = it
      if (addedSources != this@combineLatest.size) return@addSource

      if (skipFirstEvents && !skippedFirstEvents[index]) {
        skippedFirstEvents[index] = true
      } else {
        mediator.value = combiner.invoke(result)
      }
    }
    addedSources += 1
  }
  return  mediator
}
