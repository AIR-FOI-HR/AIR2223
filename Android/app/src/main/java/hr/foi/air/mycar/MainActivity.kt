package hr.foi.air.mycar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import hr.foi.air.mycar.adapters.PageAdapter
import hr.foi.air.mycar.fragments.HomeFragment
import hr.foi.air.mycar.fragments.MapFragment
import hr.foi.air.mycar.fragments.ProfileFragment
import hr.foi.air.mycar.fragments.VehicleManagementFragment

class MainActivity : AppCompatActivity() {
    private lateinit var viewPager : ViewPager2
    private lateinit var tabLayout : TabLayout

    fun createNavigation(){
        viewPager = findViewById(R.id.view_pager_home)
        tabLayout = findViewById(R.id.tabovi)
        val pagerAdapter = PageAdapter(supportFragmentManager,lifecycle)

        pagerAdapter.Add(PageAdapter.Item(R.string.title_home, R.drawable.ic_baseline_home_24, HomeFragment::class))
        pagerAdapter.Add(PageAdapter.Item(R.string.title_myCar, R.drawable.ic_baseline_directions_car_24, hr.foi.air.mycar.fragments.VehicleManagementFragment::class))
        pagerAdapter.Add(PageAdapter.Item(R.string.title_map, R.drawable.ic_baseline_map_24, MapFragment::class))
        pagerAdapter.Add(PageAdapter.Item(R.string.title_profile, R.drawable.ic_baseline_person_24, ProfileFragment::class))

        viewPager.adapter = pagerAdapter

        TabLayoutMediator(tabLayout, viewPager){
            tab,position->
            tab.setText(pagerAdapter.items[position].Title)
            tab.setIcon(pagerAdapter.items[position].Icon)
        }.attach()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNavigation()

        //test za provjeru navigacije
        /*
        val btnLogin = findViewById<Button>(R.id.btn_login)
        btnLogin.setOnClickListener{
            val intent = Intent(this,VehicleManagement::class.java)
            startActivity(intent)
        }*/
    }
    //testiranje
    //test15
    //test16
}