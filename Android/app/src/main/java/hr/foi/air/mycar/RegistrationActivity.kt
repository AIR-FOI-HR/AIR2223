package hr.foi.air.mycar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText

class RegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        //password show - on/off
        val btnShowPassword = findViewById<Button>(R.id.btnShowPasswordRegistration)
        var textPassword = findViewById<EditText>(R.id.inputPassword)
        btnShowPassword.setOnClickListener {
            if (textPassword.inputType != 1) {
                textPassword.inputType = 1
            } else if (textPassword.inputType == 1) {
                textPassword.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }

        val btnBack = findViewById<Button>(R.id.btnBackRegistration)
        btnBack.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }
}
