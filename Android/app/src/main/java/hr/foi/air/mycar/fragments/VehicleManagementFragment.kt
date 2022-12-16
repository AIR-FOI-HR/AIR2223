package hr.foi.air.mycar.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import hr.foi.air.mycar.AboutVehicleActivity
import hr.foi.air.mycar.R
import hr.foi.air.mycar.WarningActivity

class VehicleManagementFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_vehicle_management, container, false)

        val buttonDetails = view.findViewById<Button>(R.id.btn_details)
        val buttonWarnings = view.findViewById<Button>(R.id.btn_warnings)

        buttonDetails.setOnClickListener {
            val intentAboutVehicle = Intent(context, AboutVehicleActivity::class.java)
            startActivity(intentAboutVehicle)
        }

        buttonWarnings.setOnClickListener {
            val intentWarnings = Intent(context, WarningActivity::class.java)
            startActivity(intentWarnings)
        }

        return view
    }
}