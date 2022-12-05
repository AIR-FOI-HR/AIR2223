package hr.foi.air.mycar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class VehicleManage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_manage)

        val btnClose = findViewById<Button>(R.id.btn_close)
        btnClose.setOnClickListener{
            val intent = Intent(this,AboutVehicle::class.java)
            startActivity(intent)
        }
    }
}