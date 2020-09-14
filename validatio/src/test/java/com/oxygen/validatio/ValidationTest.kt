package com.oxygen.validatio

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.nhaarman.mockitokotlin2.*
import io.reactivex.subjects.BehaviorSubject
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mockito

/**
 * @author Yamushev Igor
 * @since  2019-07-24
 */
class ValidationTest {

  private lateinit var bs1: BehaviorSubject<Boolean>
  private lateinit var bs2: BehaviorSubject<Boolean>

  private lateinit var owner: LifecycleOwner
  private lateinit var lifecycleRegistry: LifecycleRegistry
  private lateinit var observerCallback: (Boolean) -> Unit

  @Captor private lateinit var callbackArgCaptor: ArgumentCaptor<Boolean>

  @Before
  fun buildValidators() {
    // Given
    // Validation rules
    bs1 = BehaviorSubject.createDefault(false)
    bs2 = BehaviorSubject.createDefault(false)

    // Lifecycle
    owner = mock()
    lifecycleRegistry = LifecycleRegistry(owner)
    whenever(owner.lifecycle).thenReturn(lifecycleRegistry)

    // Callback
    observerCallback = mock()

    callbackArgCaptor = ArgumentCaptor.forClass(Boolean::class.java)
  }

  @Test
  fun validator_created() {
    // Given
    ValidationObserver(
      owner,
      listOf(bs1, bs2),
      false,
      observerCallback
    )

    // When
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)

    // Then
    Mockito.verify(observerCallback).invoke(callbackArgCaptor.capture())

    assert(callbackArgCaptor.value == false)
  }

  @Test
  fun validator_pos_neg_input_negative() {
    // Given
    ValidationObserver(
      owner,
      listOf(bs1, bs2),
      false,
      observerCallback
    )

    // When
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
    bs1.onNext(true)

    // Then
    Mockito.verify(observerCallback, times(2)).invoke(callbackArgCaptor.capture())

    assert(
      callbackArgCaptor.allValues.size == 2 && !callbackArgCaptor.lastValue
    )
  }

  @Test
  fun validator_pos_pos_input_positive() {
    // Given
    ValidationObserver(
      owner,
      listOf(bs1, bs2),
      false,
      observerCallback
    )

    // When
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
    bs1.onNext(true)
    bs2.onNext(true)

    // Then
    Mockito.verify(observerCallback, times(3)).invoke(callbackArgCaptor.capture())

    assert(
      callbackArgCaptor.allValues.size == 3
          && !callbackArgCaptor.firstValue
          && !callbackArgCaptor.secondValue
          && callbackArgCaptor.thirdValue
    )

  }

  @Test
  fun validator_stopped() {
    // Given
    ValidationObserver(
      owner,
      listOf(bs1, bs2),
      false,
      observerCallback
    )

    // When
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
    bs1.onNext(true)
    bs2.onNext(true)

    // Then
    Mockito.verify(observerCallback, Mockito.times(1)).invoke(false)
  }


}