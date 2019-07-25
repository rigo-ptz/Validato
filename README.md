# Validato

Validato is Android real-time validation library. It automatically starts validation when 
lifecycle owner is started and stops when owner is stopped.

# How to use
Usual usage in activity or in presenter looks like this: 

```kotlin
val validators = mutableListOf<BehaviorSubject<Boolean>>().apply {
  add(editText.isDateOfBirthValid())
  add(anotherEditText.isNotEmpty())
}

ValidationObserver(this, validators) { isValid ->
   // Do something here
   submitButton.enabled = isValid
}
```

where, for example, "isNotEmpty"

```kotlin
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
```

# Validation rules
Just create behavior subject and put it into the list!

