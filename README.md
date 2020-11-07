[![](https://jitpack.io/v/rigo-ptz/Validato.svg)](https://jitpack.io/#rigo-ptz/Validato)


# Validato

Validato is Android real-time validation library. It automatically starts validation when 
lifecycle owner is started and stops when owner is stopped.

# How to add to your project

Add repository to your root-level build.gradle
```kotlin
allprojects {
      repositories {
            ...
            maven { url 'https://jitpack.io' }
      }
}

```

Add the dependency (version = 1.1.1)
```kotlin
dependencies {
      implementation 'com.github.rigo-ptz:Validato:Version'
}

```

# How to use
Usual usage in activity or in presenter looks like this: 

```kotlin
val validators = listOf(
      name.isNotEmpty(),
      surname.isNotEmpty(),
      email.isValidEmail()
    )

ValidationObserver(
  lifecycleOwner = this,
  validators = validators,
  skipFirstEvents = false
) {
  // Do something here
  btn.isEnabled = it
}

```

where, for example, "isNotEmpty"

```kotlin
fun TextView.isNotEmpty(
  onError: ((String, View) -> Unit)? = null,
  onSuccess: ((String, View) -> Unit)? = null
): LiveData<Boolean> {
  val subject = MutableLiveData<Boolean>().apply { value = false }
  this.doAfterTextChanged {
    subject.value = it.isNotEmpty()
    if (it.isNotEmpty()) onSuccess?.invoke(it, this) else onError?.invoke(it, this)
  }
  return subject
}
```

# Validation rules
Create method (or extension function) that returns LiveData<Boolean> and put it into the list!

