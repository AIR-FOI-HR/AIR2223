package hr.foi.air.mycar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class RegistrationActivity : AppCompatActivity() {

    private lateinit var inputImeIPrezime: EditText
    private lateinit var inputEmail: EditText
    private lateinit var inputKorisnickoIme: EditText
    private lateinit var inputLozinka: EditText
    private lateinit var btnRegistirajSe: Button
    private lateinit var requestQueue: RequestQueue
    private lateinit var url: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        inputImeIPrezime = findViewById(R.id.inputNameAndSurname)
        inputEmail = findViewById(R.id.inputEmail)
        inputKorisnickoIme = findViewById(R.id.inputUsername)
        inputLozinka = findViewById(R.id.inputPassword)
        btnRegistirajSe = findViewById(R.id.btnRegister)
        requestQueue = Volley.newRequestQueue(applicationContext);
        url = "https://mycar-db.000webhostapp.com/insertUser.php"

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

        //povratak na Home fragment
        val btnBack = findViewById<Button>(R.id.btnBackRegistration)
        btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btnRegistirajSe.setOnClickListener {

            if (inputImeIPrezime.text.toString() == "" || inputEmail.text.toString() == "" ||
                inputKorisnickoIme.text.toString() == "" || inputLozinka.text.toString() == ""
            ) {
                Toast.makeText(this, "Popunite sve podatke!", Toast.LENGTH_LONG).show()
            } else {
                val requestQueue = Volley.newRequestQueue(this)

                val jsonObject = JSONObject()
                jsonObject.put("ime_prezime", inputImeIPrezime.text.toString())
                jsonObject.put("email", inputEmail.text.toString())
                jsonObject.put("korisnicko_ime", inputKorisnickoIme.text.toString())
                jsonObject.put("lozinka", inputLozinka.text.toString())

                val jsonRequest = object : JsonObjectRequest(Method.POST, url, jsonObject,
                    Response.Listener { response ->
                        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                    },
                    Response.ErrorListener { error ->
                        //error
                    }) {
                }

                requestQueue.add(jsonRequest)

                inputImeIPrezime.setText("")
                inputEmail.setText("")
                inputKorisnickoIme.setText("")
                inputLozinka.setText("")
            }
        }
    }
}

