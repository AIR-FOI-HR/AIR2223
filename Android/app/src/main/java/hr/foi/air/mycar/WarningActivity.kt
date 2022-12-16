package hr.foi.air.mycar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class WarningActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_warning)

        val btnClose = findViewById<Button>(R.id.btn_close)
        btnClose.setOnClickListener{
            callMain()
        }

    }

    private fun callMain() {
        val intent = Intent(this, MainActivity::class.java).also {
            it.putExtra("ActivityIndex", "WarningActivity")
            startActivity(it)
        }
    }
}