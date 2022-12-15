package hr.foi.air.mycar.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import hr.foi.air.mycar.LoginActivity
import hr.foi.air.mycar.R
import hr.foi.air.mycar.RegistrationActivity

class HomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val buttonLogin = view.findViewById<Button>(R.id.btn_login)

        buttonLogin.setOnClickListener {

            val intent = Intent(context, LoginActivity::class.java)

        val buttonRegistration = view.findViewById<Button>(R.id.button)

        buttonRegistration.setOnClickListener {

            val intentReg = Intent(context, RegistrationActivity::class.java)


            startActivity(intentReg)
        }
        return view
    }
}