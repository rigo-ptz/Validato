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
  private val skipFirst: Boolean = false,
  callback: (Boolean) -> Unit
) : LifecycleObserver {

  private var results: LiveData<Boolean>? = null
  private val observer = object : Observer<Boolean> {
    override fun onChanged(t: Boolean?) {
      callback.invoke(t ?: return)
    }
  }

  init {
    lifecycleOwner?.lifecycle?.addObserver(this)
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_START)
  fun startValidate() {
    val owner = lifecycleOwner ?: return

    results = validators.combineLatest(skipFirst) { !it.contains(false) }

    results?.observe(owner, observer)
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
  fun stopValidate() {
    results?.removeObserver(observer)
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
  fun clear() {
    lifecycleOwner?.lifecycle?.removeObserver(this)
    lifecycleOwner = null
    results = null
  }

}