package hr.foi.air.mycar.fragments

import android.os.Bundle
import android.telecom.Call.Details
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentTransaction
import hr.foi.air.mycar.R

class VehicleManagementFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_vehicle_management, container, false)
        val btnDetalji : Button = view.findViewById(R.id.btn_details)
        btnDetalji.setOnClickListener{
            val fragment = AboutVehicleFragment()
            val transaction = childFragmentManager.beginTransaction()
            transaction?.replace(R.id.fragment_vehicle_management_id, fragment)?.commit()
        }
        return view
    }
}