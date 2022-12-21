package hr.foi.air.mycar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView

class WarningActivity : AppCompatActivity() {

    var array = arrayOf("Nizak tlak u gumama!", "Niska razina goriva!", "Niski tlak ulja!")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_warning)

        val btnClose = findViewById<Button>(R.id.btn_close)
        btnClose.setOnClickListener{
            callMain()
        }
        /*
        // use arrayadapter and define an array
        val arrayAdapter: ArrayAdapter<*>
        val warnings = arrayOf(
            "Virat Kohli", "Rohit Sharma", "Steve Smith",
            "Kane Williamson", "Ross Taylor"
        )

        // access the listView from xml file
        var mListView = findViewById<ListView>(R.id.warningList)
        arrayAdapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, warnings)
        mListView.adapter = arrayAdapter*/

        val adapter = ArrayAdapter(this,
            R.layout.listview_item, array)

        val listView:ListView = findViewById(R.id.warningList)
        listView.setAdapter(adapter)

    }

    private fun callMain() {
        val intent = Intent(this, MainActivity::class.java).also {
            it.putExtra("ActivityIndex", "WarningActivity")
            startActivity(it)
        }
    }
}