package hr.foi.air.mycar.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import hr.foi.air.mycar.R

class AboutVehicleFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_about_vehicle, container, false)
        val btnNazad : Button = view.findViewById(R.id.btn_back)
        btnNazad.setOnClickListener{
            val fragment = VehicleManagementFragment()
            val transaction = childFragmentManager?.beginTransaction()
            transaction?.replace(R.id.aboutVehicle_id,fragment)?.commit()
        }
        return view
    }
}