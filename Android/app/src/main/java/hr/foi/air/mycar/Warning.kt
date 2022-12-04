package hr.foi.air.mycar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Warning : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_warning)

        val btnClose = findViewById<Button>(R.id.btn_close)
        btnClose.setOnClickListener{
            val intent = Intent(this,VehicleManagement::class.java)
            startActivity(intent)
        }

    }
}