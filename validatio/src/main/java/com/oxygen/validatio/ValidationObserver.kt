package com.oxygen.validatio

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.BehaviorSubject

/**
 * @author Yamushev Igor
 * @since  2019-07-23
 */
class ValidationObserver(
  private var lifecycleOwner: LifecycleOwner?,
  private val validators: List<BehaviorSubject<Boolean>>,
  private val callback: (Boolean) -> Unit
) : LifecycleObserver {

  private val compositeDisposable = CompositeDisposable()

  init {
    lifecycleOwner?.lifecycle?.addObserver(this)
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_START)
  @SuppressWarnings("unused")
  fun startValidate() {
    validate(validators, callback)
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
  @SuppressWarnings("unused")
  fun stopValidate() {
    lifecycleOwner?.lifecycle?.removeObserver(this)
    compositeDisposable.clear()
    lifecycleOwner = null
  }

  private fun validate(validators: List<BehaviorSubject<Boolean>>, callback: (Boolean) -> Unit) {
    Observable
      .combineLatest(validators) { !it.contains(false) }
      .subscribe { callback.invoke(it) }
      .addTo(compositeDisposable)
  }

}