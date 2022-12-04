package hr.foi.air.mycar

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class AboutVehicle : AppCompatActivity() {
    @SuppressLint("MissingInflatedId", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_vehicle)

        val btnBack = findViewById<Button>(R.id.btn_back)
        btnBack.setOnClickListener{
            val intent = Intent(this,VehicleManagement::class.java)
            startActivity(intent)
        }

        val btnWheel = findViewById<Button>(R.id.btn_wheel)
        btnWheel.setOnClickListener{
            val intent = Intent(this,VehicleManage::class.java)
            startActivity(intent)
        }

        val btnVehicleManage = findViewById<TextView>(R.id.btn_vehicle_manage)
        btnVehicleManage.setOnClickListener{
            val intent = Intent(this,VehicleManage::class.java)
            startActivity(intent)
        }
    }
}