package com.oxygen.validatio

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.*
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

/**
 * @author Yamushev Igor
 * @since  14.9.2020
 */
@RunWith(MockitoJUnitRunner::class)
class ValidationTestLiveData {

  private lateinit var rule1: MutableLiveData<Boolean>
  private lateinit var rule2: MutableLiveData<Boolean>
  private lateinit var rule3: MutableLiveData<Boolean>

  private lateinit var owner: LifecycleOwner
  private lateinit var lifecycleRegistry: LifecycleRegistry
  private lateinit var observerCallback: (Boolean) -> Unit

  private val callbackArgCaptor = argumentCaptor<Boolean>()

  @get:Rule
  var instantExecutorRule = InstantTaskExecutorRule()

  @Before
  fun buildValidators() {
    // Given
    // Validation rules
    rule1 = MutableLiveData<Boolean>().apply { value = false }
    rule2 = MutableLiveData<Boolean>().apply { value = false }
    rule3 = MutableLiveData<Boolean>().apply { value = false }

    // Lifecycle
    owner = mock()
    lifecycleRegistry = LifecycleRegistry(owner)
    whenever(owner.lifecycle).thenReturn(lifecycleRegistry)

    // Callback
    observerCallback = mock()
  }

  @Test
  fun `ValidationObserver - created, don't skip first values` () {
    // Given
    ValidationObserver(
      owner,
      listOf(rule1, rule2),
      false,
      observerCallback
    )

    // When
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)

    // Then
    Mockito.verify(observerCallback, times(2)).invoke(callbackArgCaptor.capture())

    assert(!callbackArgCaptor.firstValue)
    assert(!callbackArgCaptor.secondValue)
  }

  @Test
  fun `ValidationObserver - created, skip first values` () {
    // Given
    ValidationObserver(
      owner,
      listOf(rule1, rule2),
      true,
      observerCallback
    )

    // When
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)

    // Then
    Mockito.verify(observerCallback, times(0)).invoke(callbackArgCaptor.capture())
  }

  @Test
  fun `positive-negative input, result is negative`() {
    // Given
    ValidationObserver(
      owner,
      listOf(rule1, rule2),
      false,
      observerCallback
    )

    // When
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
    rule1.value = true

    // Then
    Mockito.verify(observerCallback, times(3)).invoke(callbackArgCaptor.capture())

    assert(
      callbackArgCaptor.allValues.size == 3 && !callbackArgCaptor.lastValue
    )
  }

  @Test
  fun `positive-positive input, result is positive`() {
    // Given
    ValidationObserver(
      owner,
      listOf(rule1, rule2),
      false,
      observerCallback
    )

    // When
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
    rule1.value = true
    rule2.value = true

    // Then
    Mockito.verify(observerCallback, times(4)).invoke(callbackArgCaptor.capture())

    assert(
      callbackArgCaptor.allValues.size == 4
          && !callbackArgCaptor.thirdValue
          && callbackArgCaptor.lastValue
    )
  }

  @Test
  fun `lifecycle stopped`() {
    // Given
    ValidationObserver(
      owner,
      listOf(rule1, rule2),
      false,
      observerCallback
    )

    // When
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
    rule1.value = true
    rule2.value = true

    // Then
    Mockito.verify(observerCallback, times(2)).invoke(false)
  }

}