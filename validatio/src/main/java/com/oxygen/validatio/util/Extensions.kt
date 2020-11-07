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
  val latestValues = mutableMapOf<LiveData<T>, T>()
  val combineIfNeeded: (MediatorLiveData<R>) -> Unit = { mediatorLiveData ->
    if (!skipFirstEvents || (skipFirstEvents && latestValues.size == this.size)) {
      mediatorLiveData.value = combiner.invoke(latestValues.values.toList())
    }
  }
  val mediator = MediatorLiveData<R>()

  forEach { item ->
    mediator.addSource(item) {
      latestValues[item] = it
      combineIfNeeded(mediator)
    }
  }

  return  mediator
}

