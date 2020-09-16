package com.oxygen.validato

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.oxygen.validatio.ValidationObserver
import com.oxygen.validatio.rules.isNotEmpty
import com.oxygen.validatio.rules.isValidEmail

/**
 * @author Yamushev Igor
 * @since  2019-07-23
 */
class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val name = findViewById<TextView>(R.id.name)
    val surname = findViewById<TextView>(R.id.surname)
    val email = findViewById<TextView>(R.id.email)
    val btn = findViewById<TextView>(R.id.btn)

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
      btn.isEnabled = it
    }
  }

}