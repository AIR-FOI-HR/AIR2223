package hr.foi.air.mycar

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.JsonObject
import org.json.JSONArray
import org.json.JSONObject
import java.lang.reflect.Method


class LoginActivity : AppCompatActivity() {

    private lateinit var inputKorisnickoIme: EditText
    private lateinit var inputLozinka: EditText
    private lateinit var url: String
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        inputKorisnickoIme = findViewById(R.id.inputUsernameLogin)
        inputLozinka = findViewById(R.id.inputPasswordLogin)
        url = "https://mycar-db.000webhostapp.com/login.php"
        btnLogin = findViewById(R.id.btnLogin)

        //password show - on/off
        val btnShowPassword = findViewById<Button>(R.id.btnShowPassword)
        var textPassword = findViewById<EditText>(R.id.inputPasswordLogin)
        btnShowPassword.setOnClickListener {
            if (textPassword.inputType != 1) {
                textPassword.inputType = 1
            } else if (textPassword.inputType == 1) {
                textPassword.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }

        //povratak na Home fragment
        val btnBack = findViewById<Button>(R.id.btnBackLogin)
        btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val korisnikObject = JSONObject()
        korisnikObject.put("korisnicko_ime", inputKorisnickoIme.text)
        korisnikObject.put("lozinka", inputLozinka.text)

        btnLogin.setOnClickListener {
            if (inputKorisnickoIme.text.toString() == "" || inputLozinka.text.toString() == "") {
                Toast.makeText(this, "Popunite sve podatke!", Toast.LENGTH_LONG).show()
            } else {
                val request = JsonObjectRequest(Request.Method.POST, url, korisnikObject,
                    { response ->
                        // handle the response
                        if(response.get("success") == true){
                            Toast.makeText(applicationContext, "Uspješna prijava!", Toast.LENGTH_SHORT).show()
                            callMain()
                        }else{
                            Toast.makeText(applicationContext, "Neuspješna prijava!", Toast.LENGTH_SHORT).show()
                        }
                        println(response)
                    },
                    { error ->
                        // handle the error
                        Toast.makeText(applicationContext, "Neuspjela veza!", Toast.LENGTH_SHORT)
                            .show()
                        error.printStackTrace()
                    }
                )
                val queue = Volley.newRequestQueue(this)
                queue.add(request)
            }
        }

    }

    private fun callMain() {
        val intent = Intent(this, MainActivity::class.java).also {
            it.putExtra("ActivityIndex", "LoginActivity")
            startActivity(it)
        }
    }
}