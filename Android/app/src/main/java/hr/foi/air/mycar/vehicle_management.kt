package hr.foi.air.mycar

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import hr.foi.air.mycar.databinding.ActivityVehicleManagementBinding

class VehicleManagement : AppCompatActivity() {

    private lateinit var binding: ActivityVehicleManagementBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVehicleManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_vehicle_management)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.myCar, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val btnDetails = findViewById<Button>(R.id.btn_details)
        btnDetails.setOnClickListener{
            val intent = Intent(this,AboutVehicle::class.java)
            startActivity(intent)
        }

        val btnWarnings = findViewById<Button>(R.id.btn_warnings)
        btnWarnings.setOnClickListener{
            val intent = Intent(this,Warning::class.java)
            startActivity(intent)
        }
    }
}