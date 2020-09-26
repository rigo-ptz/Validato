package com.oxygen.validatio

import androidx.lifecycle.*
import com.oxygen.validatio.util.combineLatest

/**
 * @author Yamushev Igor
 * @since  2019-07-23
 */
class ValidationObserver(
  private var lifecycleOwner: LifecycleOwner?,
  private val validators: List<LiveData<Boolean>>,
  private val skipFirstEvents: Boolean = false,
  private val fireIfAtLeastOneIsValid: Boolean = false,
  callback: ((Boolean) -> Unit)?
) : LifecycleObserver {

  private var lastResult: LiveData<Boolean>? = null

  private val observer = object : Observer<Boolean> {
    override fun onChanged(t: Boolean?) {
      callback?.invoke(t ?: return)
    }
  }

  init {
    lifecycleOwner?.lifecycle?.addObserver(this)
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_START)
  fun startValidate() {
    val owner = lifecycleOwner ?: return

    lastResult = validators.combineLatest(skipFirstEvents) { if (fireIfAtLeastOneIsValid) it.contains(true) else !it.contains(false) }

    lastResult?.observe(owner, observer)
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
  fun stopValidate() {
    lastResult?.removeObserver(observer)
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
  fun clear() {
    lifecycleOwner?.lifecycle?.removeObserver(this)
    lifecycleOwner = null
    lastResult = null
  }

  fun getLastResult() = lastResult?.value ?: false

}