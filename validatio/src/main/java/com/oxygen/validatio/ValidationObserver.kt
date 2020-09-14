package com.oxygen.validatio

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject

/**
 * @author Yamushev Igor
 * @since  2019-07-23
 */
class ValidationObserver(
  private var lifecycleOwner: LifecycleOwner?,
  private val validators: List<BehaviorSubject<Boolean>>,
  private val skipFirst: Boolean = false,
  private val callback: (Boolean) -> Unit
) : LifecycleObserver {

  private var validationDisposable: Disposable? = null

  init {
    lifecycleOwner?.lifecycle?.addObserver(this)
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_START)
  fun startValidate() {
    validationDisposable = Observable
      .combineLatest(validators) { !it.contains(false) }
      .publish { Observable.merge(it.take(1).filter { !skipFirst }, it.skip(1)) }
      .subscribe { callback.invoke(it) }
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
  fun stopValidate() {
    validationDisposable?.takeUnless { it.isDisposed }?.dispose()
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
  fun clear() {
    lifecycleOwner?.lifecycle?.removeObserver(this)
    lifecycleOwner = null
  }

}