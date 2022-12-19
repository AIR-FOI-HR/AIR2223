package hr.foi.air.mycar

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import hr.foi.air.controls.VehicleManageActivity

class AboutVehicleActivity : AppCompatActivity() {

    private lateinit var buttonWheel : ImageView
    private lateinit var buttonText : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_vehicle)

        val btnBack = findViewById<Button>(R.id.btn_back)
        btnBack.setOnClickListener {
            callMain()
        }

        buttonWheel = findViewById(R.id.btn_wheel)
        buttonWheel.setOnClickListener{
            val intent = Intent(this,VehicleManageActivity::class.java)
            startActivity(intent)
        }

        buttonText = findViewById(R.id.btn_vehicle_manage)
        buttonText.setOnClickListener{
            val intent = Intent(this,VehicleManageActivity::class.java)
            startActivity(intent)
        }
    }

    private fun callMain() {
        val intent = Intent(this, MainActivity::class.java).also {
            it.putExtra("ActivityIndex", "AboutVehicleActivity")
            startActivity(it)
        }
    }
}