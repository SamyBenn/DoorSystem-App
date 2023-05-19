package door.system

import ApiReq
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.SimpleAdapter
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray

class LogsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logs)

        val btnDoor = findViewById<ImageButton>(R.id.btnDoor)
        val btnSettings = findViewById<ImageButton>(R.id.btnSettings)
        val btnLogs = findViewById<ImageButton>(R.id.btnLogs)
        btnDoor.setOnClickListener {
            val doorAct = Intent(this, DoorActivity::class.java)
            startActivity(doorAct)
        }
        btnSettings.setOnClickListener {
            val settingsAct = Intent(this, SettingsActivity::class.java)
            startActivity(settingsAct)
        }
        btnLogs.setOnClickListener {
            val logsAct = Intent(this, LogsActivity::class.java)
            startActivity(logsAct)
        }

        val scrollView = findViewById<ScrollView>(R.id.logScrollView)
        val tableLayout = TableLayout(this)
        tableLayout.layoutParams = TableLayout.LayoutParams(
            TableLayout.LayoutParams.MATCH_PARENT,
            TableLayout.LayoutParams.WRAP_CONTENT
        )
        val headerRow = TableRow(this)

        val doorNameHeader = TextView(this)
        doorNameHeader.text = "Door Name"
        doorNameHeader.textSize = 24f
        doorNameHeader.gravity = Gravity.CENTER
        doorNameHeader.setPadding(0, 16, 8, 16)
        headerRow.addView(doorNameHeader)

        val carduidHeader = TextView(this)
        carduidHeader.text = "Card UID"
        carduidHeader.textSize = 24f
        carduidHeader.gravity = Gravity.CENTER
        carduidHeader.setPadding(8, 16, 8, 16)
        headerRow.addView(carduidHeader)

        val timeHeader = TextView(this)
        timeHeader.text = "Time"
        timeHeader.textSize = 24f
        timeHeader.gravity = Gravity.CENTER
        timeHeader.setPadding(8, 16, 8, 16)
        headerRow.addView(timeHeader)

        headerRow.setBackgroundColor(Color.parseColor("#6200EE"))
        tableLayout.addView(headerRow)
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)

        val apiUrl = prefs.getString("apiUrl", "default_value")
        val apiPort = prefs.getString("apiPort", "default_value")

        val apiReq = ApiReq
        apiReq.post("http://$apiUrl:$apiPort/api/log/all", "") { response, error ->
            if (response == null) {
                //Toast.makeText(this, "Server Error", Toast.LENGTH_SHORT).show()
                Log.d("LoginActivity", "Server Error")
            }
            else {
                val jsonArray = JSONArray(response)
                val Logs = ArrayList<HashMap<String, String>>()
                for (i in 0 until jsonArray.length()) {
                    val log = jsonArray.getJSONObject(i)
                    val logMap = HashMap<String, String>()
                    logMap["doorName"] = log.getString("doorName")
                    logMap["cardUid"] = log.getString("cardUid")
                    logMap["time"] = log.getString("time")
                    logMap["accessGranted"] = log.getString("accessGranted")
                    Logs.add(logMap)
                }

                for (log in Logs){
                    Log.d("Log: ", log.toString())
                    val dataRow = TableRow(this)
                    dataRow.layoutParams = TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT
                    )

                    val doorName = TextView(this)
                    doorName.text = log["doorName"]
                    doorName.textSize = 20f
                    doorName.gravity = Gravity.CENTER
                    doorName.setPadding(0, 16, 8, 16)

                    val cardUid = TextView(this)
                    cardUid.text = log["cardUid"]
                    cardUid.textSize = 20f
                    cardUid.gravity = Gravity.CENTER
                    cardUid.setPadding(8, 16, 8, 16)

                    val time = TextView(this)
                    time.text = log["time"]
                    time.textSize = 18f
                    time.gravity = Gravity.CENTER
                    time.setPadding(8, 16, 8, 16)

                    dataRow.addView(doorName)
                    dataRow.addView(cardUid)
                    dataRow.addView(time)

                    if (log["accessGranted"] == "1"){
                        dataRow.setBackgroundColor(Color.parseColor("#44BB44"))
                    }
                    else{
                        dataRow.setBackgroundColor(Color.parseColor("#FF2222"))
                    }

                    tableLayout.addView(dataRow)

                }

                runOnUiThread {scrollView.addView(tableLayout)}

            }
        }
    }
}