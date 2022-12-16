package hr.foi.air.mycar

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2

class AboutVehicleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_vehicle)

        val btnBack = findViewById<Button>(R.id.btn_back)
        btnBack.setOnClickListener {
            callMain()
        }
    }

    private fun callMain() {
        val intent = Intent(this, MainActivity::class.java).also {
            it.putExtra("ActivityIndex", "AboutVehicleActivity")
            startActivity(it)
        }
    }
}